package org.dentaku.gentaku.cartridge.java.impl;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.omg.uml.foundation.core.Classifier;

public class BeanPlugin extends JavaPluginBase {

    private String callbackMacroVM = "org/dentaku/gentaku/cartridge/java/impl/nocallbacks.vm";
  
    public BeanPlugin(VelocityTemplateEngine velocityTemplateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(new String[] { "Entity" }, velocityTemplateEngine, metadataProvider, writerMapper);
        setFileregex(".java");
        setFilereplace("Impl.java");
        setMultioutput(true);
    }

    public String getInterfaceFullname(Classifier obj) {
        return this.getMetadataProvider().getOriginalPackageName(obj) + "." + obj.getName();
    }
    
    public String getCallbackMacroVM() {
        return this.callbackMacroVM;
    }
    public void setCallbackMacroVM(String value) {
        this.callbackMacroVM = value;
    }

}