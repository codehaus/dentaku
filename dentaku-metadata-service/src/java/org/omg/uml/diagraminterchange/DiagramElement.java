package org.omg.uml.diagraminterchange;

/**
 * DiagramElement object instance interface.
 */
public interface DiagramElement extends javax.jmi.reflect.RefObject {
    /**
     * Returns the value of attribute isVisible.
     * @return Value of attribute isVisible.
     */
    public boolean isVisible();
    /**
     * Sets the value of isVisible attribute. See {@link #isVisible} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setVisible(boolean newValue);
    /**
     * Returns the value of reference property.
     * @return Value of reference property.
     */
    public java.util.Collection getProperty();
    /**
     * Returns the value of reference reference.
     * @return Value of reference reference.
     */
    public java.util.Collection getReference();
    /**
     * Returns the value of reference container.
     * @return Value of reference container.
     */
    public org.omg.uml.diagraminterchange.GraphElement getContainer();
    /**
     * Sets the value of reference container. See {@link #getContainer} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setContainer(org.omg.uml.diagraminterchange.GraphElement newValue);
}
