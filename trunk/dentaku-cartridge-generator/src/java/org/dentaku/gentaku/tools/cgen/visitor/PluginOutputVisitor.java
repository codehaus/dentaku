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
package org.dentaku.gentaku.tools.cgen.visitor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.dentaku.gentaku.tools.cgen.plugin.GenGenPlugin;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.DocumentFactory;
import org.dom4j.QName;
import org.dom4j.tree.DefaultElement;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.TaggedValueImpl;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.TaggedValue;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * This class generates XML output descriptors based on a UML model.  It uses the visitor pattern support in dom4j, as during
 * development, it was unclear what parts of the model were going to be necessary for generation, and it was easier to go
 * through the complexity of working in dom4j directly than having to constantly update a custom AST.  Once this code is
 * stabilized, it should be used as a prototype for a version that uses a custom AST, reducing the code complexity significantly
 * because the dom4j semantics do not clutter it.
 * <br>
 * This code also relies on a custom dom4j
 */

public class PluginOutputVisitor {
    protected Document xsdDoc;
    protected UmlPackage model;

    public PluginOutputVisitor(Document xsdDoc, UmlPackage model) {
        this.xsdDoc = xsdDoc;
        this.model = model;
    }

    public boolean visit(Element mappingNode, Branch parentOutput, Namespace parent) {
        boolean result = false;
        if (mappingNode.getName().equals("element")) {
            Element mappingXSD = (Element) xsdDoc.selectSingleNode(mappingNode.attributeValue("path"));

            String location = mappingNode.attributeValue("location");
            String prefix = mappingXSD.attributeValue("name");
            Collection c = findElementsForLocationAndPrefix(location, prefix, parent);
            // now iterate those...

            if (!c.isEmpty()) {
                for (Iterator elementIterator = c.iterator(); elementIterator.hasNext();) {
                    ModelElementImpl element = (ModelElementImpl) elementIterator.next();
                    if (element instanceof Namespace) {
                        parent = (Namespace) element;
                    }

                    Element newLocalNode = DocumentHelper.createElement(mappingXSD.attributeValue("name"));
                    if (iterateAttributes(mappingXSD, element, newLocalNode)) {
                        iterateElements(mappingNode, newLocalNode, parent);
                        parentOutput.add(newLocalNode);
                        result = true;
                    }
                }
            } else if (location.equals("root")) {
                Element newLocalNode = DocumentHelper.createElement(mappingXSD.attributeValue("name"));
                if (iterateElements(mappingNode, newLocalNode, parent)) {
                    parentOutput.add(newLocalNode);
                    result = true;
                }
            }
        }
        return result;
    }

    private boolean iterateAttributes(Element mappingXSD, ModelElementImpl element, Element newLocalNode) {
        boolean result = false;
        for (Iterator it = mappingXSD.selectNodes("*/xs:attribute").iterator(); it.hasNext();) {
            Element node1 = (Element) it.next();
            String attrKey = mappingXSD.attributeValue("name") + "." + node1.attributeValue("name");
            TaggedValueImpl taggedValue = (TaggedValueImpl) element.getTaggedValue(attrKey);
            if (taggedValue != null) {
                ((Element) newLocalNode).addAttribute(node1.attributeValue("name"), taggedValue.getValue());
                result = true;
            }
        }
        return result;
    }

    private boolean iterateElements(Element mappingNode, Element newLocalNode, Namespace parent) {
        boolean result = false;
        for (Iterator it = mappingNode.elements().iterator(); it.hasNext();) {
            LocalDefaultElement n = (LocalDefaultElement) it.next();
            result = n.accept(this, newLocalNode, parent) || result;
        }
        return result;
    }

    /**
     * Returns all model objects that are in a specified location and containing a tag with the specified prefix
     *
     * @param location Specifies the model base class to search through
     * @param prefix   Node is selected if it contains a tag of that prefix
     * @param parent
     * @return Nodes matching criteria
     */
    private Collection findElementsForLocationAndPrefix(String location, final String prefix, final Namespace parent) {
        Collection elements = Collections.EMPTY_LIST;
        if (location.equals("package")) {
            elements = model.getModelManagement().getUmlPackage().refAllOfType();
        } else if (location.equals("class")) {
            elements = model.getCore().getUmlClass().refAllOfType();
        } else if (location.equals("method")) {
            elements = model.getCore().getOperation().refAllOfType();
        } else if (location.equals("field")) {
            elements = model.getCore().getAttribute().refAllOfType();
        }

        Collection result = CollectionUtils.select(elements, new Predicate() {
            public boolean evaluate(Object object) {
                ModelElement mei = (ModelElement) object;
                if (parent != null
                        && ((mei instanceof Classifier && mei.getNamespace() != parent)
                            || (mei instanceof Feature && ((Feature) mei).getOwner() != parent))) {
                    return false;
                }
                Collection c = mei.getTaggedValue();
                for (Iterator it = c.iterator(); it.hasNext();) {
                    TaggedValue taggedValue = (TaggedValue) it.next();
                    if (taggedValue.getName().startsWith(prefix)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return result;
    }

    /**
     * DocumentFactory class for dom4j.  The base implementation of the visitor was insufficient four our needs, but not sure yet
     * what the information we needed for a custom AST.  So the best thing was to keep the AST in dom4j (giving full access to the
     * actual XML) but change the accept method through a change to the factory.
     */
    public static class PluginDocumentFactory extends DocumentFactory {
        public Element createElement(QName qname) {
            return new LocalDefaultElement(qname);
        }
    }

    /**
     * The class that is instantiated by the factory.  Really only does something with the accept method, adding a couple of custom
     * state parameters we need and *not* calling all the childen of a node as the defualt impl does.
     */
    public static class LocalDefaultElement extends DefaultElement {
        public LocalDefaultElement(QName qname) {
            super(qname);
        }

        public boolean accept(PluginOutputVisitor visitor, Branch newParent, Namespace parent) {
            return visitor.visit(this, newParent, parent);
        }
    }
}
