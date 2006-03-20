package org.omg.uml.diagraminterchange;

/**
 * A_diagramElement_property association proxy interface.
 */
public interface ADiagramElementProperty extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param diagramElement Value of the first association end.
     * @param property Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.diagraminterchange.DiagramElement diagramElement, org.omg.uml.diagraminterchange.Property property);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param property Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getProperty(org.omg.uml.diagraminterchange.DiagramElement diagramElement);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param diagramElement Value of the first association end.
     * @param property Value of the second association end.
     */
    public boolean add(org.omg.uml.diagraminterchange.DiagramElement diagramElement, org.omg.uml.diagraminterchange.Property property);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param diagramElement Value of the first association end.
     * @param property Value of the second association end.
     */
    public boolean remove(org.omg.uml.diagraminterchange.DiagramElement diagramElement, org.omg.uml.diagraminterchange.Property property);
}
