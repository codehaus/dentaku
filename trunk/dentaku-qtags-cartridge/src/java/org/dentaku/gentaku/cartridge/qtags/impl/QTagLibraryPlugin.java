package org.dentaku.gentaku.cartridge.qtags.impl;

import org.dentaku.services.metadata.QDoxMetadataProvider;
import org.generama.Plugin;
import org.generama.QDoxCapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;

import java.util.Collection;

/**
 * An empty subclass to get VelocityTemplateEngine to grab our configuration instead
 * @version $Revision$
 */
public class QTagLibraryPlugin extends org.xdoclet.plugin.qtags.impl.QTagLibraryPlugin {
    public QTagLibraryPlugin(VelocityTemplateEngine velocityTemplateEngine, QDoxCapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(velocityTemplateEngine, metadataProvider, writerMapper);
    }
}
