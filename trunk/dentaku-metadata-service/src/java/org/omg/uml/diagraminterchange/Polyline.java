package org.omg.uml.diagraminterchange;

/**
 * Polyline object instance interface.
 */
public interface Polyline extends org.omg.uml.diagraminterchange.GraphicPrimitive {
    /**
     * Returns the value of attribute waypoints.
     * @return Value of waypoints attribute.
     */
    public java.util.List getWaypoints();
    /**
     * Returns the value of attribute closed.
     * @return Value of attribute closed.
     */
    public boolean isClosed();
    /**
     * Sets the value of closed attribute. See {@link #isClosed} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setClosed(boolean newValue);
}
