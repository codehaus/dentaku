/*
 * EntityBasePluginTest.java
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

import org.generama.Plugin;
import org.generama.MetadataProvider;
import org.generama.WriterMapper;
import org.generama.VelocityTemplateEngine;
import org.dentaku.services.metadata.QDoxMetadataProvider;
import org.dentaku.gentaku.AbstractJavaGeneratingPluginTestBase;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.FileNotFoundException;
import java.io.File;

public class EntityBasePluginTest extends AbstractJavaGeneratingPluginTestBase {
    protected Plugin createPlugin(MetadataProvider metadataProvider, WriterMapper writerMapper) throws Exception {
        return new EntityBasePlugin(new VelocityTemplateEngine(), (QDoxMetadataProvider) metadataProvider, writerMapper);
    }

    protected URL getExpected() throws FileNotFoundException, MalformedURLException {
        String basedir = System.getProperty("gentaku.entity.home");
        assertNotNull(basedir);
        return new File(basedir + "/src/expected/org/dentaku/cartridge/expected/UserBase.java").toURL();
    }
}
