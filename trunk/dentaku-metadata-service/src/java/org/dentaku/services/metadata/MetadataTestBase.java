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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Collection;

import junit.framework.TestCase;

import org.codehaus.plexus.embed.Embedder;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.ModelElementClass;

public class MetadataTestBase extends TestCase {
    private Embedder e;
    protected Collection metadata;
    protected URL resource = null;
    public static final String DEFAULTCONF = "DefaultContainerConfiguration.xml";

    protected void setUp() throws Exception {
        e = new Embedder();

        if (resource == null) {
            // if no config override, take a guess
            String className = getClass().getName();
            String name = className.substring(className.lastIndexOf(".") + 1) + ".xml";
            URL synthetic = getClass().getResource(name);
            URL paths[] = new URL[]{ synthetic, this.getClass().getResource(DEFAULTCONF), MetadataTestBase.class.getResource(DEFAULTCONF) };
            for (int i = 0; i < paths.length; i++) {
                if (paths[i] != null &&  new File(paths[i].getFile()).exists()) {
                    resource = paths[i];
                    break;
                }
            }
            if (resource == null) {
                throw new FileNotFoundException("can't find any resource configuration!");
            }
        }

        System.out.println("Container configured from '" + resource.toExternalForm() +"'");
        e.setConfiguration(resource);
        e.start();
        JMICapableMetadataProvider ms = (JMICapableMetadataProvider) e.lookup(JMICapableMetadataProvider.ROLE);
        UmlPackage model = ms.getModel();
        CorePackage core = model.getCore();
        ModelElementClass modelElement = core.getModelElement();
        this.metadata = modelElement.refAllOfType();
    }
}
