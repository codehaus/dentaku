package org.omg.uml.diagraminterchange;

/**
 * GraphConnector object instance interface.
 */
public interface GraphConnector extends javax.jmi.reflect.RefObject {
    /**
     * Returns the value of attribute position.
     * @return Value of attribute position.
     */
    public org.omg.uml.diagraminterchange.Point getPosition();
    /**
     * Sets the value of position attribute. See {@link #getPosition} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setPosition(org.omg.uml.diagraminterchange.Point newValue);
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
     * Returns the value of reference graphEdge.
     * @return Value of reference graphEdge.
     */
    public java.util.Collection getGraphEdge();
}
