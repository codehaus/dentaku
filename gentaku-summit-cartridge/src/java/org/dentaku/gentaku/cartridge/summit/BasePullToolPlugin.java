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
import org.netbeans.jmiimpl.omg.uml.foundation.core.TaggedValueImpl;
import org.omg.uml.foundation.core.TaggedValue;

/**
 *
 * Creates the basic pull tool for each of the objects needed in a 
 * screen
 *
 * @author <a href="mailto:david@dwynter.plus.com">David Wynter</a>
 */
public class BasePullToolPlugin extends JavaPluginBase {
	
	private ClassView rootClassView = null;
	private SummitHelper helper =null;
	
	List dependentEntityClasses = new ArrayList();
	List dependentDisplayAttributes = new ArrayList();

    public BasePullToolPlugin(VelocityTemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        setStereotype("RootSelectable");
        // a cludge to get the thing to generate
        setCreateonly(true);
    }

    public Collection getMetadata() {
        return ((JMICapableMetadataProvider)metadataProvider).getJMIMetadata();
    }
    
    protected void populateContextMap(Map m) {
        super.populateContextMap(m);
        helper = new SummitHelper();
        Object metadata = null;
		try {
			metadata = m.get("metadata");
		} catch (Exception e) {
			// need to add logging to report model errors and incorrectness of model
			e.printStackTrace();
		}
		if(metadata!=null) {
    		rootClassView = helper.buildClassView((ClassifierImpl)metadata, null);
            m.put("rootClassView", rootClassView);
            m.put("qualifiedScreenName", 
            		((ClassifierImpl)metadata).getFullyQualifiedName()
            		+ ((TaggedValueImpl)((ModelElementImpl)metadata).getTaggedValue(SummitHelper.SCRN_NAME)).getValue());

    	}
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
        return pack + ".tools";
    }

    public String getDestinationFilename(Object metadata) {
        TaggedValue taggedValue = ((ModelElementImpl) metadata).getTaggedValue(SummitHelper.SCRN_NAME);
        if(taggedValue != null) {
        	Collection tags = taggedValue.getDataValue();
        	for (Iterator it = tags.iterator(); it.hasNext();) {
        		Object name = it.next();
                if (name instanceof String) {
                    return "Base" + name + "PullTool.java";
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
