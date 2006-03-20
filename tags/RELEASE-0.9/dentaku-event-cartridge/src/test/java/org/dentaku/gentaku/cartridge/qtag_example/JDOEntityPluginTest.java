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
package org.dentaku.gentaku.cartridge.qtag_example;

import org.dentaku.gentaku.cartridge.AbstractXMLGeneratingPluginTestBase;
import org.dentaku.gentaku.cartridge.qtag_example.JDOEntityPlugin;
import org.dentaku.services.metadata.QDoxMetadataProvider;
import org.generama.JellyTemplateEngine;
import org.generama.MetadataProvider;
import org.generama.Plugin;
import org.generama.WriterMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

public class JDOEntityPluginTest extends AbstractXMLGeneratingPluginTestBase {
    protected Plugin createPlugin(MetadataProvider metadataProvider, WriterMapper writerMapper) throws Exception {
        return new JDOEntityPlugin(new JellyTemplateEngine(), (QDoxMetadataProvider)metadataProvider, writerMapper);
    }

    protected URL getExpected() throws FileNotFoundException, MalformedURLException {
        String basedir = System.getProperty("user.dir");
        assertNotNull(basedir);
        return new File(basedir + "/dentaku-event-cartridge/src/expected/JDOConfig.xml").toURL();
    }
}
