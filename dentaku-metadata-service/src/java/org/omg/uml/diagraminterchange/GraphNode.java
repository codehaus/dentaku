package org.omg.uml.diagraminterchange;

/**
 * GraphNode object instance interface.
 */
public interface GraphNode extends org.omg.uml.diagraminterchange.GraphElement {
    /**
     * Returns the value of attribute size.
     * @return Value of attribute size.
     */
    public org.omg.uml.diagraminterchange.Dimension getSize();
    /**
     * Sets the value of size attribute. See {@link #getSize} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setSize(org.omg.uml.diagraminterchange.Dimension newValue);
}
