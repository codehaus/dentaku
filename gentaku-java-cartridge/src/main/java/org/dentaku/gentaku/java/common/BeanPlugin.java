package org.dentaku.gentaku.java.common;

import org.dentaku.gentaku.java.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.omg.uml.foundation.core.Classifier;

public class BeanPlugin extends JavaPluginBase {

    public BeanPlugin(VelocityTemplateEngine velocityTemplateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(new String[] { "Entity" }, velocityTemplateEngine, metadataProvider, writerMapper);
        setFileregex(".java");
        setFilereplace("Impl.java");
        setMultioutput(true);
    }

    public String getInterfaceFullname(Classifier obj) {
        return this.getMetadataProvider().getOriginalPackageName(obj) + "." + obj.getName();
    }

}