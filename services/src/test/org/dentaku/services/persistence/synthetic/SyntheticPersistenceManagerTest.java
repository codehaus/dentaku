/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package org.dentaku.services.persistence.synthetic;

import org.dentaku.services.ServiceTestBase;
import org.dentaku.services.persistence.PersistenceManager;
import test.Root;

public class SyntheticPersistenceManagerTest extends ServiceTestBase {
    private PersistenceManager pm;

    protected void setUp() throws Exception {
        super.setUp();
        pm = (PersistenceManager)container.lookup(PersistenceManager.ROLE);
        assertNotNull(pm);
    }

    public void testZeroRelation() throws Exception {
        Root r = (Root)pm.load(Root.class, new Long(0));
        assertNotNull(r);
        assertTrue(r.getChild().size() == 0);
    }

    public void testOneRelation() throws Exception {
        Root r = (Root)pm.load(Root.class, new Long(1));
        assertNotNull(r);
        assertTrue(r.getChild().size() == 1);
    }

    public void testManyRelation() throws Exception {
        Long id = new Long((System.currentTimeMillis() % 50) * 3 + 2);
        Root r = (Root)pm.load(Root.class, id);
        assertNotNull(r);
        assertTrue(Root.getTestCollectionSize() == r.getChild().size());
    }


}
