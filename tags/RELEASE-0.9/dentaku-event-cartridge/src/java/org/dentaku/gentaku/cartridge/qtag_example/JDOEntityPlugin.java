/**
 *
 *  Copyright 2004 Brian Topping
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dentaku.gentaku.cartridge.qtag_example;

import org.dentaku.gentaku.cartridge.qtag_example.qtags.TagLibrary;
import org.dentaku.services.metadata.QDoxMetadataProvider;
import org.generama.JellyTemplateEngine;
import org.generama.QDoxCapableMetadataProvider;
import org.generama.WriterMapper;
import org.generama.defaults.QDoxPlugin;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.BeanProperty;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.AbstractJavaEntity;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Arrays;

public class JDOEntityPlugin extends QDoxPlugin {
    QDoxCapableMetadataProvider metadataProvider;

    public JDOEntityPlugin(JellyTemplateEngine jellyTemplateEngine, QDoxMetadataProvider mp, WriterMapper writerMapper) {
        super(jellyTemplateEngine, mp, writerMapper);
        metadataProvider = mp;
//        getStereotypes().add("Entity");
        setFileregex(".java");
        setFilereplace("Base.java");
        setMultioutput(false);
        new TagLibrary(metadataProvider);
    }

    public boolean shouldGenerate(Object metadata) {
        JavaClass clazz = (JavaClass) metadata;

        return clazz.getTagByName("jdo.class") != null;
    }

    ///////////////// HELPER METHODS /////////////////////////////////////
    /**
     * provide list of hibernate properties for class. it could be getter, as well
     * as field ( for direct property access )
     */
    public List getClassProperties(JavaClass clazz) {
        ArrayList retval = new ArrayList();

        getPropertiesRecursive(clazz, null, retval);

        return retval;
    }

    /**
     * recursive property retrival stopping at stop tag
     */
    void getPropertiesRecursive(JavaClass clazz, Collection stopTags, List accumulate) {
        getProperties(clazz, accumulate);

        JavaClass superclass = clazz.getSuperJavaClass();

        if (superclass != null) {
            // stop recursion?
            if (stopTags != null && !getTags(superclass, stopTags).isEmpty()) {
                // yep.
                return;
            }

            getPropertiesRecursive(superclass, stopTags, accumulate);
        }
    }

    /**
     * gather hibernate propertis from given class into list
     */
    public void getProperties(JavaClass clazz, List accumulate) {
        int i;

        // walk through  property getters
        BeanProperty[] beanProperties = clazz.getBeanProperties();
        JDOProperty property;

        for (i = 0; i < beanProperties.length; i++) {
            // property is ours, if we have at least one of designated property tags
            // and there is accessor
            if (beanProperties[i] != null && beanProperties[i].getAccessor() != null) {

                property = new JDOProperty();
                property.setName(beanProperties[i].getName());
                property.setEntity(beanProperties[i].getAccessor());

                if (!accumulate.contains(property)) {
                    accumulate.add(property);
                }
            }
        }

        JavaField[] fields = clazz.getFields();

        for (i = 0; i < fields.length; i++) {
            if (!getTags(fields[i], getPropertyTagList()).isEmpty()) {
                property = new JDOProperty();
                property.setName(fields[i].getName());
                property.setEntity(fields[i]);
                property.setAccess("field");

                if (!accumulate.contains(property)) {
                    accumulate.add(property);
                }
            }
        }
    }

    // list holding tag names defining property existence
    private final static List PROPERTY_TAGS = new ArrayList();
    // list holding tag names causing stop of recursive search
    // for properties
    private final static List HIERARCHY_STOP_TAGS = new ArrayList();
    /**
     * provide list of valid property tags
     */
    public List getPropertyTagList() {
        return PROPERTY_TAGS;

    }

    static {
        PROPERTY_TAGS.add("hibernate.property");
        PROPERTY_TAGS.add("hibernate.many-to-one");
        PROPERTY_TAGS.add("hibernate.one-to-one");
        PROPERTY_TAGS.add("hibernate.component");
        PROPERTY_TAGS.add("hibernate.dynamic-component");
        PROPERTY_TAGS.add("hibernate.any");
        PROPERTY_TAGS.add("hibernate.map");
        PROPERTY_TAGS.add("hibernate.set");
        PROPERTY_TAGS.add("hibernate.list");
        PROPERTY_TAGS.add("hibernate.bag");
        PROPERTY_TAGS.add("hibernate.idbag");
        PROPERTY_TAGS.add("hibernate.array");
        PROPERTY_TAGS.add("hibernate.primitive");
        PROPERTY_TAGS.add("hibernate.key-property");

        HIERARCHY_STOP_TAGS.add("hibernate.class");
        HIERARCHY_STOP_TAGS.add("hibernate.subclass");
        HIERARCHY_STOP_TAGS.add("hibernate.joined-subclass");

    }


    /**
     * provide combined list of all the tags with given tag names
     */
    public List getTags(AbstractJavaEntity metadata, Collection tagNames) {
        ArrayList al = new ArrayList();

        for (Iterator iter = tagNames.iterator(); iter.hasNext();) {
            al.addAll(Arrays.asList(metadata.getTagsByName((String) iter.next())));
        }

        return al;
    }


    public class JDOProperty {
        String name;
        String access;
        AbstractJavaEntity entity;

        /**
         * name of hibernate property
         */
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * access specification. shall be field for fields
         */
        public String getAccess() {
            return access;
        }

        public void setAccess(String access) {
            this.access = access;
        }

        public AbstractJavaEntity getEntity() {
            return entity;
        }

        public void setEntity(AbstractJavaEntity entity) {
            this.entity = entity;
        }

        /**
         * puprose of this equality method is to allow proprety overriding
         * properties are equal, if they have the same name, regardless of access.
         * Since we build property map from top to bottom of class hierarchy, we will reflect
         * java behaviour
         */
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            if (!(o instanceof JDOProperty)) {
                return false;
            }

            if (getName() != null) {
                return getName().equals(((JDOProperty) o).getName());
            }

            return false;
        }

        public int hashCode() {
            if (getName() != null) {
                return getName().hashCode();
            }
            return super.hashCode();
        }

        public String toString() {
            return "[" + this.name + "][" + this.access + "]";
        }
    }


    ///////////////// HELPER METHODS /////////////////////////////////////
}
