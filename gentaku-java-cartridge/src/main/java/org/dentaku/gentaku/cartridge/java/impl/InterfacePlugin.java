package org.dentaku.gentaku.cartridge.java.impl;

import java.lang.reflect.Method;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;

public class InterfacePlugin extends JavaPluginBase {

    public InterfacePlugin(VelocityTemplateEngine velocityTemplateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(new String[] { "Entity" }, velocityTemplateEngine, metadataProvider, writerMapper);
        setMultioutput(true);
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