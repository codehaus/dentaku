/*
 * Created on Dec 24, 2004
 *
 * Copyright STPenable Ltd. (c) 2004
 * 
 */
package org.dentaku.gentaku.cartridge.summit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.dentaku.services.metadata.JMIUMLMetadataProvider;
import org.generama.JellyTemplateEngine;
import org.generama.Plugin;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.TaggedValueImpl;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;

/**
 * Simple plugin to get all screennames from 
 * stereotypes <<RootSelectable>>
 * to generate the PullTool configuration file
 * which is included in the main Plexus config file
 * using the standard name SummitPull.xml
 * 
 * @author <a href="mailto:david@dwynter.plus.com">David Wynter</a>
 */
public class PullConfigPlugin extends Plugin {
    JMIUMLMetadataProvider metadataProvider;

    public PullConfigPlugin(JellyTemplateEngine jellyTemplateEngine, JMIUMLMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(jellyTemplateEngine, metadataProvider, writerMapper);

        setMultioutput(false);
        setEncoding("UTF-8");
        this.metadataProvider = metadataProvider;
    }

    public boolean shouldGenerate(Object metadata) {
        Collection stereotypes = ((ModelElementImpl)metadata).getStereotypeNames();
        return stereotypes.contains(SummitHelper.APP_NAME);
    }

    public String getDestinationFilename(Collection metadata) {
		String appName = new String();
		Collection stereotypes = ((ModelElementImpl)metadata).getStereotypeNames();
        for (Iterator it = stereotypes.iterator(); it.hasNext();) {
        	ModelElement elem = (ModelElement) it.next();
            if (elem.getName().equals(SummitHelper.APP_NAME)) {
            	appName= elem.getName();
            }
        }
        return appName+".xml";
    }

    public Collection getScreenNames(Collection metadata) {
		Collection screenNames = new ArrayList();
        for (Iterator it = metadata.iterator(); it.hasNext();) {
        	ModelElement elem = (ModelElement) it.next();
            if (elem instanceof TaggedValue && elem.getName().equals(SummitHelper.SCRN_NAME)) {
            	screenNames.add(((TaggedValueImpl)elem).getValue());
            }
        }
        return screenNames;
    }

    protected Collection getMetadata() {
        return metadataProvider.getJMIMetadata();
    }
}
