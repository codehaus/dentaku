package org.omg.uml.diagraminterchange;

/**
 * Diagram object instance interface.
 */
public interface Diagram extends org.omg.uml.diagraminterchange.GraphNode {
    /**
     * Returns the value of attribute name.
     * @return Value of attribute name.
     */
    public java.lang.String getName();
    /**
     * Sets the value of name attribute. See {@link #getName} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setName(java.lang.String newValue);
    /**
     * Returns the value of attribute zoom.
     * @return Value of attribute zoom.
     */
    public double getZoom();
    /**
     * Sets the value of zoom attribute. See {@link #getZoom} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setZoom(double newValue);
    /**
     * Returns the value of attribute viewport.
     * @return Value of attribute viewport.
     */
    public org.omg.uml.diagraminterchange.Point getViewport();
    /**
     * Sets the value of viewport attribute. See {@link #getViewport} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setViewport(org.omg.uml.diagraminterchange.Point newValue);
    /**
     * Returns the value of reference diagramLink.
     * @return Value of reference diagramLink.
     */
    public java.util.Collection getDiagramLink();
    /**
     * Returns the value of reference owner.
     * @return Value of reference owner.
     */
    public org.omg.uml.diagraminterchange.SemanticModelBridge getOwner();
    /**
     * Sets the value of reference owner. See {@link #getOwner} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setOwner(org.omg.uml.diagraminterchange.SemanticModelBridge newValue);
}
