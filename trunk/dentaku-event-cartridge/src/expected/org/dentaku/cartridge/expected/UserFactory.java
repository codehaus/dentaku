/*
 * Generated file. Do not edit.
 */
package org.dentaku.cartridge.expected;

import org.dentaku.services.persistence.PersistenceException;
import org.dentaku.services.persistence.PersistenceFactory;
import org.dentaku.services.persistence.PersistenceManagerStorage;

/**
 * <p>
 * Factory class.
 * Is able to find and create objects of type User.
 * Those can be described as follows:
 * </p>
 *
 */
public class UserFactory implements PersistenceFactory {
    PersistenceManager pm = null;

    public void setManager(PersistenceManager pm) {
        this.pm = pm;
    }

   // ---------------- create methods --------------------

   /**
    * Creates a null User object.
    * @return Object  the created User
    */
    public org.dentaku.services.persistence.ModelEntity create() throws PersistenceException {
        return new User();
    }

   /**
    * Clone the object.
    * @return User the created object
    */
    public org.dentaku.services.persistence.ModelEntity create(Object src) throws PersistenceException {
        User source = (User)src;
        User object = new User();

        object.setId (source.getId());
        object.setUsername (source.getUsername());
        object.setPassword (source.getPassword());
        object.setNotes (source.getNotes());
        pm.saveOrUpdate(object);

        return object;
    }

    // ---------------- finder methods  ----------------------
    /**
     *
     * Finds User object by its primary key.
     * In Hibernate, this is just a call to load().
     *
     */
    public org.dentaku.services.persistence.ModelEntity findByPrimaryKey (java.io.Serializable id) throws PersistenceException {
        return (org.dentaku.services.persistence.ModelEntity)pm.load(User.class, id);
    }


    // ---------------- filter methods  ----------------------
}
