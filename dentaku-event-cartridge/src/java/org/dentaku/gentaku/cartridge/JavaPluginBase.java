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

import org.generama.MetadataProvider;
import org.generama.Plugin;
import org.generama.TemplateEngine;
import org.generama.WriterMapper;
import org.generama.defaults.FileWriterMapper;
import org.generama.defaults.JavaGeneratingPlugin;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class JavaPluginBase extends JavaGeneratingPlugin {
    private boolean createonly;

    public Map getContextObjects() {
        Map result = super.getContextObjects();
        result.put("class", result.get("metadata"));
        return result;
    }

    protected List stereotypes;

    public JavaPluginBase(TemplateEngine templateEngine, MetadataProvider metadataProvider, WriterMapper writerMapper) {
        super(templateEngine, metadataProvider, new CheckFileWriterMapper(writerMapper));
        setMultioutput(true);
        stereotypes = new ArrayList();
    }

    public boolean shouldGenerate(Object metadata) {
        String stereotypeName = null;
        if (stereotypes.size() == 0) {
            String className = getClass().getName();
            String pluginName = className.substring(className.lastIndexOf(".") + 1);
            stereotypeName = pluginName.substring(0, pluginName.indexOf("Plugin"));
            return matchesStereotype((ModelElementImpl)metadata, stereotypeName);
        } else {
            for (Iterator it = stereotypes.iterator(); it.hasNext();) {
                stereotypeName = (String) it.next();
                if (matchesStereotype((ModelElementImpl)metadata, stereotypeName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean matchesStereotype(ModelElementImpl object, String stereotype) {
        boolean b = false;
        if (object != null) {
            Collection names = object.getStereotypeNames();
            b = names.contains(stereotype);
        }
        return b;
    }

//    protected void putMetadata(Map m, Object meta) {
//        if (meta instanceof Classifier) {
//            m.put("metadata", PClassifier.newInstance((Classifier) meta, model));
//        } else {
//            super.putMetadata(m, meta);
//        }
//    }

//    public URL getResourceURLRelativeToThisPackage(Class clazz, String resourceName) {
//        String className = clazz.getName();
//        String packageName = className.substring(0, className.lastIndexOf('.'));
//        String resourcePath = "/" + packageName.replace('.', '/') + "/" + resourceName;
//        URL resource = clazz.getResource(resourcePath);
//        Assert.assertNotNull("Resource not found at path: " + resourcePath, resource);
//        return resource;
//    }
//
//    protected Map loadConfiguration() throws RuntimeException {
//        // let's configure ourselves
//        byte buf[] = new byte[4096];
//        int ptr = 0, read = 0;
//        String input = "";
//        String classname = getClass().getName();
//        String filename = classname.substring(classname.lastIndexOf(".") + 1) + ".xml";
//        InputStream in = getClass().getResourceAsStream(filename);
//        try {
//            do {
//                ptr += read;
//                read = in.read(buf, ptr, 4096);
//                input += new String(buf).substring(0, read);
//            } while (read == 4096);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        XStream xstream = new XStream();
//        xstream.alias("template", Template.class);
//        return (Map) xstream.fromXML(input);
//    }
//
//    public static class Template {
//        String template;
//        boolean createonly;
//    }

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