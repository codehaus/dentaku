package org.omg.uml.diagraminterchange;

/**
 * GraphNode class proxy interface.
 */
public interface GraphNodeClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public GraphNode createGraphNode();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param isVisible 
     * @param position 
     * @param size 
     * @return The created instance object.
     */
    public GraphNode createGraphNode(boolean isVisible, org.omg.uml.diagraminterchange.Point position, org.omg.uml.diagraminterchange.Dimension size);
}
