package org.omg.uml.diagraminterchange;

/**
 * GraphConnector class proxy interface.
 */
public interface GraphConnectorClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public GraphConnector createGraphConnector();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param position 
     * @return The created instance object.
     */
    public GraphConnector createGraphConnector(org.omg.uml.diagraminterchange.Point position);
}
