/*
 * Created on Nov 29, 2004
 *
 * Copyright STPenable Ltd. (c) 2004
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
import java.util.Map;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.TaggedValueImpl;

/**
 * Creates the pull tool that extends the Base Pulltool for each of 
 * the objects needed in a screen
 * 
 * @author <a href="mailto:david@dwynter.plus.com">David Wynter</a>
 */

public class PullToolPlugin extends JavaPluginBase {

    public PullToolPlugin(VelocityTemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        setStereotype("Entity");
        setCreateonly(false);
    }
    /* (non-Javadoc)
	 * @see org.generama.Plugin#getMetadata()
	 */
	public Collection getMetadata() {
        return ((JMICapableMetadataProvider)metadataProvider).getJMIMetadata();
	}
    public boolean shouldGenerate(Object metadata) {
        Collection stereotypes = ((ModelElementImpl)metadata).getStereotypeNames();
        return stereotypes.contains("RootSelectable");
    }
    protected void populateContextMap(Map m) {
        super.populateContextMap(m);
    	ClassifierImpl metadata = null;
		try {
			metadata = (ClassifierImpl)m.get("metadata");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(metadata!=null) {
	        m.put(SummitHelper.SCRN_NAME, ((TaggedValueImpl)metadata.getTaggedValue(SummitHelper.SCRN_NAME)).getValue());
	        m.put("qualifiedScreenName", 
	        		metadataProvider.getOriginalPackageName(metadata)
	        		+ ((TaggedValueImpl)((ModelElementImpl)metadata).getTaggedValue(SummitHelper.SCRN_NAME)).getValue());
    	}
    }
    public String getDestinationFilename(Object metadata) {
    	String destName = metadataProvider.getOriginalPackageName(metadata) + ".tools."
		+((TaggedValueImpl)((ModelElementImpl)metadata).getTaggedValue(SummitHelper.SCRN_NAME)).getValue()
		+"PullTool";
    	return destName.replaceAll("\\.", "/");
    }
}
