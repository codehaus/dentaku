package org.dentaku.gentaku.cartridge.qtags.impl;

import com.thoughtworks.qdox.model.JavaClass;
import org.dentaku.gentaku.GentakuTag;
import org.generama.QDoxCapableMetadataProvider;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;

/**
 * An empty subclass to get VelocityTemplateEngine to grab our configuration instead
 * @version $Revision$
 */
public class QTagLibraryPlugin extends org.xdoclet.plugin.qtags.impl.QTagLibraryPlugin {
    public QTagLibraryPlugin(VelocityTemplateEngine velocityTemplateEngine, QDoxCapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(velocityTemplateEngine, metadataProvider, writerMapper);
    }

    public boolean shouldGenerate(Object metadata) {
        JavaClass javaClass = (JavaClass) metadata;
        boolean isTagClass = javaClass.isA(GentakuTag.class.getName());

        boolean enabled = javaClass.getTagByName("qtags.ignore") == null;

        return javaClass.isInterface() && isTagClass && enabled;
    }
}
