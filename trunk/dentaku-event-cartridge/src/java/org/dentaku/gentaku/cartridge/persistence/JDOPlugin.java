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

import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.JellyTemplateEngine;
import org.generama.Plugin;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;

import java.util.Collection;

public class JDOPlugin extends Plugin {
    private JMICapableMetadataProvider metadataProvider;
    public JDOPlugin(JellyTemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        this.metadataProvider = metadataProvider;
    }

    protected Collection getMetadata() {
        return metadataProvider.getJMIMetadata();
    }

    public boolean isMultioutput() {
        return false;
    }

    public boolean shouldGenerate(Object metadata) {
        boolean b = ((ModelElementImpl)metadata).getStereotypeNames().contains("Entity");
        return b;
    }
}
