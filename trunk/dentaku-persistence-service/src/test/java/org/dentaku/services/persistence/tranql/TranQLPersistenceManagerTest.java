/**
 *
 *  Copyright 2004 Brian Topping
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dentaku.services.persistence.tranql;

import org.dentaku.services.PersistenceServiceTestBase;
import org.dentaku.services.persistence.PersistenceManagerStorage;
import test.Root;
import test.Child;

public class TranQLPersistenceManagerTest extends PersistenceServiceTestBase {
    private PersistenceManagerStorage pm;

    protected void setUp() throws Exception {
        super.setUp();
        pm = (PersistenceManagerStorage)container.lookup(PersistenceManagerStorage.ROLE);
        assertNotNull(pm);
    }

    public void testLoad() throws Exception {
        Root r = (Root)pm.load(Root.class, new Long(1));
        assertNotNull(r);
    }

}
