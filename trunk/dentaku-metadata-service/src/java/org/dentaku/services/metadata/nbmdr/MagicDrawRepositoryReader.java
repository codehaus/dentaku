/*
 * MagicDrawRepositoryReader.java
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
package org.dentaku.services.metadata.nbmdr;

import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.omg.uml.UmlPackage;
import org.dentaku.services.metadata.RepositoryReader;
import org.dentaku.services.metadata.RepositoryException;
import org.dentaku.services.metadata.JMIUMLMetadataProvider;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Iterator;

/**
 * This used to be completely URL based, then something broke with XStream because we have these crappy version conflicts
 * with Plexus.  When that's fixed, change the model field back to a URL and get this cleaned up.
 */
public class MagicDrawRepositoryReader implements RepositoryReader {
    static {
        // set the logging so output does not go to standard out
        System.setProperty("org.netbeans.lib.jmi.Logger.fileName", "mdr.log");

        // configure MDR to use an in-memory storage implementation
        System.setProperty("org.netbeans.mdr.storagemodel.StorageFactoryClassName", "org.netbeans.mdr.persistence.memoryimpl.StorageFactoryImpl");

        // configure MDR to use JDBC storage implementation
//        System.setProperty("org.netbeans.mdr.storagemodel.StorageFactoryClassName", "org.netbeans.mdr.persistence.jdbcimpl.JdbcStorageFactory");
//        System.setProperty("MDRStorageProperty.org.netbeans.mdr.persistence.jdbcimpl.driverClassName", "org.netbeans.mdr.persistence.jdbcimpl.JdbcStorageFactory");
//        # MDRStorageProperty.org.netbeans.mdr.persistence.jdbcimpl.driverClassName = the
//        fully qualified
//        name of
//        the JDBC
//        driver to
//        use(e.g.org.postgresql.Driver)
//        # MDRStorageProperty.org.netbeans.mdr.persistence.jdbcimpl.url = the
//        JDBC URL
//        to use
//        to connect
//        to the
//        database(e.g.jdbc:postgresql://localhost/mdrdb)
//        # MDRStorageProperty.org.netbeans.mdr.persistence.jdbcimpl.schemaName =
    };

    private String model;
    private Logger log = Logger.getLogger(MagicDrawRepositoryReader.class.getName());
    protected final static String META_PACKAGE = "UML";
    public static final String MODEL_NAME = "MODEL";

    public MagicDrawRepositoryReader() {
        // i sure hope pico is initialized correctly
    }

    public MagicDrawRepositoryReader(URL modelURL) {
        this.model = modelURL.toString();
    }

    public UmlPackage getModel() throws RepositoryException {
        UmlPackage model = null;
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
            readInputStream(model1);
            log.info("MDR: read XMI ");
            log.info("MDR: created Model");
            model = (UmlPackage) model1;
            repository.endTrans();
        } catch (Exception e) {
            repository.endTrans(true);
            throw new RepositoryException("could not instantiate repostiory", e);
        }
        return model;
    }

    private void readInputStream(RefPackage umlModel) throws IOException, MalformedXMIException {
        String fullname = model;
        // the embedded XMI is always saved as the filename minus the ".zip" suffix
        String filename = fullname.substring(fullname.lastIndexOf("/")+1, fullname.indexOf(".zip"));

        // check file exists
        URL url = null;
        try {
            url = new URL(model);
        } catch (MalformedURLException e) {
            // try relative path on classpath
            // HACK: this should have a registered URL handler for res://
            url = new URL(getClass().getClassLoader().getResource(""), model);
        }
        File file = new File(url.getFile());
        if (!file.exists()) {
            throw new FileNotFoundException(url.getFile() + " could not be found");
        }

        // get the input stream
        System.out.println("Reading from MagicDraw repository: "+ fullname);
        ZipFile zip = new ZipFile(file);
        ZipEntry entry = zip.getEntry(filename);
        InputStream input = zip.getInputStream(entry);
        XMIReader xmiReader = XMIReaderFactory.getDefault().createXMIReader();
        xmiReader.read(input, fullname, umlModel);
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
}
