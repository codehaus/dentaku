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

public class RestrictionConverter extends ConverterBase {
    public RestrictionConverter(ClassMapper classMapper, String classAttributeIdentifier) {
        super(Restriction.class, classMapper, classAttributeIdentifier);
    }

    protected void setValue(Object result, Object o) {
        if (o instanceof Enumeration) {
            ((Restriction) result).getElements().add(o);
        } else {
            throw new RuntimeException(convertingClass.getName() + " does not yet support children of " + o.getClass().getName());
        }
    }
}
