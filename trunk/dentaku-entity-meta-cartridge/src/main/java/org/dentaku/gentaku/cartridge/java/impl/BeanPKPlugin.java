package org.dentaku.gentaku.cartridge.java.impl;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;

public class BeanPKPlugin extends JavaPluginBase {

    private String callbackMacroVM = "org/dentaku/gentaku/cartridge/java/impl/nocallbacks.vm";
    
    public BeanPKPlugin(VelocityTemplateEngine velocityTemplateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(new String[] { "Entity" }, velocityTemplateEngine, metadataProvider, writerMapper);
        setFileregex(".java");
        setFilereplace("ImplPK.java");
        setMultioutput(true);
    }

    public boolean shouldGenerate(Object metadata) {
        if (!super.shouldGenerate(metadata)) {
            return false;
        }

        if (!(metadata instanceof ClassifierImpl)) {
            return false;
        }

        ClassifierImpl c = (ClassifierImpl) metadata;

        return this.jmiHelper.countPrimaryKey(c) > 1;
    }

    public String getCallbackMacroVM() {
        return this.callbackMacroVM;
    }
    public void setCallbackMacroVM(String value) {
        this.callbackMacroVM = value;
    }
}