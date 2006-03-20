package org.dentaku.gentaku.cartridge.qtags.impl;

import org.generama.MetadataProvider;
import org.generama.Plugin;
import org.generama.QDoxCapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;
import org.xdoclet.AbstractJavaGeneratingPluginTestCase;
import org.dentaku.services.metadata.QDoxMetadataProvider;

import java.io.IOException;
import java.net.URL;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class QTagImplPluginTestCase extends AbstractJavaGeneratingPluginTestCase {
    protected Plugin createPlugin(MetadataProvider metadataProvider,
                                          WriterMapper writerMapper) throws Exception {
        return new QTagImplPlugin(new VelocityTemplateEngine(), (QDoxMetadataProvider)metadataProvider, writerMapper);
    }

    protected URL getTestSource() throws IOException {
        return getResourceRelativeToThisPackage("test/FooBarTag.java");
    }

    protected URL getExpected() throws IOException {
        return getResourceRelativeToThisPackage("test/FooBarTagImpl.java");
    }
}
