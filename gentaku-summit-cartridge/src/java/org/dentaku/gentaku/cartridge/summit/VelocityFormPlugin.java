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
import java.util.List;
import java.util.Map;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;

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
        setStereotype("Entity");
        setStereotype("RootSelectable");
        setStereotype("ChildSelectable"); 
        setStereotype("ChildTable");
     
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
        Collection stereotypes = ((ModelElementImpl)metadata).getStereotypeNames();
        return stereotypes.contains("RootSelectable");
    }
}