package org.omg.uml.behavioralelements.collaborations;

/**
 * A_playedRole_conformingLink association proxy interface.
 */
public interface APlayedRoleConformingLink extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param playedRole Value of the first association end.
     * @param conformingLink Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.collaborations.AssociationRole playedRole, org.omg.uml.behavioralelements.commonbehavior.Link conformingLink);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param playedRole Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getPlayedRole(org.omg.uml.behavioralelements.commonbehavior.Link conformingLink);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param conformingLink Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getConformingLink(org.omg.uml.behavioralelements.collaborations.AssociationRole playedRole);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param playedRole Value of the first association end.
     * @param conformingLink Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.collaborations.AssociationRole playedRole, org.omg.uml.behavioralelements.commonbehavior.Link conformingLink);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param playedRole Value of the first association end.
     * @param conformingLink Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.collaborations.AssociationRole playedRole, org.omg.uml.behavioralelements.commonbehavior.Link conformingLink);
}
