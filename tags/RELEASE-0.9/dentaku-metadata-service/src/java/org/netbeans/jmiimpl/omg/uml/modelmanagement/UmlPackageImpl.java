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
package org.netbeans.jmiimpl.omg.uml.modelmanagement;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.netbeans.mdr.storagemodel.StorableObject;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.modelmanagement.ModelManagementPackage;
import org.omg.uml.modelmanagement.UmlPackage;
import org.omg.uml.modelmanagement.UmlPackageClass;

public abstract class UmlPackageImpl extends ModelElementImpl implements UmlPackage {

    public UmlPackageImpl(StorableObject storable) {
        super(storable);
    }

    public UmlPackage getChildPackage(String name, boolean create) {
        org.omg.uml.modelmanagement.UmlPackage current = this;

        if (name != null && name.length() > 0) {
            String[] names = name.split("\\.");
            CorePackage core = ((org.omg.uml.UmlPackage) refOutermostPackage()).getCore();
            UmlPackageClass namespaceClass = ((ModelManagementPackage) refImmediatePackage()).getUmlPackage();

            for (int i = 0; i < names.length; i++) {
                final String s = names[i];
                final UmlPackage currentCopy = current;
                Object l = CollectionUtils.find(namespaceClass.refAllOfType(), new Predicate() {

                    public boolean evaluate(Object object) {
                        return object != null && ((UmlPackage) object).getNamespace() == currentCopy
                                && ((UmlPackage) object).getName().equals(s);
                    }
                });

                if (l == null) {
                    if (create) {
                        // add Namespace and make it current
                        UmlPackage newUmlPackage = namespaceClass.createUmlPackage();
                        newUmlPackage.setName(s);
                        core.getANamespaceOwnedElement().add(current, newUmlPackage);
                        current = newUmlPackage;
                    } else {
                        throw new RuntimeException("'" + name + "' not found and create disabled");
                    }
                } else {
                    current = (UmlPackage) l;
                }
            }
        }
        return current;
    }
}
