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

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.alias.ClassMapper;

import java.util.List;
import java.util.ArrayList;

public class SchemaConverter extends ConverterBase {
    public SchemaConverter(ClassMapper mapper, String classAttributeIdentifier) {
        super(Schema.class, mapper, classAttributeIdentifier);
    }

    public boolean canConvert(Class type) {
        return type.equals(Schema.class);
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Schema result = (Schema)super.unmarshal(reader, context);
        List elements = new ArrayList();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            Object item = readItem(reader, context, null); // TODO: arg, what should replace null?
            elements.add(item);
            reader.moveUp();
        }
        result.setElements(elements);
        return result;
    }
}
