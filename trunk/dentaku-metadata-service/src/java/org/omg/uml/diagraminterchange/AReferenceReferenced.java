package org.omg.uml.diagraminterchange;

/**
 * A_reference_referenced association proxy interface.
 */
public interface AReferenceReferenced extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param reference Value of the first association end.
     * @param referenced Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.diagraminterchange.Reference reference, org.omg.uml.diagraminterchange.DiagramElement referenced);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param reference Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getReference(org.omg.uml.diagraminterchange.DiagramElement referenced);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param referenced Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.diagraminterchange.DiagramElement getReferenced(org.omg.uml.diagraminterchange.Reference reference);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param reference Value of the first association end.
     * @param referenced Value of the second association end.
     */
    public boolean add(org.omg.uml.diagraminterchange.Reference reference, org.omg.uml.diagraminterchange.DiagramElement referenced);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param reference Value of the first association end.
     * @param referenced Value of the second association end.
     */
    public boolean remove(org.omg.uml.diagraminterchange.Reference reference, org.omg.uml.diagraminterchange.DiagramElement referenced);
}
