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
package org.dentaku.gentaku.tools.cgen.xmi;

import org.dentaku.services.metadata.Utils;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.VisitorSupport;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;
import org.nanocontainer.ant.PicoContainerTask;
import org.nanocontainer.integrationkit.ContainerComposer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Startable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XMIGen implements ContainerComposer, Startable {
    private UUID uuid = new UUID();
    private Document jdoDoc;
    private Set visited;
    private final Map typeCache = new HashMap();

    public XMIGen() {
        System.setProperty("org.dom4j.factory", PluginDocumentFactory.class.getName());
    }

    public class XMIGenTask extends PicoContainerTask {
        public XMIGenTask() {
            extraContainerComposer = new XMIGen();
        }
    }

    public void composeContainer(MutablePicoContainer pico, Object assemblyScope) {
        // maybe we'll need this someday
    }

    public void stop() {
    }

    public void start() {
        SAXReader reader = new SAXReader();
        try {
            String filename = "dentaku-cartridge-generator/src/xml/mapping.xml";
            Document mappingDoc = reader.read(Utils.checkURL(Utils.checkURL(new File(Utils.getRootDir(), filename).toURL())));
            filename = "dentaku-cartridge-generator/src/xml/jdo_2_0.xsd";
            jdoDoc = reader.read(Utils.checkURL(new File(Utils.getRootDir(), filename).toURL()));

            // annotate XSD with mapping document
            mappingDoc.accept(new VisitorSupport() {
                public void visit(Element node) {
                    String path = node.attributeValue("path");
                    if (path != null) {
                        LocalDefaultElement xsdVisit = (LocalDefaultElement) selectSingleNode(jdoDoc, path);
                        xsdVisit.setAnnotation(node);
                    }
                }
            });

            String rootPath = ((Element) selectSingleNode(mappingDoc, "/mapping/element")).attributeValue("path");
            Document document = createXMIDoc(mappingDoc, jdoDoc, mappingDoc.getRootElement().attributeValue("tagNameBase"), rootPath);

            File file = new File(Utils.getRootDir() + "/dentaku-cartridge-generator/target/xmi");
            file.mkdirs();
            writeFile(document, new File(file, "MDProfile.xmi"));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Document createXMIDoc(Document mappingDoc, Document jdoDoc, String packageName, String rootPath) {
        Document document = DocumentHelper.createDocument();
        Branch content = createXMIDocument(document);

        Element model = createIdentifiedEmptyElement(content, "Model").addAttribute("name", "foo");

        // create the package hierarchy.  we have two elements that we need to manage here, the space for the plugin and
        // the space for the generator metadata.  if the paths overlap, there's extra work.
        Element modelPackage = model;

        // add the XSD and mapping documents as tagged values into the package
        Element gengenPackage = createPackageHierarchy("org.dentaku.gentaku.gengen", model);

        // add the package for this package
        modelPackage = createPackageHierarchy(packageName, model);

        // create tagdefs into gengen
        Element gengenOwnedElement = createOwnedElement(gengenPackage);
        Element xsdTagdef = createTaggedValueDefinition(gengenOwnedElement, "gengen.XSD", null, "Package", "String", "false"); // todo documentation value from XSD
        Element emptyUMLElement = createEmptyUMLElement(modelPackage, "ModelElement.taggedValue");
        createUMLTaggedValue(emptyUMLElement, xsdTagdef, DocumentHelper.createCDATA(jdoDoc.asXML()));

        // create the gengen stereotype marker
        Element gengenStereotype = createIdentifiedEmptyElement(gengenOwnedElement, "Stereotype").addAttribute("name", "GenGenPackage");
        gengenStereotype.addElement("UML:Stereotype.baseClass", "omg.org/UML/1.4").setText("Package");
        // add one to the package we are creating
        modelPackage.addAttribute("stereotype", gengenStereotype.attributeValue("xmi.id"));

        Element mappingTagdef = createTaggedValueDefinition(gengenOwnedElement, "gengen.mapping", null, "Package", "String", "false"); // todo documentation value from XSD
        createUMLTaggedValue(emptyUMLElement, mappingTagdef, DocumentHelper.createCDATA(mappingDoc.asXML()));

        Element scratchPackage = createOwnedElement(modelPackage);

        // create the tag groups
        Element groupStereotype = createIdentifiedEmptyElement(scratchPackage, "Stereotype").addAttribute("name", "tagGroup");
        groupStereotype.addElement("UML:Stereotype.baseClass", "omg.org/UML/1.4").setText("TagDefinition");

        Element groupTagdef = createTaggedValueDefinition(scratchPackage, "Group", null, "TagDefinition", "String", "false"); // todo documentation value from XSD
        groupTagdef.addAttribute("stereotype", groupStereotype.attributeValue("xmi.id"));

        Element enumeration = createIdentifiedEmptyElement(scratchPackage, "Enumeration").addAttribute("name", packageName).addElement("UML:Enumeration.literal", "omg.org/UML/1.4");

        generateTags(scratchPackage, jdoDoc, enumeration, rootPath, groupTagdef);

        // create the stereotypes that are defined in the mapping doc
        generateStereotypes(mappingDoc, scratchPackage, jdoDoc);

        return document;
    }

    private void generateStereotypes(Document mappingDoc, Element scratchPackage, Document jdoDoc) {
        List stereotypes = selectNodes(mappingDoc, "/mapping/stereotype");
        for (Iterator it = stereotypes.iterator(); it.hasNext();) {
            Element s = (Element) it.next();
            String location = s.attributeValue("location");

            Element stereotype = createIdentifiedEmptyElement(scratchPackage, "Stereotype").addAttribute("name", s.attributeValue("name"));
            createTextElement(stereotype, "Stereotype.baseClass", mapLocationName(location));
            for (Iterator elemIt = selectNodes(mappingDoc, "//stereotype[@ref='" + s.attributeValue("name") + "']").iterator(); elemIt.hasNext();) {
                Element se = ((Element) elemIt.next()).getParent();
                Element xsdNode = (Element) jdoDoc.selectSingleNode(se.attributeValue("path"));

                Element tag = createEmptyUMLElement(stereotype, "Stereotype.definedTag");
                for (Iterator it1 = xsdNode.selectNodes("*/xs:attribute").iterator(); it1.hasNext();) {
                    Element attribute = (Element) it1.next();
                    String tagdefType = getType(attribute, tag);
                    String required = (attribute.attributeValue("use") != null && attribute.attributeValue("use").equals("required") ? "true" : "false");
                    createTaggedValueDefinition(tag, xsdNode.attributeValue("name") + "." + attribute.attributeValue("name"), null, mapLocationName(location), tagdefType, required); // todo documentation value from XSD
                }

            }
        }
    }

    /**
     * We want to generate tags for walk of the root and all referenced elements, grouped by global element name.
     */
    private void generateTags(Element tagPackage, Document jdoDoc, Element enumeration, String rootPath, Element groupTagdef) {
        LocalDefaultElement elem = (LocalDefaultElement) selectSingleNode(jdoDoc, rootPath);
        visited = new HashSet();
        processNodeTags(elem, enumeration, tagPackage, null, elem, groupTagdef);
    }

    private void processNodeTags(LocalDefaultElement xsdNode, Element enumeration, Element tagPackage, Element literal, LocalDefaultElement parentElement, Element groupTagdef) {
        if (xsdNode.getName().equals("element")) {
            String ref = xsdNode.attributeValue("ref");
            if (ref != null) {
                LocalDefaultElement thisElem = (LocalDefaultElement) selectSingleNode(jdoDoc, "/xs:schema/xs:element[@name='" + ref + "']");
                processNodeTags(thisElem, enumeration, tagPackage, literal, parentElement, groupTagdef);
                return;
            }

            String name = xsdNode.attributeValue("name");
            if (name != null && visited.contains(name)) {
                return;
            }
            visited.add(name);
            literal = createIdentifiedEmptyElement(enumeration, "EnumerationLiteral").addAttribute("name", xsdNode.attributeValue("name"));
            parentElement = xsdNode;
        }
        for (Iterator it = xsdNode.elementIterator(); it.hasNext();) {
            LocalDefaultElement thisElem = (LocalDefaultElement) it.next();
            if (thisElem.getName().equals("attribute")) {
//                Element enum = (Element) enumeration.attributes().get(enumeration.attributeCount() - 1);
                Element annotation = parentElement.getAnnotation();
                if (annotation != null) {
                    String baseClass = mapLocationName(annotation.attributeValue("location"));
                    String tagdefType = getType(thisElem, tagPackage);
                    String required = (thisElem.attributeValue("use") != null && thisElem.attributeValue("use").equals("true") ? "true" : "false");
                    Element tvDef = createTaggedValueDefinition(tagPackage, parentElement.attributeValue("name") + "." + thisElem.attributeValue("name"), null, baseClass, tagdefType, required); // todo documentation value from XSD
                    Element taggedValue = createIdentifiedEmptyElement(createEmptyUMLElement(tvDef, "ModelElement.taggedValue"), "TaggedValue");
                    taggedValue.addAttribute("name", literal.attributeValue("name")).addAttribute("type", groupTagdef.attributeValue("xmi.id")).addAttribute("referenceValue", literal.attributeValue("xmi.id"));
                }
            } else {
                processNodeTags(thisElem, enumeration, tagPackage, literal, parentElement, groupTagdef);
            }
        }
    }

    private String getType(Element xsdAttributeRoot, Branch tagPackage) {
        String type = xsdAttributeRoot.attributeValue("type");
        // default value
        String result = "String";
        if (type != null) {
            //  it's a non-restricted simple type, do any mapping we want to do here
            // for now, do nothing and keep default of "String"
        } else {
            // there is a child element describing the type, we only handle xs:simpleType with a restriction for now

            List types = selectNodes(xsdAttributeRoot, "xs:simpleType/xs:restriction/xs:enumeration");
            if (checkBooleanType(types)) {
                result = "Boolean";
            } else {
                Integer key = new Integer(types.hashCode());
                result = (String)typeCache.get(key);
                if (result == null) {
                    // we need to generate an enumeration
                    Element enumeration = createIdentifiedEmptyElement(tagPackage, "Enumeration").addAttribute("name", xsdAttributeRoot.attributeValue("name") + "Type");
                    Element child = enumeration.addElement("UML:Enumeration.literal", "omg.org/UML/1.4");
                    for (Iterator it = types.iterator(); it.hasNext();) {
                        Element element = (Element) it.next();
                        createIdentifiedEmptyElement(child, "EnumerationLiteral").addAttribute("name", element.attributeValue("value"));
                    }
                    result = enumeration.attributeValue("xmi.id");
                    typeCache.put(key, result);
                }
            }

        }
        return result;
    }

    private boolean checkBooleanType(List types) {
        boolean result = false;
        if (types.size() == 2) {
            if (((Element) types.get(0)).attributeValue("value").equalsIgnoreCase("true")) {
                if (((Element) types.get(1)).attributeValue("value").equalsIgnoreCase("false")) {
                    result = true;
                }
            } else if (((Element) types.get(0)).attributeValue("value").equalsIgnoreCase("false")) {
                if (((Element) types.get(1)).attributeValue("value").equalsIgnoreCase("true")) {
                    result = true;
                }
            }
        }
        return result;
    }

    private Element createUMLTaggedValue(Element parent, Element tagDefinition, Node content) {
        parent = createIdentifiedEmptyElement(parent, "TaggedValue");
        parent.addAttribute("name", tagDefinition.attributeValue("name")).addAttribute("type", tagDefinition.attributeValue("xmi.id"));
        Element element = parent.addElement("UML:TaggedValue.dataValue", "omg.org/UML/1.4");
        if (content != null) {
            element.add(content);
        }
        return parent;
    }

    private Element createPackageHierarchy(String path, Element modelPackage) {
        String pathTokens[] = path.split("\\.");
        for (int i = 0; i < pathTokens.length; i++) {
            String s = pathTokens[i];
            Element current = (Element) selectSingleNode(modelPackage, "UML:Namespace.ownedElement/UML:Package[@name='" + s + "']");
            if (current == null) {
                Element model = modelPackage.element("Namespace.ownedElement");
                if (model == null) {
                    model = createOwnedElement(modelPackage);
                }
                modelPackage = createIdentifiedEmptyElement(model, "Package").addAttribute("name", s);
            } else {
                modelPackage = current;
            }
        }
        return modelPackage;
    }

    private String mapLocationName(String location) {
        String result = null;
        if (location.equals("field")) {
            result = "Attribute";
        } else if (location.equals("method")) {
            result = "Operation";
        } else if (location.equals("class")) {
            result = "Class";
        } else if (location.equals("package")) {
            result = "Package";
        } else {
            result = location.substring(0, 1).toUpperCase() + location.substring(1);
        }
        return result;
    }

    private Element createTaggedValueDefinition(Branch parent, String name, String documentation, String baseClass, String tagdefType, String required) {
        Element tagDefinition = createUMLTagDefinition(parent, name, tagdefType);

        // create multiplicity
        Element modelPackage = createEmptyUMLElement(tagDefinition, "TagDefinition.multiplicity");
        modelPackage = createIdentifiedEmptyElement(modelPackage, "Multiplicity");
        modelPackage = createEmptyUMLElement(modelPackage, "Multiplicity.range");
        createIdentifiedEmptyElement(modelPackage, "MultiplicityRange").addAttribute("lower", "1").addAttribute("upper", "1");

        // create documentation
        if (documentation != null) {
            modelPackage = createEmptyUMLElement(tagDefinition, "ModelElement.comment");
            createIdentifiedEmptyElement(modelPackage, "Comment").addAttribute("name", documentation);
        }

        // set autocreate
        modelPackage = tagDefinition.addElement("XMI.extension").addAttribute("xmi.extender", "MagicDraw UML 8.0").addAttribute("xmi.extenderID", "MagicDraw UML 8.0");
        modelPackage.addElement("TagDefinition.baseClass").setText(baseClass);
        modelPackage.addElement("TagDefinition.createTaggedValue").addAttribute("xmi.value", required);

        return tagDefinition;
    }

    private void writeFile(Branch document, File file) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(System.getProperty("file.encoding"));
        format.setSuppressDeclaration(false);
        format.setExpandEmptyElements(false);

        Writer out = new FileWriter(file);
        final XMLWriter xmlWriter = new XMLWriter(out, format);
        xmlWriter.setEscapeText(false);

        xmlWriter.write(document);
        xmlWriter.flush();
        xmlWriter.close();
    }

    private Element createUMLTagDefinition(Branch modelPackage, String tagdefName, String tagdefType) {
        return modelPackage.addElement("UML:TagDefinition", "omg.org/UML/1.4").addAttribute("xmi.id", uuid.generateUUID()).addAttribute("name", tagdefName).addAttribute("tagType", tagdefType);
    }

    private Element createIdentifiedEmptyElement(Branch model, String elementType) {
        return createEmptyUMLElement(model, elementType).addAttribute("xmi.id", uuid.generateUUID());
    }

    private Element createOwnedElement(Branch modelPackage) {
        return createEmptyUMLElement(modelPackage, "Namespace.ownedElement");
    }

    private void createTextElement(Branch modelPackage, String elementType, String textValue) {
        modelPackage.addElement("UML:" + elementType, "omg.org/UML/1.4").setText(textValue);
    }

    private Element createEmptyUMLElement(Branch model, String elementType) {
        Element ownedElement = model.addElement("UML:" + elementType, "omg.org/UML/1.4");
        return ownedElement;
    }

    private Branch createXMIDocument(Document document) {
        Element root = document.addElement("XMI").addAttribute("xmi.version", "1.2").addAttribute("timestamp", GregorianCalendar.getInstance().getTime().toString()).addNamespace("UML", "omg.org/UML/1.4");
//        xmi.addDocType("XMI", "SYSTEM", "uml14xmi12.dtd");
        Element header = root.addElement("XMI.header");
        header.addElement("XMI.documentation").addElement("XMI.exporter").setText("Gentaku Generator Generator");
        header.addElement("XMI.metamodel").addAttribute("xmi.name", "UML").addAttribute("xmi.version", "1.4");
        Element content = root.addElement("XMI.content");

        return content;
    }

    private Object selectSingleNode(Branch document, String path) {
        Object result = null;
        try {
            XPath xpath = setupPath(path);
            result = xpath.selectSingleNode(document);
        } catch (JaxenException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private List selectNodes(Branch document, String path) {
        List result = null;
        try {
            XPath xpath = setupPath(path);
            result = xpath.selectNodes(document);
        } catch (JaxenException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private XPath setupPath(String path) throws JaxenException {
        XPath xpath = new Dom4jXPath(path);
        xpath.addNamespace("xs", "http://www.w3.org/2001/XMLSchema");
        xpath.addNamespace("UML", "omg.org/UML/1.4");
        return xpath;
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
     * The class that is instantiated by the factory.  Really only does something with the accept method.
     */
    public static class LocalDefaultElement extends DefaultElement {
        private Element annotation;

        public LocalDefaultElement(QName qname) {
            super(qname);
        }

        public Element getAnnotation() {
            return annotation;
        }

        public void setAnnotation(Element annotation) {
            this.annotation = annotation;
        }
    }
}
