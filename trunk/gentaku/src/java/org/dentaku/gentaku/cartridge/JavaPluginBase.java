/*
 * JavaPluginBase.java
 * Copyright 2004-2004 Bill2, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dentaku.gentaku.cartridge;

import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.generama.Plugin;
import org.generama.TemplateEngine;
import org.generama.WriterMapper;
import org.generama.defaults.FileWriterMapper;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.omg.uml.foundation.core.TaggedValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class JavaPluginBase extends Plugin {
    private boolean createonly;
    protected String stereotype;
    protected List stereotypes;

    public JavaPluginBase(TemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, new CheckFileWriterMapper(writerMapper));
        setMultioutput(true);
        stereotypes = new LinkedList();
    }

    public Map getContextObjects() {
        Map result = super.getContextObjects();
        result.put("class", result.get("metadata"));
        return result;
    }

    public String getDestinationClassname(Object metadata) {
        String destinationFilename = getDestinationFilename(metadata);
        return destinationFilename.substring(0, destinationFilename.indexOf('.'));
    }

    public String getDestinationFullyQualifiedClassName(Object metadata) {
        String packageName = getDestinationPackage(metadata);
        packageName = packageName.equals("") ? "" : packageName + ".";
        return packageName + getDestinationClassname(metadata);
    }

    public boolean shouldGenerate(Object metadata) {
        String stereotypeName = null;
        boolean result = false;
        TaggedValue taggedValue = ((ModelElementImpl) metadata).getTaggedValue("gentaku.generate");
        if (!(taggedValue != null && taggedValue.getDataValue().contains("false"))) {
            if (stereotypes.size() == 0) {
                String className = getClass().getName();
                String pluginName = className.substring(className.lastIndexOf(".") + 1);
                stereotypeName = pluginName.substring(0, pluginName.indexOf("Plugin"));
                result = matchesStereotype((ModelElementImpl) metadata, stereotypeName);
            } else {
                for (Iterator it = stereotypes.iterator(); it.hasNext();) {
                    stereotypeName = (String) it.next();
                    if (matchesStereotype((ModelElementImpl) metadata, stereotypeName)) {
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    public static boolean matchesStereotype(ModelElementImpl object, String stereotype) {
        boolean b = false;
        if (object != null) {
            Collection names = object.getStereotypeNames();
            b = names.contains(stereotype);
        }
        return b;
    }

    public boolean isCreateonly() {
        return createonly;
    }

    public void setCreateonly(boolean createOnly) {
        this.createonly = createOnly;
        ((CheckFileWriterMapper) getWriterMapper()).setCreateonly(createOnly);
    }

    public List getStereotypes() {
        return stereotypes;
    }

    public void setStereotypes(List stereotypes) {
        this.stereotypes = stereotypes;
    }

    public void setStereotype(String stereotype) {
        this.stereotypes.add(stereotype);
    }

    private static class CheckFileWriterMapper implements WriterMapper {
        private WriterMapper delegate;
        private boolean createOnly;

        public CheckFileWriterMapper(WriterMapper delegate) {
            this.delegate = delegate;
        }

        public Writer getWriter(Object metadata, Plugin plugin) throws IOException {
            Writer result = null;
            if (this.delegate instanceof FileWriterMapper) {
                String pakkage = plugin.getDestinationPackage(metadata);
                String packagePath = pakkage.replace('.', '/');
                File dir = new File(plugin.getDestdirFile(), packagePath);
                dir.mkdirs();
                String filename = plugin.getDestinationFilename(metadata);
                File out = new File(dir, filename);
                if (!(createOnly && out.exists())) {
                    try {
                        result = new OutputStreamWriter(new FileOutputStream(out), plugin.getEncoding());
                    } catch (UnsupportedEncodingException e) {
                        throw new IOException(e.toString());
                    }
                }
            } else {
                result = delegate.getWriter(metadata, plugin);
            }
            return result;
        }

        public boolean isCreateonly() {
            return createOnly;
        }

        public void setCreateonly(boolean createOnly) {
            this.createOnly = createOnly;
        }
    }

    /**
     * <p>Converts a string following the Java naming conventions to a
     * database attribute name.  For example convert customerName to
     * CUSTOMER_NAME.</p>
     *
     * @param s         string to convert
     * @param separator character used to separate words
     * @return string converted to database attribute format
     */
    public static String toDatabaseAttributeName(String s, String separator) {
        StringBuffer databaseAttributeName = new StringBuffer();
        StringCharacterIterator iter = new StringCharacterIterator(lowerCaseFirstLetter(s));
        for (char character = iter.first(); character != CharacterIterator.DONE; character = iter.next()) {
            if (Character.isUpperCase(character)) {
                databaseAttributeName.append(separator);
            }
            character = Character.toUpperCase(character);
            databaseAttributeName.append(character);
        }
        return databaseAttributeName.toString();
    }

    public String fromDatabaseAttributeName(String s, String separator) {
        if (s == null) {
            return null;
        }
        String tok[] = s.split(separator);
        StringBuffer databaseAttributeName = new StringBuffer();
        databaseAttributeName.append(lowerCaseFirstLetter(tok[0]));
        for (int i = 1; i < tok.length; i++) {
            databaseAttributeName.append(upperCaseFirstLetter(tok[i]));
        }
        return databaseAttributeName.toString();
    }

    /**
     * <p>Removes the capitalization of a string. That is, it returns
     * "hamburgerStall" when receiving a "HamburgerStall".</p>
     *
     * @param s the input string
     * @return String the output string.
     */
    public static String lowerCaseFirstLetter(String s) {
        if (s != null && s.length() > 0) {
            return s.substring(0, 1).toLowerCase() + s.substring(1);
        } else {
            return s;
        }
    }

    public String upperCaseFirstLetter(String s) {
        if (s != null && s.length() > 0) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        } else {
            return s;
        }
    }
}