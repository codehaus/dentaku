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
import org.dom4j.*;
import org.dom4j.Element;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.netbeans.jmiimpl.omg.uml.foundation.core.TaggedValueImpl;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.*;
import org.omg.uml.foundation.core.Namespace;

import java.util.*;

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
    protected TagDefinition group;

    public PluginOutputVisitor(Document xsdDoc, UmlPackage model, TagDefinition group) {
        this.xsdDoc = xsdDoc;
        this.model = model;
        this.group = group;
    }

    /**
     * Standard visitor pattern entry point.
     *
     * @param mappingNode  Node that we are currently visiting in the mapping document
     * @param parentOutput Output node for document we are building.  Recursive calls gradually expand this
     * @param modelElement ModelElement from UML that we are currently working on
     * @param location
     * @return true if we did something worth keeping
     */
    public boolean visit(LocalDefaultElement mappingXSD, Branch parentOutput, ModelElement modelElement, String location) throws GenerationException {
        boolean result = false;
        if (mappingXSD.getName().equals("element")) {
            String ref = mappingXSD.attributeValue("ref");
            if (ref != null) {
                mappingXSD = (LocalDefaultElement) Util.selectSingleNode(xsdDoc, "/xs:schema/xs:element[@name='" + ref + "']");
            }

            String newLocation = updateLocation(mappingXSD, location);

            if (newLocation.equals("root")) {
                // root element is a special case because we always iterate it.  But is this the only occurence not caught
                // in the following loop?  Smells fishy.  todo This is also broken because the root element cannot have attributes
                Element newLocalNode = DocumentHelper.createElement(mappingXSD.attributeValue("name"));
                if (iterateElements(mappingXSD, newLocalNode, modelElement, newLocation)) {
                    parentOutput.add(newLocalNode);
                    result = true;
                }
            } else {
                // iterate candidate ModelElements that match these criteria
                // the tag prefix we are looking for in tags named "prefix.tag"
                String prefix = mappingXSD.attributeValue("name");
                Collection c = findElementsForLocationAndPrefix(newLocation, prefix, modelElement);
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
                            iterateElements(mappingXSD, newLocalNode, modelElement, newLocation);
                            postGenerate(mappingXSD, parentOutput, modelElement, newLocalNode);
                            parentOutput.add(newLocalNode);
                            result = true;
                        }
                    } else {
                        result = true;
                    }
                }
            }
        } else if (mappingXSD.getName().equals("attributeGroup")) {
            String ref = mappingXSD.attributeValue("ref");
            if (ref != null) {
                mappingXSD = (LocalDefaultElement) Util.selectSingleNode(xsdDoc, "/xs:schema/xs:attributeGroup[@name='" + ref + "']");
            }
            result = iterateElements(mappingXSD, parentOutput, modelElement, location);
        } else {
            result = iterateElements(mappingXSD, parentOutput, modelElement, location);
        }
        return result;
    }

    /**
     * See if we can replace our current location with one that matches the mapping document.  We find that through
     * the annotations that we added earlier.  This corresponds to the 'location' attribute in the mapping document.
     * @param mappingXSD
     * @param location
     * @return
     */
    private String updateLocation(LocalDefaultElement mappingXSD, String location) {
        LocalDefaultElement annotation = mappingXSD.getAnnotation();
        if (annotation != null) {
            String candidateLocation = annotation.attributeValue("location");
            if (candidateLocation != null) {
                // if we have an annotation and it contains a location, use it
                location = candidateLocation;
            }
        }
        return location;
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
            // this call is wrong!  With N>1 schemas with overloaded tag names, we need to check that the tag definition is in the schema we are
            // currently working with.  Where are we going to get that from???
            TaggedValueImpl taggedValue = (TaggedValueImpl) element.getTaggedValue(tagClass + "." + tagName);
            if (taggedValue != null && taggedValue.getType() == group) {
            	if(tagClass.equals("package")) {
                    newLocalNode.addAttribute(tagName, element.getFullyQualifiedName());
            	} else {
            		newLocalNode.addAttribute(tagName, taggedValue.getValue());
            	}
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
         * @param location
         * @return true if we did anything of value
         */
    private boolean iterateElements(Element mappingXSD, Branch newLocalNode, ModelElement parent, String location) throws GenerationException {
        boolean result = false;
        for (Iterator it = mappingXSD.elements().iterator(); it.hasNext();) {
            LocalDefaultElement n = (LocalDefaultElement) it.next();
            result = n.accept(this, newLocalNode, parent, location) || result;
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
    private Collection findElementsForLocationAndPrefix(String location, final String prefix, final ModelElement parent) {
        // special case if parent is not an element container, the list of candidate elements is just this element
        if (parent != null && !(parent instanceof Namespace)) {
            return Collections.singletonList(parent);
        }
        Collection elements = getCandidateElementsForLocation(location);

        Collection result = CollectionUtils.select(elements, new Predicate() {
                    public boolean evaluate(Object object) {
                        ModelElement candidate = (ModelElement) object;
                        if (notTheDroidsYoureLookingFor(candidate, parent) || (candidate instanceof HashMap)) {
                            return false;
                        }
                        Collection c = candidate.getTaggedValue();
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
     * Don't even ask what this is supposed to do because I frankly don't remember.  But if you figure it out, please
     * do rename it and replace the javadocs with something more meaningful.
     * @param candidate
     * @param parent
     * @return
     */
    private boolean notTheDroidsYoureLookingFor(ModelElement candidate, final ModelElement parent) {
        return parent != null
                       && candidate != parent
                       && ((candidate instanceof Classifier && candidate.getNamespace() != parent)
                            || (candidate instanceof Feature && ((Feature) candidate).getOwner() != parent));
    }

    /**
     * Returns crosscut of all model elements for a type.
     *
     * @param location The location we are wanting to look in, {package, class, method, field...}
     * @return All nodes in the model from that location
     */
    private Collection getCandidateElementsForLocation(String location) {
        Collection elements = new LinkedList();
        if (location.equals("package")) {
            elements.addAll(model.getModelManagement().getUmlPackage().refAllOfType());
        } else if (location.equals("class")) {
            elements.addAll(model.getCore().getUmlClass().refAllOfType());
        } else if (location.equals("method")) {
            elements.addAll(model.getCore().getOperation().refAllOfType());
        } else if (location.equals("field")) {
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
