package org.omg.uml.diagraminterchange;

/**
 * Property class proxy interface.
 */
public interface PropertyClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Property createProperty();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param key 
     * @param value 
     * @return The created instance object.
     */
    public Property createProperty(java.lang.String key, java.lang.String value);
}
