/*
 * CopyrightPlugin (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.metadata.dbmapping;

import com.thoughtworks.xstream.XStream;

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
            XStream xstream = new XStream();
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

    private class TypeMapping {
        private List typeNames;
        private String jdbcType;
        private String sqlPattern;
        private String sqlDefaultLength;
    }
}
