package org.omg.uml.behavioralelements.collaborations;

/**
 * A_context_interactionInstanceSet association proxy interface.
 */
public interface AContextInteractionInstanceSet extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param context Value of the first association end.
     * @param interactionInstanceSet Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.collaborations.CollaborationInstanceSet context, org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet interactionInstanceSet);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param context Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.collaborations.CollaborationInstanceSet getContext(org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet interactionInstanceSet);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param interactionInstanceSet Required value of the second association 
     * end.
     * @return Collection of related objects.
     */
    public java.util.Collection getInteractionInstanceSet(org.omg.uml.behavioralelements.collaborations.CollaborationInstanceSet context);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param context Value of the first association end.
     * @param interactionInstanceSet Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.collaborations.CollaborationInstanceSet context, org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet interactionInstanceSet);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param context Value of the first association end.
     * @param interactionInstanceSet Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.collaborations.CollaborationInstanceSet context, org.omg.uml.behavioralelements.collaborations.InteractionInstanceSet interactionInstanceSet);
}
