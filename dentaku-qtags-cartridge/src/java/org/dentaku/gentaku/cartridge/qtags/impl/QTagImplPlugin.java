package org.dentaku.gentaku.cartridge.qtags.impl;

import org.dentaku.services.metadata.QDoxMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.generama.QDoxCapableMetadataProvider;
import org.generama.defaults.JavaGeneratingPlugin;

import java.util.Collection;

/**
 * An empty subclass to get VelocityTemplateEngine to grab our configuration instead
 * @version $Revision$
 */
public class QTagImplPlugin extends org.xdoclet.plugin.qtags.impl.QTagImplPlugin {
    public QTagImplPlugin(VelocityTemplateEngine velocityTemplateEngine, QDoxCapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(velocityTemplateEngine, metadataProvider, writerMapper);
    }
}
