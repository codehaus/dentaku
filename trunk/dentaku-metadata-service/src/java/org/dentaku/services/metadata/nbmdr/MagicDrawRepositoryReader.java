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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;

import org.apache.tools.ant.types.FileSet;
import org.dentaku.services.metadata.JMIUMLMetadataProvider;
import org.dentaku.services.metadata.RepositoryException;
import org.dentaku.services.metadata.RepositoryReader;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.omg.uml.UmlPackage;

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
    private URL modelURL;
    private List searchPaths = new ArrayList();
    private Logger log = Logger.getLogger(MagicDrawRepositoryReader.class.getName());
    protected final static String META_PACKAGE = "UML";
    public static final String MODEL_NAME = "MODEL";
    private UmlPackage modelPackage = null;

    public MagicDrawRepositoryReader() {
        // i sure hope pico is initialized correctly
    }

    public MagicDrawRepositoryReader(URL modelURL) {
        this.modelURL = modelURL;
    }

    public MagicDrawRepositoryReader(URL modelURL, Collection fileSet) {
        this.modelURL = modelURL;
        for (Iterator it = fileSet.iterator(); it.hasNext();) {
            FileSet set = (FileSet) it.next();
            searchPaths.add(set.getDir(null).getAbsolutePath());
        }
    }

    public UmlPackage getModel() throws RepositoryException {
        if (modelPackage == null) {
            log.info("creating repository");
            MDRepository repository = MDRManager.getDefault().getDefaultRepository();
//            repository.beginTrans(true);
            try {
                URL metamodel = this.getClass().getResource("/M2_DiagramInterchangeModel.xml");

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

                MofPackage metaModel = metaModelPackage;
                JMIUMLMetadataProvider.booted = true;
                RefPackage model1 = repository.getExtent(MODEL_NAME);
                if (model1 != null) {
                    log.info("MDR: deleting exising model");
                    model1.refDelete();
                }
                model1 = repository.createExtent(MODEL_NAME, metaModel);
                readInputStream(model1);
                modelPackage = (UmlPackage) model1;
//                repository.endTrans();
            } catch (Exception e) {
//                repository.endTrans(true);
                throw new RepositoryException("could not instantiate repostiory", e);
            }
        }
        return modelPackage;
    }

    private void readInputStream(RefPackage umlModel) throws IOException, MalformedXMIException {
        System.out.println("Rootdir is: "+ XMIInputConfigImpl.getRootDir());
        String fullname = model;
        URL url = null;
        if (model != null) {
            // the embedded XMI is always saved as the filename minus the ".zip" suffix

            // check file exists
            url = XMIInputConfigImpl.checkURL(model);
            if (url == null) {
                url = XMIInputConfigImpl.checkURL(XMIInputConfigImpl.getRootDir() + model);
            }
        } else if (modelURL != null) {
            try {
                url = XMIInputConfigImpl.checkURL(modelURL);
                fullname = url.toExternalForm();
            } catch (Exception e) { }
        }

        if (url == null) {
            throw new FileNotFoundException(model + " could not be found");
        }

        // get the input stream
        String filename = fullname.substring(fullname.lastIndexOf("/") + 1, fullname.indexOf(".zip"));
        System.out.println("Reading from MagicDraw repository: " + fullname);
        ZipFile zip = new ZipFile(url.getFile());
        ZipEntry entry = zip.getEntry(filename);
        InputStream input = null;
        try {
            input = zip.getInputStream(entry);
        } catch (IOException e) {
            log.severe("couldn't find file in archive.  Make sure you have not recently renamed the file at the OS level and/or re-save");
            throw e;
        }
        XMIReader xmiReader = XMIReaderFactory.getDefault().createXMIReader(new XMIInputConfigImpl(new RefPackage[]{umlModel}, searchPaths));
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
