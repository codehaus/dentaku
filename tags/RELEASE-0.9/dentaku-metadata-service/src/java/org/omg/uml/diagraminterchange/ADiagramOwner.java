package org.omg.uml.diagraminterchange;

/**
 * A_diagram_owner association proxy interface.
 */
public interface ADiagramOwner extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param diagram Value of the first association end.
     * @param owner Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.diagraminterchange.Diagram diagram, org.omg.uml.diagraminterchange.SemanticModelBridge owner);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param diagram Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.diagraminterchange.Diagram getDiagram(org.omg.uml.diagraminterchange.SemanticModelBridge owner);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param owner Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.diagraminterchange.SemanticModelBridge getOwner(org.omg.uml.diagraminterchange.Diagram diagram);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param diagram Value of the first association end.
     * @param owner Value of the second association end.
     */
    public boolean add(org.omg.uml.diagraminterchange.Diagram diagram, org.omg.uml.diagraminterchange.SemanticModelBridge owner);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param diagram Value of the first association end.
     * @param owner Value of the second association end.
     */
    public boolean remove(org.omg.uml.diagraminterchange.Diagram diagram, org.omg.uml.diagraminterchange.SemanticModelBridge owner);
}
