/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.gentaku.cartridge.metadata;

import com.thoughtworks.xstream.XStream;
import org.dentaku.services.metadata.JMIUMLMetadataProvider;
import org.generama.Plugin;
import org.generama.WriterMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MetadataSerializer extends Plugin {
    JMIUMLMetadataProvider mp = null;
    private File destinationfilename;

    public MetadataSerializer(JMIUMLMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(null, metadataProvider, writerMapper);
        mp = metadataProvider;
    }

    public File getDestinationfilename() {
        return destinationfilename;
    }

    public void setDestinationfilename(File destinationfilename) {
        this.destinationfilename = destinationfilename;
    }

    public void start() {
        destinationfilename.getParentFile().mkdirs();
        OutputStreamWriter os = null;
        XStream xs = new XStream();
        try {
            os = new OutputStreamWriter(new FileOutputStream(destinationfilename), getEncoding());
            os.write(xs.toXML(mp.getModel()));
            os.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't create output file", e);
        }
    }
}
