/*
 * ThreadLocalSessionProvider.java
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
package org.dentaku.services.persistence.hibernate;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.dentaku.services.persistence.PersistenceException;

import java.lang.reflect.Method;

public class ThreadLocalSessionProvider implements SessionProvider, LogEnabled {
    static Object hbnObject = null;
    static ThreadLocal session = new ThreadLocal();
    static ThreadLocal transaction = new ThreadLocal();
    SessionFactory factory;
    static Logger log = new ConsoleLogger(Logger.LEVEL_DEBUG, ThreadLocalSessionProvider.class.getName());

    public Session getSession() throws PersistenceException {
        return getThreadLocalSession();
    }

    public static Session getThreadLocalSession() throws PersistenceException {
        Session sess = (Session) session.get();
        try {
            if (sess == null) {
                sess = getFactory().openSession();
                Transaction tr = sess.beginTransaction();
                session.set(sess);
                transaction.set(tr);
                if (log.isDebugEnabled()) {
                    log.debug("created session and started new transaction");
                }
            }
            if (!sess.isConnected()) {
                sess.reconnect();
            }
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
        return sess;
    }

    public void resetSession() throws PersistenceException {
        session.set(null);
        transaction.set(null);
    }

    public void releaseSession() throws PersistenceException {
        releaseSessionImpl();
    }

    public void releaseSessionImpl() throws PersistenceException {
        try {
            Transaction tr = (Transaction) transaction.get();
            if (tr != null && !tr.wasCommitted() && !tr.wasRolledBack()) {
                transaction.set(null);
                tr.commit();
            }
            Session session = ((Session)ThreadLocalSessionProvider.session.get());
            if (session != null) {
                session.close();
                ThreadLocalSessionProvider.session.set(null);
                if (log.isDebugEnabled()) {
                    log.debug("returned session and closed it");
                }
            }
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    public void rollback() {
        rollbackImpl();
    }

    public static void rollbackImpl() {
        Transaction tr = (Transaction) transaction.get();
        if (tr != null) {
            try {
                tr.rollback();
            } catch (HibernateException e) {
            } finally {
                transaction.set(null);
            }
        }
    }

    public void enableLogging(Logger logger) {
        log = logger;
    }

    /**
     * This method uses reflection to call to avoid a circular reference with the model project
     *
     * @return
     * @throws HibernateException
     */
    private static SessionFactory getFactory() throws PersistenceException {
        try {
            Method m = Class.forName("org.dentaku.container.jboss.HibernateUtil").getMethod("getSessionFactory", null);
            return (SessionFactory) m.invoke(null, null);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }
}
