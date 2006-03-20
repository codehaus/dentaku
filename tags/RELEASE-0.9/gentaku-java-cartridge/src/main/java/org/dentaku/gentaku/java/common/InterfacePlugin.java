package org.dentaku.gentaku.java.common;

import org.dentaku.gentaku.java.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;

public class InterfacePlugin extends JavaPluginBase {

    public InterfacePlugin(VelocityTemplateEngine velocityTemplateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(new String[] { "Entity", "entity", "interface", "Interface" }, velocityTemplateEngine, metadataProvider, writerMapper);
        setMultioutput(true);
    }

}