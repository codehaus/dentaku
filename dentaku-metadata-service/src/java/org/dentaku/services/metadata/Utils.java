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
package org.dentaku.services.metadata;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;

public class Utils {
    public static String getRootDir() {
        String rootdir = System.getProperty("dentaku.rootdir");
        if (rootdir == null) {
            rootdir = System.getProperty("user.dir");
        }
        return rootdir + "/";
    }

    public static URL checkURL(URL check) {
        URL result = null;
        if (check != null) {
            try {
                InputStream is = check.openStream();
                is.close();
                result = check;
            } catch (IOException e) { }
        }
        return result;
    }

    public static URL checkURL(String check) {
        URL result = null;
        if (check != null) {
            try {
                result = checkURL(new File(check).toURL());
            } catch (MalformedURLException e) { }
        }
        return result;
    }
}
