package org.omg.uml.behavioralelements.statemachines;

/**
 * SignalEvent object instance interface.
 */
public interface SignalEvent extends org.omg.uml.behavioralelements.statemachines.Event {
    /**
     * Returns the value of reference signal.
     * @return Value of reference signal.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Signal getSignal();
    /**
     * Sets the value of reference signal. See {@link #getSignal} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setSignal(org.omg.uml.behavioralelements.commonbehavior.Signal newValue);
}
