package org.omg.uml.diagraminterchange;

/**
 * DiagramLink object instance interface.
 */
public interface DiagramLink extends javax.jmi.reflect.RefObject {
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
     * Returns the value of reference graphElement.
     * @return Value of reference graphElement.
     */
    public org.omg.uml.diagraminterchange.GraphElement getGraphElement();
    /**
     * Sets the value of reference graphElement. See {@link #getGraphElement} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setGraphElement(org.omg.uml.diagraminterchange.GraphElement newValue);
    /**
     * Returns the value of reference diagram.
     * @return Value of reference diagram.
     */
    public org.omg.uml.diagraminterchange.Diagram getDiagram();
    /**
     * Sets the value of reference diagram. See {@link #getDiagram} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setDiagram(org.omg.uml.diagraminterchange.Diagram newValue);
}
