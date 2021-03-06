package org.omg.uml.behavioralelements.usecases;

/**
 * A_includer_addition association proxy interface.
 */
public interface AIncluderAddition extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param includer Value of the first association end.
     * @param addition Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.usecases.Include includer, org.omg.uml.behavioralelements.usecases.UseCase addition);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param includer Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getIncluder(org.omg.uml.behavioralelements.usecases.UseCase addition);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param addition Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.usecases.UseCase getAddition(org.omg.uml.behavioralelements.usecases.Include includer);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param includer Value of the first association end.
     * @param addition Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.usecases.Include includer, org.omg.uml.behavioralelements.usecases.UseCase addition);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param includer Value of the first association end.
     * @param addition Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.usecases.Include includer, org.omg.uml.behavioralelements.usecases.UseCase addition);
}
