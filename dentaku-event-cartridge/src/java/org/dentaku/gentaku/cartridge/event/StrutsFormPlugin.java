/*
 * StrutsFormPlugin.java
 * Copyright 2002-2004 Bill2, Inc.
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

import org.dentaku.services.metadata.JMIUMLMetadataProvider;
import org.dentaku.gentaku.cartridge.UMLUtils;
import org.generama.MetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.generama.defaults.JavaGeneratingPlugin;

import java.util.Collection;

public class StrutsFormPlugin extends JavaGeneratingPlugin {
    protected UMLUtils umlUtils;

    public StrutsFormPlugin(VelocityTemplateEngine templateEngine, MetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        umlUtils = UMLUtils.getInstance((JMIUMLMetadataProvider)getMetadataProvider(), this);
        setMultioutput(true);
    }

    public boolean shouldGenerate(Object metadata) {
        Collection stereotypes = umlUtils.getStereotypeNames(metadata);
        return stereotypes.contains("StrutsForm");
    }

    public UMLUtils getUmlUtils() {
        return umlUtils;
    }
}