/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.gentaku.cartridge.struts;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.generama.TemplateEngine;
import org.generama.MetadataProvider;
import org.generama.WriterMapper;
import org.generama.VelocityTemplateEngine;

public class StrutsActionTestPlugin extends JavaPluginBase {
    public StrutsActionTestPlugin(VelocityTemplateEngine templateEngine, MetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, writerMapper);
        getStereotypes().add("StrutsAction");
        setCreateonly(true);
        setFileregex(".java");
        setFilereplace("Test.java");
    }
}