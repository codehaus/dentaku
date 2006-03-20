package org.omg.uml.diagraminterchange;

/**
 * GraphEdge class proxy interface.
 */
public interface GraphEdgeClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public GraphEdge createGraphEdge();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param isVisible 
     * @param position 
     * @param waypoints 
     * @return The created instance object.
     */
    public GraphEdge createGraphEdge(boolean isVisible, org.omg.uml.diagraminterchange.Point position, java.util.List waypoints);
}
