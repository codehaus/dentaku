package org.dentaku.gentaku.cartridge.java.impl;

import java.lang.reflect.Method;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
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


    public String metodos(Object o) {
        String x = "";

        Method[] ms = o.getClass().getMethods();

        for (int i = 0; i < ms.length; i++) {

            x += "\n" + ms[i].getName() + "         --------> " + ms[i];

        }

        return x;
    }
}