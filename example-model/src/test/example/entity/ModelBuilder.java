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
package example.entity;

import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.dentaku.services.metadata.JMIUMLMetadataProvider;
import org.dentaku.services.metadata.RepositoryReader;
import org.dentaku.services.metadata.Utils;
import org.dentaku.services.metadata.nbmdr.MagicDrawRepositoryReader;
import org.dentaku.gentaku.tools.cgen.plugin.GenGenPlugin;
import org.dentaku.gentaku.Gentaku;
import org.generama.Generama;

import java.io.File;

public class ModelBuilder extends junit.framework.TestCase {
    public Generama plugin;

    protected void setUp() throws Exception {
        String filename = "example-model/src/uml/model.xml.zip";
        RepositoryReader rr = new MagicDrawRepositoryReader(Utils.checkURL(new File(Utils.getRootDir(), filename).toURL()));
        JMICapableMetadataProvider mp = new JMIUMLMetadataProvider(rr);
        plugin = new Generama(mp);
    }

    public void testSomething() throws Exception {
        plugin.start();
    }
}
