/*
 *  Module.java
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
package org.dentaku.gentaku.tools.cgen.xmi;

import org.apache.tools.ant.DynamicConfigurator;
import org.apache.tools.ant.BuildException;

import java.util.HashMap;
import java.util.Map;

public class Module implements DynamicConfigurator {
    private Map properties = new HashMap();
    private String schema;
    private String mapping;

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setDynamicAttribute(String name, String value) throws BuildException {
        properties.put(name, value);
    }

    public Object createDynamicElement(String string) throws BuildException {
        throw new BuildException("No sub elements of " + string + " is allowed");
    }

    public Map getProperties() {
        return properties;
    }
}
