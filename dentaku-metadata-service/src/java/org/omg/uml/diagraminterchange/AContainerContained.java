package org.omg.uml.diagraminterchange;

/**
 * A_container_contained association proxy interface.
 */
public interface AContainerContained extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param container Value of the first association end.
     * @param contained Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.diagraminterchange.GraphElement container, org.omg.uml.diagraminterchange.DiagramElement contained);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param container Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.diagraminterchange.GraphElement getContainer(org.omg.uml.diagraminterchange.DiagramElement contained);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param contained Required value of the second association end.
     * @return List of related objects.
     */
    public java.util.List getContained(org.omg.uml.diagraminterchange.GraphElement container);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param container Value of the first association end.
     * @param contained Value of the second association end.
     */
    public boolean add(org.omg.uml.diagraminterchange.GraphElement container, org.omg.uml.diagraminterchange.DiagramElement contained);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param container Value of the first association end.
     * @param contained Value of the second association end.
     */
    public boolean remove(org.omg.uml.diagraminterchange.GraphElement container, org.omg.uml.diagraminterchange.DiagramElement contained);
}
