/*
 * CopyrightPlugin (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.metadata;

import org.generama.MetadataProvider;
import org.omg.uml.UmlPackage;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.jmi.model.MofPackage;
import javax.jmi.model.ModelPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.jmi.reflect.RefPackage;
import java.util.Collection;
import java.util.Iterator;
import java.net.URL;
import java.io.IOException;

public class JMIUMLMetadataProvider implements MetadataProvider {
    private RepositoryReader reader;
    protected UmlPackage model;
    private static Log log = LogFactory.getLog(JMIUMLMetadataProvider.class);
    protected final static String META_PACKAGE = "UML";
    
    static {
        // configure MDR to use an in-memory storage implementation
        System.setProperty(
            "org.netbeans.mdr.storagemodel.StorageFactoryClassName",
            "org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");
        // set the logging so output does not go to standard out
        System.setProperty("org.netbeans.lib.jmi.Logger.fileName", "mdr.log");
    };

    public JMIUMLMetadataProvider(RepositoryReader reader) {
        this.reader = reader;
        try {
            setupModel();
        } catch (RepositoryException e) {
            throw new RuntimeException("Couldn't parse UML", e);
        }
    }

    public Collection getMetadata() {
        return getModel().getCore().getModelElement().refAllOfType();
    }

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

    public UmlPackage getModel() {
        return model;
    }

    private void setupModel() throws RepositoryException {
        if (model == null) {
            log.info("creating repository");
            MDRepository repository = MDRManager.getDefault().getDefaultRepository();
            try {
                URL metamodel = this.getClass().getResource("/M2_DiagramInterchangeModel.xml");
                MofPackage metaModel = loadMetaModel(metamodel, repository);
                model = loadModel(metaModel, repository);
            } catch (Exception e) {
                throw new RepositoryException("could not instantiate repostiory", e);
            }
        }
    }

    private MofPackage loadMetaModel(URL metaModelURL, MDRepository repository) throws CreationFailedException, IOException, MalformedXMIException {
        log.info("MDR: creating MetaModel using URL =" + metaModelURL.toExternalForm());

        // Use the metaModelURL as the name for the repository extent.
        // This ensures we can load mutiple metamodels without them colliding.
        ModelPackage metaModelExtent = (ModelPackage) repository.getExtent(metaModelURL.toExternalForm());
        if (metaModelExtent == null) {
            metaModelExtent = (ModelPackage) repository.createExtent(metaModelURL.toExternalForm());
        }
        MofPackage metaModelPackage = findPackage(META_PACKAGE, metaModelExtent);
        if (metaModelPackage == null) {
            XMIReader xmiReader = XMIReaderFactory.getDefault().createXMIReader();
            xmiReader.read(metaModelURL.toExternalForm(), metaModelExtent);

            // locate the UML package definition that was just loaded in
            metaModelPackage = findPackage(META_PACKAGE, metaModelExtent);
        }
        log.info("MDR: created MetaModel");
        return metaModelPackage;
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

    /**
     * Loads a model into the repository and validates the model against
     * the given metaModel.
     *
     * @param modelURL   url of model
     * @param repository netbeans MDR
     * @param metaModel  meta model of model
     * @return populated model
     * @throws CreationFailedException unable to create model in repository
     * @throws IOException             unable to read model
     * @throws MalformedXMIException   model violates metamodel
     */
    private UmlPackage loadModel(MofPackage metaModel, MDRepository repository) throws CreationFailedException, IOException, MalformedXMIException {
        log.info("MDR: creating Model");
        RefPackage model = repository.getExtent("MODEL");
        if (model != null) {
            log.info("MDR: deleting exising model");
            model.refDelete();
        }
        log.info("MDR: creating model extent");
        model = repository.createExtent("MODEL", metaModel);
        log.info("MDR: created model extent");
        log.info("MDR: reading XMI");
        reader.readInputStream(model);
        log.info("MDR: read XMI ");
        log.info("MDR: created Model");
        return (UmlPackage) model;
    }
}
