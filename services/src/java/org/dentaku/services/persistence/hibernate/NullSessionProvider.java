/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.persistence.hibernate;

import net.sf.hibernate.Session;
import org.dentaku.services.persistence.PersistenceException;

public class NullSessionProvider implements SessionProvider {
    public Session getSession() throws PersistenceException {
        return null;
    }

    public void releaseSession() throws PersistenceException {
    }

    public void resetSession() throws PersistenceException {
    }

    public void rollback() {
    }
}
