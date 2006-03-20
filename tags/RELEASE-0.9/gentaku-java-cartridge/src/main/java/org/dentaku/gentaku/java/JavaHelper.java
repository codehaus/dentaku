package org.dentaku.gentaku.java;


public class JavaHelper {

    public String parseType(String type) {

        if ("char".equals(type)) {
            return "Character";
        } else if ("boolean".equals(type)) {
            return "Boolean";
        } else if ("byte".equals(type)) {
            return "Byte";
        } else if ("double".equals(type)) {
            return "Double";
        } else if ("float".equals(type)) {
            return "Float";
        } else if ("int".equals(type)) {
            return "Integer";
        } else if ("long".equals(type)) {
            return "Long";
        } else if ("short".equals(type)) {
            return "Short";
        }

        return type;
    }


}
