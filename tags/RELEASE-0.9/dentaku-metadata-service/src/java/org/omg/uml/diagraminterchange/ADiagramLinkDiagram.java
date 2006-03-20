package org.omg.uml.diagraminterchange;

/**
 * A_diagramLink_diagram association proxy interface.
 */
public interface ADiagramLinkDiagram extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param diagramLink Value of the first association end.
     * @param diagram Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.diagraminterchange.DiagramLink diagramLink, org.omg.uml.diagraminterchange.Diagram diagram);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param diagramLink Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getDiagramLink(org.omg.uml.diagraminterchange.Diagram diagram);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param diagram Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.diagraminterchange.Diagram getDiagram(org.omg.uml.diagraminterchange.DiagramLink diagramLink);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param diagramLink Value of the first association end.
     * @param diagram Value of the second association end.
     */
    public boolean add(org.omg.uml.diagraminterchange.DiagramLink diagramLink, org.omg.uml.diagraminterchange.Diagram diagram);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param diagramLink Value of the first association end.
     * @param diagram Value of the second association end.
     */
    public boolean remove(org.omg.uml.diagraminterchange.DiagramLink diagramLink, org.omg.uml.diagraminterchange.Diagram diagram);
}
