package org.omg.uml.diagraminterchange;

/**
 * A_uml1SemanticModelBridge_element association proxy interface.
 */
public interface AUml1SemanticModelBridgeElement extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param uml1SemanticModelBridge Value of the first association end.
     * @param element Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.diagraminterchange.Uml1SemanticModelBridge uml1SemanticModelBridge, org.omg.uml.foundation.core.Element element);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param element Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.Element getElement(org.omg.uml.diagraminterchange.Uml1SemanticModelBridge uml1SemanticModelBridge);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param uml1SemanticModelBridge Value of the first association end.
     * @param element Value of the second association end.
     */
    public boolean add(org.omg.uml.diagraminterchange.Uml1SemanticModelBridge uml1SemanticModelBridge, org.omg.uml.foundation.core.Element element);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param uml1SemanticModelBridge Value of the first association end.
     * @param element Value of the second association end.
     */
    public boolean remove(org.omg.uml.diagraminterchange.Uml1SemanticModelBridge uml1SemanticModelBridge, org.omg.uml.foundation.core.Element element);
}
