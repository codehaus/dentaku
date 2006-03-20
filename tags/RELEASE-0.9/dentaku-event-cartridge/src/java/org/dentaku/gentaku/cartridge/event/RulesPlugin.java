/*
 * RulesPlugin.java
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
package org.dentaku.gentaku.cartridge.event;

import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.JellyTemplateEngine;
import org.generama.Plugin;
import org.generama.WriterMapper;

import java.util.Collection;
import java.util.Map;

public class RulesPlugin extends Plugin {
    private JMICapableMetadataProvider metadataProvider;

    public RulesPlugin(JellyTemplateEngine jellyTemplateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(jellyTemplateEngine, metadataProvider, writerMapper);
        this.metadataProvider = metadataProvider;
        setEncoding("UTF-8");
    }

    public String getDestinationFilename(Object metadata) {
        return "drools.drl";
    }

    protected Collection getMetadata() {
        return metadataProvider.getJMIMetadata();
    }
}
