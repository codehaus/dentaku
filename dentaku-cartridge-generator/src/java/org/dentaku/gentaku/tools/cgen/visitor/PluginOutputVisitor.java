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
import org.dentaku.gentaku.cartridge.GenerationException;
import org.dentaku.gentaku.tools.cgen.Util;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

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

    /**
     * Standard visitor pattern entry point.
     *
     * @param mappingNode  Node that we are currently visiting in the mapping document
     * @param parentOutput Output node for document we are building.  Recursive calls gradually expand this
     * @param modelElement ModelElement from UML that we are currently working on
     * @return true if we did something worth keeping
     */
    public boolean visit(LocalDefaultElement mappingXSD, Branch parentOutput, ModelElement modelElement) throws GenerationException {
        boolean result = false;
        if (!mappingXSD.getName().equals("element")) {
            result = iterateElements(mappingXSD, parentOutput, modelElement);
        } else {
            String ref = mappingXSD.attributeValue("ref");
            if (ref != null) {
                mappingXSD = (LocalDefaultElement) Util.selectSingleNode(xsdDoc, "/xs:schema/xs:element[@name='" + ref + "']");
            }

            Set locationSet = getTextNodesSet(mappingXSD, "location");
            if (locationSet.contains("root")) {
                // root element is a special case because we always iterate it.  But is this the only occurence not caught
                // in the following loop?  Smells fishy
                Element newLocalNode = DocumentHelper.createElement(mappingXSD.attributeValue("name"));
                if (iterateElements(mappingXSD, newLocalNode, modelElement)) {
                    parentOutput.add(newLocalNode);
                    result = true;
                }
            } else {
                // iterate candidate ModelElements that match these criteria
                // the tag prefix we are looking for in tags named "prefix.tag"
                String prefix = mappingXSD.attributeValue("name");
                Collection c = findElementsForLocationAndPrefix(locationSet, prefix, modelElement);
                for (Iterator elementIterator = c.iterator(); elementIterator.hasNext();) {
                    ModelElementImpl element = (ModelElementImpl) elementIterator.next();
                    if (element instanceof Namespace || element instanceof Feature) {
                        modelElement = element;
                    } else {
                        throw new AssertionError("Please report this to Brian");
                    }

                    preGenerate(mappingXSD, parentOutput, modelElement);
                    if (generate(mappingXSD, parentOutput, modelElement)) {
                        Element newLocalNode = DocumentHelper.createElement(mappingXSD.attributeValue("name"));
                        // @todo note that in older versions of this code, we used to keep elements that properly rendered, even if a node did not have
                        // any attributes that rendered.  This was removed organically in the process of tracking down problems, but it may be that with
                        // that problem solved that the old behavior is the correct behavior.  If it is, the special case noise when a root element is
                        // being rendered can probably be removed as well
                        if (iterateAttributes(mappingXSD, element, newLocalNode)) {
                            iterateElements(mappingXSD, newLocalNode, modelElement);
                            postGenerate(mappingXSD, parentOutput, modelElement, newLocalNode);
                            parentOutput.add(newLocalNode);
                            result = true;
                        }
                    } else {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    private Set getTextNodesSet(LocalDefaultElement mappingXSD, String criteria) {
        Set locations = new HashSet();
        for (Iterator it = Util.selectNodes(mappingXSD.getAnnotation(), criteria).iterator(); it.hasNext();) {
            String s = (String) ((Element) it.next()).getText();
            locations.add(s);
        }
        return locations;
    }

    private void preGenerate(LocalDefaultElement mappingXSD, Branch parentOutput, ModelElement modelElement) throws GenerationException {
        Set generators = getGenerators(mappingXSD);
        for (Iterator genIter = generators.iterator(); genIter.hasNext();) {
            LocalDefaultElement g = (LocalDefaultElement) genIter.next();
            g.getGenerator().preGenerate(mappingXSD, parentOutput, modelElement);
        }
    }

    /**
     * Standard generator calls, but if any one of them returns false, the aggregate is false.  This is to say that if one generator decides
     * that it doesn't want document generation by the standard method, then no generation is done.  This may be backwards, please speak up
     * if this doesn't work for you!
     *
     * @param mappingXSD
     * @param parentOutput
     * @param modelElement
     * @return
     * @throws GenerationException
     */
    private boolean generate(LocalDefaultElement mappingXSD, Branch parentOutput, ModelElement modelElement) throws GenerationException {
        boolean result = true;
        Set generators = getGenerators(mappingXSD);
        for (Iterator genIter = generators.iterator(); genIter.hasNext();) {
            LocalDefaultElement g = (LocalDefaultElement) genIter.next();
            result = result && g.getGenerator().generate(mappingXSD, parentOutput, modelElement);
        }
        return result;
    }

    private void postGenerate(LocalDefaultElement mappingXSD, Branch parentOutput, ModelElement modelElement, Element outputElement) throws GenerationException {
        Set generators = getGenerators(mappingXSD);
        for (Iterator genIter = generators.iterator(); genIter.hasNext();) {
            LocalDefaultElement g = (LocalDefaultElement) genIter.next();
            g.getGenerator().postGenerate(mappingXSD, parentOutput, modelElement, outputElement);
        }
    }

    private Set getGenerators(LocalDefaultElement mappingXSD) {
        Set generators = new HashSet();
        for (Iterator genIter = Util.selectNodes(mappingXSD.getAnnotation(), "stereotype").iterator(); genIter.hasNext();) {
            LocalDefaultElement element = (LocalDefaultElement) genIter.next();
            generators.addAll(Util.selectNodes(element, "generator"));
        }
        return generators;
    }

    /**
     * Iterate all attributes for a node.  The result of this call is used to determine whether child elements are rendered
     * as well.
     *
     * @param mappingXSD   The Schema document node for matching the element we are looking at
     * @param element      The model element that we are examining
     * @param newLocalNode Output node for document we are building.  Recursive calls at higher level gradually expand this
     * @return true if we should keep this node
     */
    private boolean iterateAttributes(Element mappingXSD, ModelElementImpl element, Element newLocalNode) {
        boolean result = false;
        String tagClass = mappingXSD.attributeValue("name");

        // first check to see if there is a tag without the attribute value.  This triggers class generation regardless of
        // whether there is any attributes
        if ((TaggedValueImpl) element.getTaggedValue(tagClass) != null) {
            result = true;
        }
        for (Iterator it = mappingXSD.selectNodes("*/xs:attribute").iterator(); it.hasNext();) {
            Element node = (Element) it.next();
            String tagName = node.attributeValue("name");
            TaggedValueImpl taggedValue = (TaggedValueImpl) element.getTaggedValue(tagClass + "." + tagName);
            if (taggedValue != null) {
                newLocalNode.addAttribute(tagName, taggedValue.getValue());
                result = true;
            }
        }
        return result;
    }

    /**
     * Iterate all the elements of the mappingNode by visiting each one of them.
     *
     * @param mappingXSD   The node of the XSD
     * @param newLocalNode Output node for document we are building.  Recursive calls gradually expand this
     * @param parent       The parent element from the UML model that we are parsing in parellel
     * @return true if we did anything of value
     */
    private boolean iterateElements(Element mappingXSD, Branch newLocalNode, ModelElement parent) throws GenerationException {
        boolean result = false;
        for (Iterator it = mappingXSD.elements().iterator(); it.hasNext();) {
            LocalDefaultElement n = (LocalDefaultElement) it.next();
            result = n.accept(this, newLocalNode, parent) || result;
        }
        return result;
    }

    /**
     * Returns all model objects (read: candidates) that are in a specified location and containing a tag with the specified prefix
     *
     * @param location Specifies the model base class to search through
     * @param prefix   Node is selected if it contains a tag of that prefix
     * @param parent   ModelElement in the UML model that we are currently considering
     * @return Nodes matching criteria
     */
    private Collection findElementsForLocationAndPrefix(Set locations, final String prefix, final ModelElement parent) {
        // special case if parent is not an element container, the list of candidate elements is just this element
        if (parent != null && !(parent instanceof Namespace)) {
            return Collections.singletonList(parent);
        }
        Collection elements = getCandidateElementsForLocation(locations);

        Collection result = CollectionUtils.select(elements, new Predicate() {
            public boolean evaluate(Object object) {
                ModelElement mei = (ModelElement) object;
                if (parent != null && ((mei instanceof Classifier && mei.getNamespace() != parent) || (mei instanceof Feature && ((Feature) mei).getOwner() != parent)) || (mei instanceof HashMap)) {
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
     * Returns crosscut of all model elements for a type.
     *
     * @param location The location we are wanting to look in, {package, class, method, field...}
     * @return All nodes in the model from that location
     */
    private Collection getCandidateElementsForLocation(Set locations) {
        Collection elements = new LinkedList();
        if (locations.contains("package")) {
            elements.addAll(model.getModelManagement().getUmlPackage().refAllOfType());
        } else if (locations.contains("class")) {
            elements.addAll(model.getCore().getUmlClass().refAllOfType());
        } else if (locations.contains("method")) {
            elements.addAll(model.getCore().getOperation().refAllOfType());
        } else if (locations.contains("field")) {
            elements.addAll(model.getCore().getAttribute().refAllOfType());
        }
        return elements;
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
}
