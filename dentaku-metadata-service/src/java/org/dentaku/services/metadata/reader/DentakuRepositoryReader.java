/**
 *
 *  Copyright 2004 Brian Topping
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dentaku.services.metadata.reader;

import org.apache.log4j.Logger;
import org.dentaku.services.metadata.RepositoryException;
import org.dentaku.services.metadata.RepositoryReader;
import org.netbeans.lib.jmi.xmi.XmiSAXReader;
import org.omg.uml.UmlPackage;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This is a developmental XMI reader
 */
public class DentakuRepositoryReader implements RepositoryReader {
    private Logger log = Logger.getLogger(DentakuRepositoryReader.class);
    private String model = null;

    public UmlPackage getModel() throws RepositoryException {
        try {
            readInputStream(null);
        } catch (Exception e) {
            throw new RepositoryException("caught an exception", e);
        }
        return null;
    }

    private void readInputStream(RefPackage umlModel) throws IOException, MalformedXMIException {
        // the embedded XMI is always saved as the filename minus the ".zip" suffix
        String filename = model.substring(model.lastIndexOf("/") + 1, model.indexOf(".zip"));

        // check file exists
        File file = new File(new URL(model).getFile());
        if (!file.exists()) {
            throw new FileNotFoundException(model + " could not be found");
        }

        // get the input stream
        System.out.println("Reading from MagicDraw repository: " + model);
        ZipFile zip = new ZipFile(file);
        ZipEntry entry = zip.getEntry(filename);
        InputStream input = zip.getInputStream(entry);
        XMISaxReader reader = new XMISaxReader();
        InputSource is = new InputSource(input);
        try {
            reader.read(is);
        } catch (SAXException e) {
            log.error("caught an exception", e);
        } catch (ParserConfigurationException e) {
            log.error("caught an exception", e);
        }
    }
}
