/*
 * MetadataServiceTest.java
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
package org.dentaku.services.metadata;

import java.util.Collection;
import java.util.Iterator;

import org.netbeans.jmiimpl.omg.uml.foundation.core.ClassifierImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Operation;

public class MetadataServiceTest extends MetadataTestBase {
    public void testRootClass() throws Exception {
        ModelElementImpl elem = null;
        for (Iterator it = metadata.iterator(); it.hasNext();) {
            elem = (ModelElementImpl) it.next();
            if (elem instanceof Classifier && elem.getName().equals("root")) {
                break;
            }
            fail("root element not found");
        }

        ClassifierImpl c = (ClassifierImpl)elem;
        Collection operations = c.getOperations();
        assertTrue(operations.size() == 1);
        Operation o = (Operation)operations.iterator().next();
        assertEquals(o.getName(), "doSomething");
        assertTrue(o.getParameter().size() == 2);
        assertTrue(c.getAttributes().size() == 3);
        assertTrue(c.getTargetEnds().size() == 1);
        assertNotNull(c.getPrimaryKeyAttribute());
        assertEquals(c.getPrimaryKeyAttribute().getName(), "one");
    }
}
