package org.dentaku.gentaku.metacartridge.java;

import org.dentaku.gentaku.metacartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.omg.uml.foundation.core.Classifier;

public class BeanPlugin extends JavaPluginBase {

    private String callbacksPath = "org/dentaku/gentaku/metacartridge/java/defaultcallbacks";

    public BeanPlugin(VelocityTemplateEngine velocityTemplateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(new String[] { "Entity" }, velocityTemplateEngine, metadataProvider, writerMapper);
        setFileregex(".java");
        setFilereplace("Impl.java");
        setMultioutput(true);
    }

    public String getInterfaceFullname(Classifier obj) {
        return this.getMetadataProvider().getOriginalPackageName(obj) + "." + obj.getName();
    }
    
    public String getCallbacksPath() {
        return this.callbacksPath;
    }
    public void setCallbacksPath(String value) {
        this.callbacksPath = value;
    }

}