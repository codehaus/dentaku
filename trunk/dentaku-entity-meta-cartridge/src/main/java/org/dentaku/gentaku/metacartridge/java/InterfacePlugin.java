package org.dentaku.gentaku.metacartridge.java;

import org.dentaku.gentaku.metacartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;

public class InterfacePlugin extends JavaPluginBase {

    public InterfacePlugin(VelocityTemplateEngine velocityTemplateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(new String[] { "Entity", "entity", "interface", "Interface" }, velocityTemplateEngine, metadataProvider, writerMapper);
        setMultioutput(true);
    }

}