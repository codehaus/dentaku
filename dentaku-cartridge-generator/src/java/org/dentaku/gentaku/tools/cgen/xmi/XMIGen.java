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

import org.nanocontainer.ant.PicoContainerTask;
import org.nanocontainer.integrationkit.ContainerComposer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Startable;
import org.dom4j.io.SAXReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Branch;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.DocumentFactory;
import org.dom4j.Node;
import org.dom4j.DocumentException;
import org.dentaku.services.metadata.Utils;

import java.util.List;
import java.util.Iterator;
import java.util.GregorianCalendar;
import java.io.IOException;
import java.io.Writer;
import java.io.FileWriter;
import java.io.File;

public class XMIGen implements ContainerComposer, Startable {
    UUID uuid = new UUID();

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
            Document mappingDoc = reader.read(Utils.checkURL("dentaku-cartridge-generator/src/xml/mapping.xml"));
            Document jdoDoc = reader.read(Utils.checkURL("dentaku-cartridge-generator/src/xml/jdo_2_0.xsd"));

            Document document = createXMIDoc(mappingDoc, jdoDoc);

            writeFile(document, "/dentaku-cartridge-generator/target/jellySrc/text.xmi");
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Document createXMIDoc(Document mappingDoc, Document jdoDoc) {
        Document document = DocumentHelper.createDocument();
        Branch content = createXMIDocument(document);

        Element model = createIdentifiedEmptyElement(content, "Model").addAttribute("name", "foo");

        // create the package hierarchy.  we have two elements that we need to manage here, the space for the plugin and
        // the space for the generator metadata.  if the paths overlap, there's extra work.
        Element modelPackage = model;

        // add the XSD and mapping documents as tagged values into the package
        Element gengenPackage = createPackageHierarchy("org.dentaku.gentaku.gengen", model);

        // add the package for this package
        String path = mappingDoc.getRootElement().attributeValue("tagNameBase");
        modelPackage = createPackageHierarchy(path, model);

        // create tagdefs into gengen
        Element gengenOwnedElement = createOwnedElement(gengenPackage);
        Element xsdTagdef = createTaggedValueDefinition(gengenOwnedElement, "gengen.XSD", null, "Package"); // todo documentation value from XSD
        Element emptyUMLElement = createEmptyUMLElement(modelPackage, "ModelElement.taggedValue");
        createUMLTaggedValue(emptyUMLElement, xsdTagdef, DocumentHelper.createCDATA(jdoDoc.asXML()));

        // create the gengen stereotype marker
        QName qName = DocumentFactory.getInstance().createQName("Stereotype.baseClass", "UML", "omg.org/UML/1.4");
        Element gengenStereotype = createIdentifiedEmptyElement(gengenOwnedElement, "Stereotype").addAttribute("name", "GenGenPackage");
        gengenStereotype.addElement(qName).setText("Package");
        // add one to the package we are creating
        modelPackage.addAttribute("stereotype", gengenStereotype.attributeValue("xmi.id"));

        Element mappingTagdef = createTaggedValueDefinition(gengenOwnedElement, "gengen.mapping", null, "Package"); // todo documentation value from XSD
        createUMLTaggedValue(emptyUMLElement, mappingTagdef, DocumentHelper.createCDATA(mappingDoc.asXML()));

        modelPackage = createOwnedElement(modelPackage);

        List stereotypes = mappingDoc.selectNodes("/mapping/stereotype");
        for (Iterator it = stereotypes.iterator(); it.hasNext();) {
            Element s = (Element) it.next();
            String location = s.attributeValue("location");
            List stereotypedElements = mappingDoc.selectNodes("//stereotype[@ref='" + s.attributeValue("name") + "']");
            generateForStereotypeMapping(modelPackage, s, stereotypedElements, jdoDoc, location);
        }
        return document;
    }

    private Element createUMLTaggedValue(Element parent, Element tagDefinition, Node content) {
        parent = createIdentifiedEmptyElement(parent, "TaggedValue");
        parent.addAttribute("name", tagDefinition.attributeValue("name")).addAttribute("type", tagDefinition.attributeValue("xmi.id"));
        QName qName = DocumentFactory.getInstance().createQName("TaggedValue.dataValue", "UML", "omg.org/UML/1.4");
        Element element = parent.addElement(qName);
        if (content != null) {
            element.add(content);
        }
        return parent;
    }

    private Element createPackageHierarchy(String path, Element modelPackage) {
        String pathTokens[] = path.split("\\.");
        for (int i = 0; i < pathTokens.length; i++) {
            String s = pathTokens[i];
            Element current = (Element) modelPackage.selectSingleNode("UML:Namespace.ownedElement/UML:Package[@name='" + s + "']");
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

    private Element generateForStereotypeMapping(Branch parent, Element mapping, List stereotypedElements, Document jdoDoc, String location) {

        Element stereotype = createIdentifiedEmptyElement(parent, "Stereotype").addAttribute("name", mapping.attributeValue("name"));
        createTextElement(stereotype, "Stereotype.baseClass", mapLocationName(location));
        for (Iterator elemIt = stereotypedElements.iterator(); elemIt.hasNext();) {
            Element se = ((Element) elemIt.next()).getParent();

            ////////////////////////////////////////////
            // what we actually want to do here is render with a Visitor because the breakdown of a type can be quite complex
            // the loop down below has been commented out while we get round-trip complete.

            Element xsdNode = (Element) jdoDoc.selectSingleNode(se.attributeValue("path"));

            Element tag = createEmptyUMLElement(stereotype, "Stereotype.definedTag");
            for (Iterator it = xsdNode.selectNodes("*/xs:attribute").iterator(); it.hasNext();) {
                Element attribute = (Element) it.next();
                createTaggedValueDefinition(tag, xsdNode.attributeValue("name") + "." + attribute.attributeValue("name"), null, mapLocationName(location)); // todo documentation value from XSD
            }

//            QName qName = DocumentFactory.getInstance().createQName("attribute", "xs", "http://www.w3.org/2001/XMLSchema");
//            for (Iterator attrIter = xsdNode.elementIterator(qName); attrIter.hasNext();) {
//                Element attrElement = (Element) attrIter.next();
//
//                tag = createEmptyUMLElement(stereotype, "Stereotype.definedTag");
//                createTaggedValueDefinition(tag, stereotype.attributeValue("xmi.id"), attrElement.attributeValue("name"), null); // todo documentation value from XSD
//            }
        }
        return stereotype;
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

    private Element createTaggedValueDefinition(Branch parent, String name, String documentation, String baseClass) {
        Element tagDefinition = createUMLTagDefinition(parent, name, "String");
        Element modelPackage = createEmptyUMLElement(tagDefinition, "TagDefinition.multiplicity");
        modelPackage = createIdentifiedEmptyElement(modelPackage, "Multiplicity");
        modelPackage = createEmptyUMLElement(modelPackage, "Multiplicity.range");
        createIdentifiedEmptyElement(modelPackage, "Multiplicity.range").addAttribute("lower", "1").addAttribute("upper", "1");

        if (documentation != null) {
            modelPackage = createEmptyUMLElement(tagDefinition, "ModelElement.comment");
            createIdentifiedEmptyElement(modelPackage, "Comment").addAttribute("name", documentation);
        }

        modelPackage = tagDefinition.addElement("XMI.extension").addAttribute("xmi.extender", "MagicDraw UML 8.0").addAttribute("xmi.extenderID", "MagicDraw UML 8.0");
        modelPackage.addElement("TagDefinition.baseClass").setText(baseClass);
        modelPackage.addElement("TagDefinition.createTaggedValue").addAttribute("xmi.value", "false");

        return tagDefinition;
    }

    private void writeFile(Branch document, String filename) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(System.getProperty("file.encoding"));
        format.setSuppressDeclaration(false);
        format.setExpandEmptyElements(false);

        Writer out = new FileWriter(new File(Utils.getRootDir() + filename));
        final XMLWriter xmlWriter = new XMLWriter(out, format);
        xmlWriter.setEscapeText(false);

        xmlWriter.write(document);
        xmlWriter.flush();
        xmlWriter.close();
    }

    private Element createUMLTagDefinition(Branch modelPackage, String tagdefName, String tagdefType) {
        QName qName = DocumentFactory.getInstance().createQName("TagDefinition", "UML", "omg.org/UML/1.4");
        return modelPackage.addElement(qName).addAttribute("xmi.id", uuid.generateUUID()).addAttribute("name", tagdefName).addAttribute("tagType", tagdefType);
    }

    private Element createIdentifiedEmptyElement(Branch model, String elementType) {
        return createEmptyUMLElement(model, elementType).addAttribute("xmi.id", uuid.generateUUID());
    }

    private Element createOwnedElement(Branch modelPackage) {
        return createEmptyUMLElement(modelPackage, "Namespace.ownedElement");
    }

    private void createTextElement(Branch modelPackage, String elementType, String textValue) {
        QName qName = DocumentFactory.getInstance().createQName(elementType, "UML", "omg.org/UML/1.4");
        modelPackage.addElement(qName).setText(textValue);
    }

    private Element createEmptyUMLElement(Branch model, String elementType) {
        QName ownedElemQName = DocumentFactory.getInstance().createQName(elementType, "UML", "omg.org/UML/1.4");
        Element ownedElement = model.addElement(ownedElemQName);
        return ownedElement;
    }

    private Branch createXMIDocument(Document document) {
        Document xmi = document;
        Element root = xmi.addElement("XMI").addAttribute("xmi.version", "1.2").addAttribute("timestamp", GregorianCalendar.getInstance().getTime().toString()).addNamespace("UML", "omg.org/UML/1.4");
//        xmi.addDocType("XMI", "SYSTEM", "uml14xmi12.dtd");
        Element header = root.addElement("XMI.header");
        header.addElement("XMI.documentation").addElement("XMI.exporter").setText("Gentaku Generator Generator");
        header.addElement("XMI.metamodel").addAttribute("xmi.name", "UML").addAttribute("xmi.version", "1.4");
        Element content = root.addElement("XMI.content");

        return content;
    }

}
