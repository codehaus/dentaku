package org.omg.uml.diagraminterchange;

/**
 * A_graphEdge_anchor association proxy interface.
 */
public interface AGraphEdgeAnchor extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param graphEdge Value of the first association end.
     * @param anchor Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.diagraminterchange.GraphEdge graphEdge, org.omg.uml.diagraminterchange.GraphConnector anchor);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param graphEdge Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getGraphEdge(org.omg.uml.diagraminterchange.GraphConnector anchor);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param anchor Required value of the second association end.
     * @return List of related objects.
     */
    public java.util.List getAnchor(org.omg.uml.diagraminterchange.GraphEdge graphEdge);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param graphEdge Value of the first association end.
     * @param anchor Value of the second association end.
     */
    public boolean add(org.omg.uml.diagraminterchange.GraphEdge graphEdge, org.omg.uml.diagraminterchange.GraphConnector anchor);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param graphEdge Value of the first association end.
     * @param anchor Value of the second association end.
     */
    public boolean remove(org.omg.uml.diagraminterchange.GraphEdge graphEdge, org.omg.uml.diagraminterchange.GraphConnector anchor);
}
