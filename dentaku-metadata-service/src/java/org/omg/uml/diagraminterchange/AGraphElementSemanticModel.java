package org.omg.uml.diagraminterchange;

/**
 * A_graphElement_semanticModel association proxy interface.
 */
public interface AGraphElementSemanticModel extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param graphElement Value of the first association end.
     * @param semanticModel Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.diagraminterchange.GraphElement graphElement, org.omg.uml.diagraminterchange.SemanticModelBridge semanticModel);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param graphElement Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.diagraminterchange.GraphElement getGraphElement(org.omg.uml.diagraminterchange.SemanticModelBridge semanticModel);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param semanticModel Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.diagraminterchange.SemanticModelBridge getSemanticModel(org.omg.uml.diagraminterchange.GraphElement graphElement);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param graphElement Value of the first association end.
     * @param semanticModel Value of the second association end.
     */
    public boolean add(org.omg.uml.diagraminterchange.GraphElement graphElement, org.omg.uml.diagraminterchange.SemanticModelBridge semanticModel);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param graphElement Value of the first association end.
     * @param semanticModel Value of the second association end.
     */
    public boolean remove(org.omg.uml.diagraminterchange.GraphElement graphElement, org.omg.uml.diagraminterchange.SemanticModelBridge semanticModel);
}
