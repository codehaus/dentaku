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
import java.util.Iterator;
import java.util.Map;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.TaggedValueImpl;
import org.omg.uml.foundation.core.TaggedValue;

/**
 * @author <a href="mailto:david@dwynter.plus.com">David Wynter</a>
 *
 * Navigates the metamodel to instantiate ClassView and
 * AttributeView classes then used these generate the CRUDAction
 * source for each of the screens
 */
public class CrudActionPlugin extends JavaPluginBase {
	
	private Collection metadata;
	private ClassView rootClassView = null;
	private SummitHelper helper =null;

    public CrudActionPlugin(VelocityTemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        setStereotype("RootSelectable");
        setCreateonly(true);
    }

    public Collection getMetadata() {
        return ((JMICapableMetadataProvider)metadataProvider).getJMIMetadata();
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
        m.put(SummitHelper.SCRN_NAME, ((TaggedValueImpl)metadata.getTaggedValue(SummitHelper.SCRN_NAME)).getValue());
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
        return pack + ".actions";
    }

    public String getDestinationFilename(Object metadata) {
        TaggedValue taggedValue = ((ModelElementImpl) metadata).getTaggedValue(SummitHelper.SCRN_NAME);
        if(taggedValue != null) {
        	Collection tags = taggedValue.getDataValue();
        	for (Iterator it = tags.iterator(); it.hasNext();) {
        		Object name = it.next();
                if (name instanceof String) {
                    return "Crud" + name + ".java";
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
