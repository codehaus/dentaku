/*
 * AttributeImpl.java
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
package org.netbeans.jmiimpl.omg.uml.foundation.core;

import java.util.Collection;
import java.util.Iterator;

import org.dentaku.services.metadata.dbmapping.DbMappingTable;
import org.netbeans.mdr.storagemodel.StorableObject;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;

abstract public class AttributeImpl extends ModelElementImpl implements org.dentaku.services.metadata.jmi.core.Attribute {
    DbMappingTable typeMappings = new DbMappingTable();

    public AttributeImpl(StorableObject storable) {
        super(storable);
    }

    public String getDatabaseColumnName() {
        String name = findTagValue("gentaku.persistence.SQLColumnName", true);
        if (name == null) {
            name = getName();
        }
        return name;
    }

    /**
     * Searches the given class feature (operation or attribute) for the
     * specified tag. <p/>
     * <p/>
     * If the follow boolean is set to true then the search will continue from
     * the class feature to the class itself and then up the class hiearchy.
     * </p>
     *
     * @param tagName name of the tag to search for
     * @param follow  <b>true </b> if search should follow inheritance hierarchy
     * @return String value of tag, <b>null </b> if tag not found
     */
    public String findTagValue(String tagName, boolean follow) {
        String value = findTagValue(tagName);
        for (ClassifierImpl element = (ClassifierImpl) getType(); value == null && element != null; element = (ClassifierImpl) element.getSuperclass()) {
            value = element.findTagValue(tagName);
        }

        return value;
    }

    /**
     * <p/>
     * Returns the JDBC type for an attribute. It gets the type from the tag
     * <code>andromda.persistence.JDBCType</code> for this.
     * </p>
     *
     * @return String the string to be used with JDBC
     * @return String the string to be used with JDBC
     */
    public String getAttributAneJDBCType() {
        String value = findTagValue("andromda.persistence.JDBCType");
        if (null == value) {
            value = ((ModelElementImpl) getType()).getFullyQualifiedName();
            if (typeMappings != null) {
                value = typeMappings.getJDBCType(value);
            }
        }
        return value;
    }

    /**
     * <p/>
     * Returns the SQL type for an attribute. Normally it gets the type from the
     * tag <code>andromda.persistence.SQLType</code>. If this tag doesn't
     * exist, it uses {@link #findAttributeSQLFieldLength(Attribute)
     * findAttributeSQLFieldLength()} and combines it's result with the standard
     * SQL type for the attributes type from the type mapping configuration
     * file.
     * </p>
     *
     * @return String the string to be used as SQL type
     */
    public String getAttributeSQLType() {
        String value = findTagValue("andromda.persistence.JDBCType");
        if (null == value) {
            value = ((ModelElementImpl) getType()).getFullyQualifiedName();
            if (typeMappings != null) {
                value = typeMappings.getSQLType(value, getAttributeSQLFieldLength());
            }
        }
        return value;
    }

    /**
     * <p/>
     * Returns the length for the SQL type of an attribute. It gets the length
     * from the tag <code>andromda.persistence.SQLFieldLength</code>. This
     * might return "50" for a VARCHAR field or "12,2" for a DECIMAL field.
     * </p>
     *
     * @param attribute the attribute
     * @return String the length of the underlying SQL field
     */
    public String getAttributeSQLFieldLength() {
        return findTagValue("andromda.persistence.SQLFieldLength", true);
    }

    public boolean isMany() {
        Multiplicity multiplicity = this.getMultiplicity();

        if (multiplicity == null) {
            return false; // no multiplicity means multiplicity==1
        }

        Collection ranges = multiplicity.getRange();
        for (Iterator i = ranges.iterator(); i.hasNext();) {
            MultiplicityRange range = (MultiplicityRange) i.next();
            if ((range.getUpper() > 1) || (range.getUpper() < 0)) {
                return true;
            }

            int rangeSize = range.getUpper() - range.getLower();
            if (rangeSize < 0) {
                return true;
            }
        }

        return false;
    }

    public boolean isOne() {
        return !this.isMany();
    }
}
