/*
 * PlexusEntityFactoryConfiguration.java
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
package org.dentaku.gentaku.cartridge.entity;

import org.dentaku.gentaku.cartridge.UMLUtils;
import org.dentaku.services.metadata.JMIUMLMetadataProvider;
import org.dentaku.services.metadata.SimpleOOHelper;
import org.generama.JellyTemplateEngine;
import org.generama.MetadataProvider;
import org.generama.WriterMapper;
import org.generama.defaults.JavaGeneratingPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlexusEntityFactoryConfiguration extends JavaGeneratingPlugin {
    protected UMLUtils umlUtils;
    private List stereotypes = new ArrayList();
    private String configFilename = "org/dentaku/conf/persistence/factory.xml";
    private SimpleOOHelper simpleOOHelper;

    public PlexusEntityFactoryConfiguration(JellyTemplateEngine jellyTemplateEngine, MetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(jellyTemplateEngine, metadataProvider, writerMapper);
        JMIUMLMetadataProvider mp = (JMIUMLMetadataProvider) getMetadataProvider();
        simpleOOHelper = SimpleOOHelper.getInstance(mp.getModel(), this);
        umlUtils = UMLUtils.getInstance((JMIUMLMetadataProvider)getMetadataProvider(), this);
        setEncoding("UTF-8");
        getStereotypes().add("Entity");
        getStereotypes().add("SubtypeEntity");
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

    public boolean shouldGenerate(Object metadata) {
        String stereotypeName = null;
        if (stereotypes.size() == 0) {
            String className = getClass().getName();
            String pluginName = className.substring(className.lastIndexOf(".") + 1);
            stereotypeName = pluginName.substring(0, pluginName.indexOf("Plugin"));
            return umlUtils.matchesStereotype(metadata, stereotypeName);
        } else {
            for (Iterator it = stereotypes.iterator(); it.hasNext();) {
                stereotypeName = (String) it.next();
                if (umlUtils.matchesStereotype(metadata, stereotypeName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public UMLUtils getUmlUtils() {
        return umlUtils;
    }

    public List getStereotypes() {
        return stereotypes;
    }

    public void setStereotypes(List stereotypes) {
        this.stereotypes = stereotypes;
    }

    public void start() {
        Map context = getContextObjects();
        context.put("transform", simpleOOHelper);
        super.start();
    }
}
