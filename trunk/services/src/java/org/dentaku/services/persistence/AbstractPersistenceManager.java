/*
 * AbstractPersistenceManager.java
 * Copyright 2002-2004 Bill2, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dentaku.services.persistence;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.dentaku.services.persistence.hibernate.SessionProvider;

import java.util.Iterator;
import java.util.Map;

public abstract class AbstractPersistenceManager implements PersistenceManager, Initializable {
    protected SessionProvider sessionProvider = null;
    private Map factories = null;

    public PersistenceFactory getPersistenceFactory(String name) throws PersistenceException {
        PersistenceFactory persistenceFactory = (PersistenceFactory) factories.get(name);
        return persistenceFactory;
    }

    public SessionProvider getSessionProvider() {
        return sessionProvider;
    }

    public void releaseSession() throws PersistenceException {
        sessionProvider.releaseSession();
    }

    public void rollback() {
        sessionProvider.rollback();
    }

    public void initialize() throws Exception {
        for (Iterator it = factories.values().iterator(); it.hasNext();) {
            PersistenceFactory factory = (PersistenceFactory) it.next();
            factory.setManager(this);
        }
    }
}
