package org.omg.uml.behavioralelements.statemachines;

/**
 * A_submachineState_submachine association proxy interface.
 */
public interface ASubmachineStateSubmachine extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param submachineState Value of the first association end.
     * @param submachine Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.statemachines.SubmachineState submachineState, org.omg.uml.behavioralelements.statemachines.StateMachine submachine);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param submachineState Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getSubmachineState(org.omg.uml.behavioralelements.statemachines.StateMachine submachine);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param submachine Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.statemachines.StateMachine getSubmachine(org.omg.uml.behavioralelements.statemachines.SubmachineState submachineState);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param submachineState Value of the first association end.
     * @param submachine Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.statemachines.SubmachineState submachineState, org.omg.uml.behavioralelements.statemachines.StateMachine submachine);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param submachineState Value of the first association end.
     * @param submachine Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.statemachines.SubmachineState submachineState, org.omg.uml.behavioralelements.statemachines.StateMachine submachine);
}
