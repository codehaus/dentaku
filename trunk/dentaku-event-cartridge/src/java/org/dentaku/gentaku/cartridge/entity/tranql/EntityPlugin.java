/*
 * EntityPlugin.java
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
package org.dentaku.gentaku.cartridge.entity.tranql;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class EntityPlugin extends JavaPluginBase {
    private JMICapableMetadataProvider metadataProvider;

    public EntityPlugin(VelocityTemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        this.metadataProvider = metadataProvider;
        getStereotypes().add("Entity");
        setCreateonly(true);
        setMultioutput(true);
    }

    protected void populateContextMap(Map m) {
        super.populateContextMap(m);
        m.put("class", m.get("metadata"));
    }

    protected Collection getMetadata() {
        return metadataProvider.getJMIMetadata();
    }
}
