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
import org.dentaku.services.metadata.JMIUMLMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;

/**
 * @author <a href="mailto:david@dwynter.plus.com">David Wynter</a>
 *
 * Navigates the metamodel to instantiate ClassView and
 * AttributeView classes then used these generate the CRUDAction
 * source for each of the screens
 */
public class CrudActionPlugin extends JavaPluginBase {
    JMIUMLMetadataProvider metadataProvider;
	// Tag names
	private final static String POSITION = "position";
	private final static String LIST_ITEMS = "listItems";
	private final static String COLUMNS = "TRcolumns";
	private final static String SCRN_NAME = "screenName";
	private final static String LABEL = "label";
	private final static String SPAN = "span";
	
	private Collection metadata;
	private ClassView rootClassView = null;
	private SummitHelper helper =null;

    public CrudActionPlugin(VelocityTemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        setStereotype("Entity");
        setCreateonly(true);
    }

    public Collection getMetadata() {
        return metadataProvider.getJMIMetadata();
    }
    
    protected void populateContextMap(Map m) {
        super.populateContextMap(m);
        helper = new SummitHelper();
    	ClassifierImpl rootViewClass = null;
		try {
			rootViewClass = (ClassifierImpl)m.get("metadata");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rootViewClass!=null) {
    		rootClassView = helper.buildClassView(rootViewClass, null);
    	}
        m.put("rootClassView", rootClassView);
        m.put("SummitHelper", helper);
    }

    public boolean shouldGenerate(Object metadata) {
        Collection stereotypes = ((ModelElementImpl)metadata).getStereotypeNames();
        return stereotypes.contains("RootSelectable");
    }
}
