package org.omg.uml.diagraminterchange;

/**
 * Property object instance interface.
 */
public interface Property extends javax.jmi.reflect.RefObject {
    /**
     * Returns the value of attribute key.
     * @return Value of attribute key.
     */
    public java.lang.String getKey();
    /**
     * Sets the value of key attribute. See {@link #getKey} for description on 
     * the attribute.
     * @param newValue New value to be set.
     */
    public void setKey(java.lang.String newValue);
    /**
     * Returns the value of attribute value.
     * @return Value of attribute value.
     */
    public java.lang.String getValue();
    /**
     * Sets the value of value attribute. See {@link #getValue} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setValue(java.lang.String newValue);
}
