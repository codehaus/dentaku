/*
 * POJOPlugin.java
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
package org.dentaku.gentaku.cartridge.core;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;

import java.util.Collection;

public class POJOPlugin extends JavaPluginBase {
    private JMICapableMetadataProvider metadataProvider;
    private String extend;

    public POJOPlugin(VelocityTemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        this.metadataProvider = metadataProvider;
        setMultioutput(true);
    }

    protected Collection getMetadata() {
        return metadataProvider.getJMIMetadata();
    }
    
    public boolean shouldGenerate(Object metadata) {
    	if(super.shouldGenerate(metadata)) {
    		return true;
    	} else {
            boolean result = false;
            result = matchesStereotype((ModelElementImpl) metadata, "POJO");
            return result;
    	}
    }

    public String getExtends() {
        return extend;
    }

    public void setExtends(String extend) {
        this.extend = extend;
    }
}
