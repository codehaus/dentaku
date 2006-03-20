/*
 * PlexusEntityFactoryConfiguration.java
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

import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.JellyTemplateEngine;
import org.generama.Plugin;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;

import java.util.Collection;

public class PlexusEntityFactoryConfiguration extends Plugin {
    private JMICapableMetadataProvider metadataProvider;
    private String configFilename = "org/dentaku/conf/persistence/factory.xml";

    public PlexusEntityFactoryConfiguration(JellyTemplateEngine jellyTemplateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(jellyTemplateEngine, metadataProvider, writerMapper);
        this.metadataProvider = metadataProvider;
        setEncoding("UTF-8");
    }

    /**
     * spelled to make XDoclet happy
     * @param metadata
     * @return
     */
    public String getDestinationFilename(Object metadata) {
        return configFilename;
    }

    /**
     * spelled to make nanocontainer happy
     * @param filename
     */
    public void setDestinationfilename(String filename) {
        configFilename = filename;
    }

    protected Collection getMetadata() {
        return metadataProvider.getJMIMetadata();
    }

    public boolean shouldGenerate(Object metadata) {
        Collection stereotypes = ((ModelElementImpl) metadata).getStereotypeNames();
        return stereotypes.contains("Entity") || stereotypes.contains("SubtypeEntity");
    }
}
