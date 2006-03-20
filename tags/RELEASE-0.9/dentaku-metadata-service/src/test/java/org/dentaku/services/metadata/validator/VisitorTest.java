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
package org.dentaku.services.metadata.validator;

import java.util.Iterator;

import org.dentaku.services.metadata.MetadataTestBase;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.UmlClass;

public class VisitorTest extends MetadataTestBase {
    public VisitorTest() {
        resource = getClass().getResource("DefaultConfig.xml");
    }

    public void testSomething() throws Exception {
        ModelElementImpl elem = null;
        for (Iterator it = metadata.iterator(); it.hasNext();) {
            elem = (ModelElementImpl) it.next();
            if (elem instanceof Classifier && elem.getName().equals("Root")) {
                break;
            }
            fail("root element not found");
        }

        ValidatingVisitor vv = new ValidatingVisitorBase() {
            int state = 1;
            public void visit(Namespace element, Object context) throws VisitorException {
                if (state != 3) {
                    fail("visit(Namespace...) was not called third");
                }
            }

            public void visit(GeneralizableElement element, Object context) throws VisitorException {
                if (state != 2) {
                    fail("visit(GeneralizableElement...) was not called second");
                }
                state = 3;
            }

            public void visit(UmlClass element, Object context) throws VisitorException {
                if (state != 1) {
                    fail("visit(UMLClass...) was not called first");
                }
                state = 2;
            }
        };
        elem.accept(vv, null);
    }
}
