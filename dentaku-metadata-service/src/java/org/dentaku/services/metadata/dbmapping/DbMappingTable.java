/*
 * DbMappingTable.java
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
package org.dentaku.services.metadata.dbmapping;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.objecttree.reflection.JavaReflectionObjectFactory;
import com.thoughtworks.xstream.alias.DefaultNameMapper;
import com.thoughtworks.xstream.alias.DefaultClassMapper;
import com.thoughtworks.xstream.xml.xpp3.Xpp3DomXMLReaderDriver;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DbMappingTable {
    private static HashMap mappings;

    public String getJDBCType(String javaType) {
        TypeMapping mapping = (TypeMapping)getConfigurationMapping().get(javaType);
        if (mapping != null) {
            return mapping.jdbcType;
        } else {
            return "missing jdbc mapping for " + javaType;
        }
    }

    public String getSQLType(String javaType, String desiredFieldLength) {
        TypeMapping mapping = (TypeMapping)getConfigurationMapping().get(javaType);
        if (mapping != null) {
            if (desiredFieldLength != null && desiredFieldLength.length() > 0) {
                return MessageFormat.format(mapping.sqlPattern, new Object[] {desiredFieldLength});
            } else {
                return MessageFormat.format(mapping.sqlPattern, new Object[] {mapping.sqlDefaultLength});
            }
        } else {
            return "missing jdbc mapping for " + javaType;
        }
    }

    public Map getConfigurationMapping() throws RuntimeException {
        if (mappings == null) {
            // let's configure ourselves
            byte buf[] = new byte[4096];
            int ptr = 0, read = 0;
            String input = "";
            InputStream in = getClass().getResourceAsStream("DbMappingTable.xml");
            try {
                do {
                    ptr += read;
                    read = in.read(buf, ptr, 4096);
                    input += new String(buf).substring(0, read);
                } while (read == 4096);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            XStream xstream =  new XStream( new JavaReflectionObjectFactory(), new DefaultClassMapper(new DefaultNameMapper() ), new Xpp3DomXMLReaderDriver());

            xstream.alias("mapping", TypeMapping.class);
            List configuration = (List) xstream.fromXML(input);

            // now cross reference it
            mappings = new HashMap();
            for (Iterator it = configuration.iterator(); it.hasNext();) {
                TypeMapping typeMapping = (TypeMapping) it.next();
                for (Iterator types = typeMapping.typeNames.iterator(); types.hasNext();) {
                    String s = (String) types.next();
                    mappings.put(s, typeMapping);
                }
            }
        }
        return mappings;
    }

    public static class TypeMapping {
        public List typeNames;
        public String jdbcType;
        public String sqlPattern;
        public String sqlDefaultLength;
    }
}
