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
package org.dentaku.gentaku.tools.cgen.model;

import com.thoughtworks.xstream.alias.ClassMapper;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.lang.reflect.Field;

public abstract class ConverterBase implements Converter {
    protected Class convertingClass = null;
    protected ClassMapper classMapper;
    protected String classAttributeIdentifier;

    abstract protected void setValue(Object result, Object o);

    protected ConverterBase(Class convertingClass, ClassMapper classMapper, String classAttributeIdentifier) {
        this.convertingClass = convertingClass;
        this.classMapper = classMapper;
        this.classAttributeIdentifier = classAttributeIdentifier;
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
//        writer.setValue(toString(source));
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Object result = null;
        if (convertingClass == null) {
            throw new InstantiationError("convertingClass not set by subclass!");
        }
        try {
            result = convertingClass.newInstance();
        } catch (Exception e) {
            throw new InstantiationError("error instantiating " + convertingClass.getName());
        }
        Field fields[] = convertingClass.getFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                String name = field.getName();
                if (name.endsWith("_")) {
                    name = name.substring(0, name.length()-1);
                }
                field.set(result, reader.getAttribute(name));
            } catch (IllegalAccessException e) {
                // do nothing
            }
        }

        while (reader.hasMoreChildren()) {
            reader.moveDown();
            Object o = readItem(reader, context, null);
            setValue(result, o);
            reader.moveUp();
        }

        return result;
    }

    protected Object readItem(HierarchicalStreamReader reader, UnmarshallingContext context, Object current) {
        String classAttribute = reader.getAttribute(classAttributeIdentifier);
        Class type;
        if (classAttribute == null) {
            type = classMapper.lookupType(reader.getNodeName());
        } else {
            type = classMapper.lookupType(classAttribute);
        }
        return context.convertAnother(current, type);
    }

    public boolean canConvert(Class type) {
        return type.equals(convertingClass);
    }
}
