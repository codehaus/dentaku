package org.omg.uml.behavioralelements.commonbehavior;

/**
 * A_ownedInstance_owner association proxy interface.
 */
public interface AOwnedInstanceOwner extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param ownedInstance Value of the first association end.
     * @param owner Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.commonbehavior.Instance ownedInstance, org.omg.uml.behavioralelements.commonbehavior.Instance owner);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param ownedInstance Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getOwnedInstance(org.omg.uml.behavioralelements.commonbehavior.Instance owner);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param owner Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Instance getOwner(org.omg.uml.behavioralelements.commonbehavior.Instance ownedInstance);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param ownedInstance Value of the first association end.
     * @param owner Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.commonbehavior.Instance ownedInstance, org.omg.uml.behavioralelements.commonbehavior.Instance owner);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param ownedInstance Value of the first association end.
     * @param owner Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.commonbehavior.Instance ownedInstance, org.omg.uml.behavioralelements.commonbehavior.Instance owner);
}
