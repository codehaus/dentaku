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
package org.dentaku.services.metadata;

import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;

import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This used to be completely URL based, then something broke with XStream because we have these crappy version conflicts
 * with Plexus.  When that's fixed, change the model field back to a URL and get this cleaned up.
 */
public class MagicDrawRepositoryReader implements RepositoryReader {
    private String model = null;

    public MagicDrawRepositoryReader() {
        // i sure hope pico is initialized correctly
    }

    public MagicDrawRepositoryReader(URL modelURL) {
        this.model = modelURL.toString();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) throws MalformedURLException {
        this.model = model;
    }

    public void readInputStream(RefPackage umlModel) throws IOException, MalformedXMIException {
        String fullname = model;
        // the embedded XMI is always saved as the filename minus the ".zip" suffix
        String filename = fullname.substring(fullname.lastIndexOf("/")+1, fullname.indexOf(".zip"));

        // check file exists
        File file = null;
        try {
            file = new File(new URL(this.model).getFile());
        } catch (MalformedURLException e) {
            // maybe it's a pathname?
            file = new File(this.model);
        }
        if (!file.exists()) {
            throw new FileNotFoundException(fullname + " could not be found");
        }

        // get the input stream
        System.out.println("Reading from MagicDraw repository: "+ fullname);
        ZipFile zip = new ZipFile(file);
        ZipEntry entry = zip.getEntry(filename);
        InputStream input = zip.getInputStream(entry);
        XMIReader xmiReader = XMIReaderFactory.getDefault().createXMIReader();
        xmiReader.read(input, fullname, umlModel);
    }
}
