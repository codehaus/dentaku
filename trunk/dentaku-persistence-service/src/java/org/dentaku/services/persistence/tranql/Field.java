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
package org.dentaku.services.persistence.tranql;

import org.tranql.schema.Attribute;

public class Field implements Attribute {
    private String physicalName;
    private String logicalName;
    private Class type;
    private boolean identity;

    public Field(String physicalName, String logicalName, Class type, boolean identity) {
        this.physicalName = physicalName;
        this.logicalName = logicalName;
        this.type = type;
        this.identity = identity;
    }

    public String getName() {
        return logicalName;
    }

    public String getPhysicalName() {
        return physicalName;
    }

    public Class getType() {
        return type;
    }

    public boolean isIdentity() {
        return identity;
    }
}
