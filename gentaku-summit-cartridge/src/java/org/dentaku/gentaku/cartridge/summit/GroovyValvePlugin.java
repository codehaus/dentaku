/*
 * Created on Feb 10, 2005
 *
 * Copyright STPenable Ltd. (c) 2005
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
package org.dentaku.gentaku.cartridge.summit;

import java.util.Collection;
import java.util.Iterator;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;

/**
 * @author David Wynter
 *
 * This generates a class that is a container for Groovy scripts used
 * to validate objects in form submissions. Resultant code uses Plexus
 * Groovy component.
 */
public class GroovyValvePlugin  extends JavaPluginBase {

	/**
	 * @author David Wynter
	 *
	 * required
	 *
	 * @param templateEngine
	 * @param metadataProvider
	 * @param writerMapper
	 */
	public GroovyValvePlugin(VelocityTemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
		super(templateEngine, metadataProvider, writerMapper);
        setStereotype(SummitHelper.GROOVY);
        setCreateonly(true);
	}

	private Collection metadata;

	/* (non-Javadoc)
	 * @see org.generama.Plugin#getMetadata()
	 */
	protected Collection getMetadata() {
        return ((JMICapableMetadataProvider)metadataProvider).getJMIMetadata();
	}

	public boolean shouldGenerate(Object metadata) {
        String stereotypeName = null;
        boolean result = false;
        for (Iterator it = stereotypes.iterator(); it.hasNext();) {
            stereotypeName = (String) it.next();
            if (matchesStereotype((ModelElementImpl) metadata, stereotypeName)) {
                result = true;
            }
        }
        return result;
    }
    
    public String getDestinationPackage(Object metadata) {
    	String pack = super.getDestinationPackage(metadata);
        return pack;
    }

    public String getDestinationFilename(Object metadata) {
        String name = ((ClassifierImpl)metadata).getName();
        return name + ".groovy";
    }

}
