/*
 * SyntheticPersistenceManagerTest.java
 * Copyright 2004-2004 Bill2, Inc.
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
package org.dentaku.services.persistence.synthetic;

import org.dentaku.services.PersistenceServiceTestBase;
import org.dentaku.services.persistence.PersistenceManager;
import test.Root;
import test.Child;

public class SyntheticPersistenceManagerTest extends PersistenceServiceTestBase {
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
        Long id = new Long(Math.max(2, System.currentTimeMillis() % Root.getTestCollectionSize()));
        Root r = (Root)pm.load(Root.class, id);
        assertNotNull(r);
        assertTrue(Child.getTestCollectionSize() == r.getChild().size());
    }


}
