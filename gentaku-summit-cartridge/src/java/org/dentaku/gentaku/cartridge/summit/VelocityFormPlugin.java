/*
 * Created on Dec 2, 2004
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.omg.uml.foundation.core.TaggedValue;

/**
 * Creates the Velocity screen template, once only, a limitation.
 * Maybe an improvement might be to move as much as possible of 
 * the layout into Velocity macros, but then maybe XML+XSLT is
 * more suited to generated screen, separation of data and layout 
 * 
 * @author <a href="mailto:david@dwynter.plus.com">David Wynter</a>
 */
public class VelocityFormPlugin extends JavaPluginBase {
	
	private ClassView rootClassView = null;
	private SummitHelper helper =null;

	List dependentDisplayAttributes = new ArrayList();

    public VelocityFormPlugin(VelocityTemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        ArrayList stereos = new ArrayList();
        setStereotype("RootSelectable");
     
        this.metadataProvider = metadataProvider;
        setCreateonly(true);
        setMultioutput(true);
    }

    protected void populateContextMap(Map m) {
        super.populateContextMap(m);
        helper = new SummitHelper();
    	ClassifierImpl metadata = null;
		try {
			metadata = (ClassifierImpl)m.get("metadata");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(metadata!=null) {
    		rootClassView = helper.buildClassView(metadata, null);
    	}
        m.put("rootClassView", rootClassView);
        m.put("SummitHelper", helper);
    }
    
    public Collection getMetadata() {
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
    public String getDestinationFilename(Object metadata) {
        TaggedValue taggedValue = ((ModelElementImpl) metadata).getTaggedValue(SummitHelper.SCRN_NAME);
        if(taggedValue != null) {
        	Collection tags = taggedValue.getDataValue();
        	for (Iterator it = tags.iterator(); it.hasNext();) {
        		Object name = it.next();
                if (name instanceof String) {
                    return (String)name + ".vm";
                } else {
                	//error in model
                }
                // error in model, should always return on first elem in Collection, should only be one
        	}
        } else {
        	// error logging needed here, model not correct
        }
    	return null;
    }
}