/*
 * Created on Dec 24, 2004
 *
 * Copyright STPenable Ltd. (c) 2004
 * 
 */
package org.dentaku.gentaku.cartridge.summit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.dentaku.services.metadata.QDoxMetadataProvider;
import org.generama.JellyTemplateEngine;
import org.generama.Plugin;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.TaggedValueImpl;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;

/**
 * Simple plugin to get all screennames from 
 * stereotypes <<RootSelectable>>
 * to generate the PullTool configuration file
 * which is included in the main Plexus config file
 * 
 * @author <a href="mailto:david@dwynter.plus.com">David Wynter</a>
 */
public class ComponentConfigPlugin extends Plugin {
	QDoxMetadataProvider metadataProvider;
	HashMap preActions;
	HashMap postActions;
	boolean actionsFound = false;

    public ComponentConfigPlugin(JellyTemplateEngine jellyTemplateEngine, QDoxMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(jellyTemplateEngine, metadataProvider, writerMapper);

        setPackageregex("[^>]+");
        setPackagereplace("META-INF/plexus");
        setMultioutput(true);
        setEncoding("UTF-8");
        this.metadataProvider = metadataProvider;
    }

    public boolean shouldGenerate(Object metadata) {
        Collection stereotypes = ((ModelElementImpl)metadata).getStereotypeNames();
        return stereotypes.contains(SummitHelper.APP_NAME);
    }

    public String getDestinationFilename(Object metadata) {
        return "components.xml";
    }
    
    public String getAppName() {
		String appName = new String();
		boolean done = false;
    	Collection metadata = getMetadata();
    	for (Iterator t = metadata.iterator(); t.hasNext();) {
    		ModelElement melem = (ModelElement) t.next();
            if (melem instanceof ClassifierImpl) {
        		Collection stereotypes = ((ModelElementImpl)melem).getStereotypeNames();
                for (Iterator it = stereotypes.iterator(); it.hasNext();) {
                	String elem = (String) it.next();
                    if (elem.equals(SummitHelper.APP_NAME)) {
                    	appName= elem;
                    	done = true;
                    	break;
                    }
                }
            } 
            if(done)
            	break;
    	}
		return appName;
    }

    public Collection getScreenNames() {
    	Collection metadata = getMetadata();
		Collection screenNames = new ArrayList();
        for (Iterator it = metadata.iterator(); it.hasNext();) {
        	ModelElement elem = (ModelElement) it.next();
            if (elem instanceof TaggedValue && elem.getName().equals(SummitHelper.SCRN_NAME)) {
            	screenNames.add(((TaggedValueImpl)elem).getValue());
            }
        }
        return screenNames;
    }

    public Collection getGroovyNames() {
    	Collection metadata = getMetadata();
		Collection groovyNames = new ArrayList();
        for (Iterator it = metadata.iterator(); it.hasNext();) {
        	ModelElement elem = (ModelElement) it.next();
            if (elem instanceof TaggedValue && elem.getName().equals(SummitHelper.GROOVY)) {
            	groovyNames.add(((TaggedValueImpl)elem).getValue());
            }
        }
        return groovyNames;
    }
    
    public String getPreAction(String screenname) {
    	if(!actionsFound) {
    		loadActionMaps();
    	} 
    	return (String)preActions.get(screenname);
    }
    public String getPostAction(String screenname) {
    	if(!actionsFound) {
    		loadActionMaps();
    	} 
    	return (String)postActions.get(screenname);
    }
    public boolean isPreAction(String screenname) {
    	if(!actionsFound) {
    		loadActionMaps();
    	} 
    	if(preActions != null && (String)preActions.get(screenname)!=null)
    		return true;
    	return false;
    }
    public boolean isPostAction(String screenname) {
    	if(!actionsFound) {
    		loadActionMaps();
    	} 
    	if (postActions != null && (String)postActions.get(screenname)!=null)
    		return true;
    	return false;
    }
    
    private void loadActionMaps() {
    	// find ClassifierImpl with this sceenname as TaggedValue
    	ClassifierImpl aScreenClass = null;
    	Collection metadata = getMetadata();
        for (Iterator it = metadata.iterator(); it.hasNext();) {
        	ModelElement elem = (ModelElement) it.next();
            if (elem instanceof TaggedValue && elem.getName().equals(SummitHelper.SCRN_NAME)) {
           		aScreenClass = (ClassifierImpl)((TaggedValueImpl)elem).getModelElement();
           		addScreen(aScreenClass);
           		aScreenClass = null;
            }
        }
        actionsFound=true;
    }

    /**
	 * @param screenClass
	 */
	private void addScreen(ClassifierImpl screenClass) {
    	// walk the Assoc with the View stereotype to ClassifierImpl
        if(screenClass != null) {
        	if(doViewToGroovy(screenClass, screenClass))
        		return;
        	Collection metadata = getMetadata();
        	ClassifierImpl aEntity = null;
        	// if none found then get all with ChildSelectable stereotype on ClassifierImpl
        	for(Iterator it = metadata.iterator(); it.hasNext(); ) {
            	ModelElement elem = (ModelElement) it.next();
            	if(elem instanceof ClassifierImpl && ((ClassifierImpl)elem).getStereotypeNames().contains(SummitHelper.CHILD_SELECTOR_STEREOTYPE)) {
               		aEntity = (ClassifierImpl)elem;
                }
        		if(aEntity!= null && doViewToGroovy(aEntity,  screenClass)) {
        			return;
        		} 
			}
    		return;
        }
	}
	
	private boolean doViewToGroovy(ClassifierImpl aEntity, ClassifierImpl screenClass) {		
    	// walk the Assoc with the View stereotype to ClassifierImpl
		Collection mentities = aEntity.getAllAssocClassOfType("View", "Entity");
		for(Iterator m = mentities.iterator(); m.hasNext();) {
    		// walk the Assoc with the Imports stereotype to ClassifierImpl
			ClassifierImpl anOtherEntity = (ClassifierImpl)m.next();
    		Collection groovies = anOtherEntity.getAllAssocClassOfType("Imports", "Groovy");
        	for(Iterator i = groovies.iterator(); i.hasNext(); ) {
        		ClassifierImpl aGroovy = (ClassifierImpl)i.next();
            	// get the Groovy name and state
        		String name = aGroovy.getName();
        		String state = aGroovy.findTagValue("state");
        		if(state.equals("preAction")) {
        			if(preActions==null) {
        				preActions = new HashMap();
        			}
        			preActions.put(screenClass.findTagValue("ScreenName"), name);
        			return true;
        		} else if(postActions==null) {
        			if(postActions == null) {
        				postActions = new HashMap();
        			}
        			postActions.put(screenClass.findTagValue("ScreenName"), name);
        			return true;
    			}
        	}
		}
		return false;
	}

	protected Collection getMetadata() {
        return metadataProvider.getJMIMetadata();
    }
}
