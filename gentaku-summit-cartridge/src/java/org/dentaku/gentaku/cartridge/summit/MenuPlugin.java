/*
 * Created on Feb 25, 2005
 *
 * Copyright STPenable Ltd. (c) 2004
 * 
 */
package org.dentaku.gentaku.cartridge.summit;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.omg.uml.foundation.core.TaggedValue;

/**
 * @author David Wynter
 *
 * Add comment here
 */
public class MenuPlugin extends JavaPluginBase {

    public MenuPlugin(VelocityTemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        ArrayList stereos = new ArrayList();
        setStereotype("RootSelectable");
     
        this.metadataProvider = metadataProvider;
        setCreateonly(false);
        setPackageregex("[^>]+");
        setPackagereplace("");
        setMultioutput(true);
    }

    protected void populateContextMap(Map m) {
        super.populateContextMap(m);
    	Collection metadata = getMetadata();
    	ArrayList screennames = new ArrayList();
    	String name = new String();
    	for(Iterator it = metadata.iterator(); it.hasNext();) {
    		TaggedValue tv = ((ModelElementImpl)it.next()).getTaggedValue(SummitHelper.SCRN_NAME);
    		if(tv != null) {
    			Collection names = tv.getDataValue();
	    		for(Iterator t = names.iterator(); t.hasNext();) {
	    			name = (String)t.next();
	        		if(name.length()>0)
	        			screennames.add(name);
	        		name="";
	    		}
    		}
    	}
        m.put("screennames", screennames);
    }
	/* (non-Javadoc)
	 * @see org.generama.Plugin#getMetadata()
	 */
	protected Collection getMetadata() {
        return ((JMICapableMetadataProvider)metadataProvider).getJMIMetadata();
	}
    public boolean shouldGenerate(Object metadata) {
        Collection stereotypes = ((ModelElementImpl)metadata).getStereotypeNames();
        return stereotypes.contains(SummitHelper.APP_NAME);
    }
    public String getDestinationFilename(Object metadata) {
        return "menu.vm";
    }
    public String pretty(String name) {
        StringBuffer prettyName = new StringBuffer();
        StringCharacterIterator iter = new StringCharacterIterator(name);
        boolean firstchar = true;
        for (char character = iter.first(); character != CharacterIterator.DONE; character = iter.next()) {
            if (Character.isUpperCase(character) && !firstchar) {
                prettyName.insert(prettyName.length(), ' ');
            }
            firstchar=false;
            prettyName.append(character);
        }
        return prettyName.toString();
    }
}
