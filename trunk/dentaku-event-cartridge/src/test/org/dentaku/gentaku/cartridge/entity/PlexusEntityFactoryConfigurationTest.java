/*
 * PlexusEntityFactoryConfigurationTest.java
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
package org.dentaku.gentaku.cartridge.entity;

import org.dentaku.gentaku.AbstractXMLGeneratingPluginTestBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.JellyTemplateEngine;
import org.generama.MetadataProvider;
import org.generama.Plugin;
import org.generama.WriterMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

public class PlexusEntityFactoryConfigurationTest extends AbstractXMLGeneratingPluginTestBase {


    protected Plugin createPlugin(MetadataProvider metadataProvider, WriterMapper writerMapper) throws Exception {
        return new PlexusEntityFactoryConfiguration(new JellyTemplateEngine(), (JMICapableMetadataProvider) metadataProvider, writerMapper);
    }

    protected URL getExpected() throws FileNotFoundException, MalformedURLException {
        String basedir = System.getProperty("gentaku.entity.home");
        assertNotNull(basedir);
        return new File(basedir + "/src/expected/PlexusEntityConfig.xml").toURL();
    }

}
