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
package org.dentaku.gentaku.cartridge.persistence;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.gentaku.cartridge.persistence.qtags.TagLibrary;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.dentaku.services.metadata.QDoxMetadataProvider;
import org.generama.JellyTemplateEngine;
import org.generama.WriterMapper;

import java.util.Collection;

public class JDOEntityPlugin extends JavaPluginBase {
    JMICapableMetadataProvider metadataProvider;

    public JDOEntityPlugin(JellyTemplateEngine jellyTemplateEngine, QDoxMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(jellyTemplateEngine, metadataProvider, writerMapper);
        this.metadataProvider = metadataProvider;
        getStereotypes().add("Entity");
        setFileregex(".java");
        setFilereplace("Base.java");
        setMultioutput(false);
        new TagLibrary(metadataProvider);

    }

    protected Collection getMetadata() {
        return metadataProvider.getJMIMetadata();
    }


}
