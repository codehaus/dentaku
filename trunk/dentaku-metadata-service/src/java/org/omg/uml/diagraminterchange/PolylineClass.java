package org.omg.uml.diagraminterchange;

/**
 * Polyline class proxy interface.
 */
public interface PolylineClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Polyline createPolyline();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param isVisible 
     * @param waypoints 
     * @param closed 
     * @return The created instance object.
     */
    public Polyline createPolyline(boolean isVisible, java.util.List waypoints, boolean closed);
}
