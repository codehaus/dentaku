package org.dentaku.gentaku.cartridge;

public class JMIHelper {

    public boolean isPrimaryType(String name) {
        return name.indexOf('.') < 0;
    }

}