/*
 * RulesPlugin.java
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
import org.generama.JellyTemplateEngine;
import org.generama.Plugin;
import org.generama.WriterMapper;

public class RulesPlugin extends Plugin {
    protected UMLUtils umlUtils;

    public RulesPlugin(JellyTemplateEngine jellyTemplateEngine, JMIUMLMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(jellyTemplateEngine, metadataProvider, writerMapper);
        umlUtils = UMLUtils.getInstance((JMIUMLMetadataProvider)getMetadataProvider(), this);
        setEncoding("UTF-8");
    }

    public String getDestinationFilename(Object metadata) {
        return "drools.drl";
    }

    public UMLUtils getUmlUtils() {
        return umlUtils;
    }

    public String getDestinationClassname(Object metadata) {
        String destinationFilename = getDestinationFilename(metadata);
        return destinationFilename.substring(0, destinationFilename.indexOf('.'));
    }

    public String getDestinationFullyQualifiedClassName(Object metadata) {
        String packageName = getDestinationPackage(metadata);
        packageName = packageName.equals("") ? "" : packageName + ".";
        return packageName + getDestinationClassname(metadata);
    }
}
