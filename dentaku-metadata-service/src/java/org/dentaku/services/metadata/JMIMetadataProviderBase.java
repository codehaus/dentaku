/*
 * JMIMetadataProviderBase.java
 * Copyright 2004-2004 Bill2, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dentaku.services.metadata;

import com.thoughtworks.qdox.model.DocletTagFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.generama.GeneramaException;
import org.generama.MetadataProvider;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;
import org.omg.uml.modelmanagement.Model;
import org.xdoclet.ConfigurableDocletTagFactory;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

public abstract class JMIMetadataProviderBase implements MetadataProvider {
    public static boolean booted;
    static {
        // configure MDR to use an in-memory storage implementation
        System.setProperty(
            "org.netbeans.mdr.storagemodel.StorageFactoryClassName",
            "org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");
        // set the logging so output does not go to standard out
        System.setProperty("org.netbeans.lib.jmi.Logger.fileName", "mdr.log");
    };

    protected RepositoryReader reader;
    protected UmlPackage model;
    private static Log log = LogFactory.getLog(JMIUMLMetadataProvider.class);
    protected final static String META_PACKAGE = "UML";
    public static final String MODEL_NAME = "MODEL";

    final ConfigurableDocletTagFactory docletTagFactory = new ConfigurableDocletTagFactory();

    // injected parameters

    protected JMIMetadataProviderBase() {
    }

    public JMIMetadataProviderBase(RepositoryReader reader) {
        this.reader = reader;
        try {
            setupModel();
        } catch (RepositoryException e) {
            throw new RuntimeException("Couldn't parse UML", e);
        }
    }

    public abstract Collection getMetadata() throws GeneramaException;

    public String getOriginalFileName(Object object) {
        if ((object == null) || !(object instanceof ModelElement)) {
            return "";
        }
        String result = null;
        ModelElement modelElement = (ModelElement) object;
        if (object instanceof TaggedValue) {
            result = ((TaggedValue) object).getName();

            // sometimes the tag name is on the TagDefinition
            if ((result == null) && (((TaggedValue) object).getType() != null)) {
                result = ((TaggedValue) object).getType().getName();

                // sometimes it is the TagType
                if (result == null) {
                    result = ((TaggedValue) object).getType().getTagType();
                }
            }
        } else {
            result = modelElement.getName();
        }
        return result + ".java";
    }

    public String getOriginalPackageName(Object object) {
        if ((object == null) || !(object instanceof ModelElement)) {
            return "";
        }
        ModelElement modelElement = (ModelElement) object;
        String packageName = "";
        for (ModelElement namespace = modelElement.getNamespace(); (namespace instanceof org.omg.uml.modelmanagement.UmlPackage) && !(namespace instanceof Model); namespace = namespace.getNamespace()) {
            packageName = "".equals(packageName) ? namespace.getName() : namespace.getName() + "." + packageName;
        }
        return packageName;
    }

    public UmlPackage getModel() throws RepositoryException {
        if (model == null) {
            setupModel();
        }
        return model;
    }

    protected void setupModel() throws RepositoryException {
        if (model == null) {
            log.info("creating repository");
            MDRepository repository = MDRManager.getDefault().getDefaultRepository();
            repository.beginTrans(true);
            try {
                URL metamodel = this.getClass().getResource("/M2_DiagramInterchangeModel.xml");
                log.info("MDR: creating MetaModel using URL =" + metamodel.toExternalForm());

                // Use the metaModelURL as the name for the repository extent.
                // This ensures we can load mutiple metamodels without them colliding.
                ModelPackage metaModelExtent = (ModelPackage) repository.getExtent(metamodel.toExternalForm());
                if (metaModelExtent == null) {
                    metaModelExtent = (ModelPackage) repository.createExtent(metamodel.toExternalForm());
                }
                MofPackage metaModelPackage = findPackage(META_PACKAGE, metaModelExtent);
                if (metaModelPackage == null) {
                    XMIReader xmiReader = XMIReaderFactory.getDefault().createXMIReader();
                    xmiReader.read(metamodel.toExternalForm(), metaModelExtent);

                    // locate the UML package definition that was just loaded in
                    metaModelPackage = findPackage(META_PACKAGE, metaModelExtent);
                }
                log.info("MDR: created MetaModel");
                MofPackage metaModel = metaModelPackage;
                JMIUMLMetadataProvider.booted = true;
                log.info("MDR: creating Model");
                RefPackage model1 = repository.getExtent(MODEL_NAME);
                if (model1 != null) {
                    log.info("MDR: deleting exising model");
                    model1.refDelete();
                }
                log.info("MDR: creating model extent");
                model1 = repository.createExtent(MODEL_NAME, metaModel);
                log.info("MDR: created model extent");
                log.info("MDR: reading XMI");
                reader.readInputStream(model1);
                log.info("MDR: read XMI ");
                log.info("MDR: created Model");
                model = (UmlPackage) model1;
                repository.endTrans();
            } catch (Exception e) {
                repository.endTrans(true);
                throw new RepositoryException("could not instantiate repostiory", e);
            }
        }
    }

    /**
     * Searches a meta model for the specified package.
     *
     * @param packageName name of package for which to search
     * @param metaModel   meta model to search
     * @return MofPackage
     */
    private MofPackage findPackage(String packageName, ModelPackage metaModel) {
        for (Iterator it = metaModel.getMofPackage().refAllOfClass().iterator(); it.hasNext();) {
            javax.jmi.model.ModelElement temp = (javax.jmi.model.ModelElement) it.next();
            if (temp.getName().equals(packageName)) {
                return (MofPackage) temp;
            }
        }
        return null;
    }

    public DocletTagFactory getDocletTagFactory() {
        return docletTagFactory;
    }
}
