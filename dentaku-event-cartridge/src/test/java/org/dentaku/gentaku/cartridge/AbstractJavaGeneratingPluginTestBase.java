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
package org.dentaku.gentaku.cartridge;

import org.generama.MetadataProvider;
import org.codehaus.plexus.embed.Embedder;
import org.dentaku.services.metadata.JMICapableMetadataProvider;

public abstract class AbstractJavaGeneratingPluginTestBase extends org.dentaku.gentaku.AbstractJavaGeneratingPluginTestBase {
    protected MetadataProvider createMetadataProvider() throws Exception {
        Embedder e = new Embedder();
        // we are never directly instantiated
        e.setConfiguration(getClass().getSuperclass().getResource("TestConfiguration.xml"));
        e.start();
        return (MetadataProvider) e.lookup(JMICapableMetadataProvider.ROLE);
    }
}
