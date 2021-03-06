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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.dentaku.gentaku.tools.cgen.Util;
import org.dentaku.gentaku.tools.cgen.visitor.LocalDefaultElement;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class XMIGenTask extends Task {
    private UUID uuid = new UUID();
    private Set visited = new HashSet();
    private final Map typeCache = new HashMap();
    private String destdir;
    public String filename = "MDProfile.xmi";
    private List antSpecifiedModules = new ArrayList();

    public XMIGenTask() {
        System.setProperty("org.dom4j.factory", PluginDocumentFactory.class.getName());
    }

    public void execute() throws BuildException {
        System.out.println("Running " + getClass().getName());
        SAXReader reader = new SAXReader();

        Document outputDocument = DocumentHelper.createDocument();
        Branch content = createXMIDocument(outputDocument);

        Element model = createIdentifiedEmptyElement(content, "Model").addAttribute("name", "foo");

        // add the XSD and mapping documents as tagged values into the package
        Element gengenPackage = createPackageHierarchy("org.dentaku.gentaku.gengen", model);
        Element gengenOwnedElement = createOwnedElement(gengenPackage);

        // create the tag group
        Element groupStereotype = createIdentifiedEmptyElement(gengenOwnedElement, "Stereotype").addAttribute("name", "tagGroup");
        groupStereotype.addElement("UML:Stereotype.baseClass", "omg.org/UML/1.4").setText("TagDefinition");
        Element groupTagdef = createTaggedValueDefinition(gengenOwnedElement, "Group", null, new String[]{"TagDefinition"}, "String", false);
        groupTagdef.addAttribute("stereotype", groupStereotype.attributeValue("xmi.id"));

        // create the gengen stereotype marker
        Element gengenStereotype = createIdentifiedEmptyElement(gengenOwnedElement, "Stereotype").addAttribute("name", "GenGenPackage");
        gengenStereotype.addElement("UML:Stereotype.baseClass", "omg.org/UML/1.4").setText("Package");

        Element xsdTaggedValueDefinition = createTaggedValueDefinition(gengenOwnedElement, "gengen.XSD", null, new String[]{"Package"}, "String", false);
        Element mappingTaggedValueDefinition = createTaggedValueDefinition(gengenOwnedElement, "gengen.mapping", null, new String[]{"Package"}, "String", false);

        try {
            for (Iterator it = antSpecifiedModules.iterator(); it.hasNext();) {
                visited.clear();
                Module module = (Module) it.next();
                if (module.getMapping() == null || module.getSchema() == null) {
                    throw new BuildException("you must provide both a mapping and a schema argument to the XMIGenTask");
                }
                Document mappingDoc = reader.read(new File(module.getMapping()));
                final Document schemaDoc = reader.read(new File(module.getSchema()));

                // annotate XSD with mapping document
                mappingDoc.accept(new VisitorSupport() {
                    public void visit(Element node) {
                        String path = node.attributeValue("path");
                        if (path != null) {
                            LocalDefaultElement xsdVisit = (LocalDefaultElement) Util.selectSingleNode(schemaDoc, path);
                            xsdVisit.setAnnotation((LocalDefaultElement) node);
                        }
                    }
                });

                String rootPath = ((Element) Util.selectSingleNode(mappingDoc, "/mapping/element[@location='root']")).attributeValue("path");
                LocalDefaultElement rootNode = (LocalDefaultElement) Util.selectSingleNode(schemaDoc, rootPath);

                // create the location sets
                createLocationSets(rootNode, "root", new Stack());

                buildDocument(model, schemaDoc, mappingDoc, rootNode, groupTagdef, gengenStereotype, xsdTaggedValueDefinition, mappingTaggedValueDefinition);
            }

            if (destdir == null)
                destdir = "./";
            File file = new File(destdir);
            file.mkdirs();
            writeFile(outputDocument, new File(file, filename));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addModule(Module module) {
        antSpecifiedModules.add(module);
    }

    private void createLocationSets(LocalDefaultElement schemaDoc, String location, Stack stack) {
        boolean pushed = false;
        if (schemaDoc.getName().equals("element")) {
            String ref = schemaDoc.attributeValue("ref");
            if (ref != null) {
                schemaDoc = (LocalDefaultElement) Util.selectSingleNode(schemaDoc, "/xs:schema/xs:element[@name='" + ref + "']");
            }

            if (stack.contains(schemaDoc)) {
                return;
            }

            stack.push(schemaDoc);
            pushed = true;

            Element annotation = schemaDoc.getAnnotation();
            if (annotation != null) {
                String candidateLocation = annotation.attributeValue("location");
                if (candidateLocation != null) {
                    location = candidateLocation;
                }
            }
            Set s = schemaDoc.getLocations();
            if (s == null) {
                s = new HashSet();
                schemaDoc.setLocations(s);
            }
            s.add(location);
        }

        for (Iterator elementIter = schemaDoc.elements().iterator(); elementIter.hasNext();) {
            LocalDefaultElement element = (LocalDefaultElement) elementIter.next();
            createLocationSets(element, location, stack);
        }

        if (pushed) {
            stack.pop();
        }
    }

    // create the package hierarchy.  we have two elements that we need to manage here, the space for the plugin and
    // the space for the generator metadata.  if the paths overlap, there's extra work.
    private void buildDocument(Element model, Document schemaDoc, Document mappingDoc, LocalDefaultElement rootNode, Element groupTagdef, Element gengenStereotype, Element xsdTagdef, Element mappingTagdef) {
        Element modelPackage;

        String packageName = mappingDoc.getRootElement().attributeValue("tagNameBase");
       // add the package for this package
        modelPackage = createPackageHierarchy(packageName, model);

        // create tagdefs into gengen
        Element emptyUMLElement = createEmptyUMLElement(modelPackage, "ModelElement.taggedValue");
        createUMLTaggedValue(emptyUMLElement, xsdTagdef, DocumentHelper.createCDATA(schemaDoc.asXML()));

        // add one to the package we are creating
        modelPackage.addAttribute("stereotype", gengenStereotype.attributeValue("xmi.id"));

        createUMLTaggedValue(emptyUMLElement, mappingTagdef, DocumentHelper.createCDATA(mappingDoc.asXML()));

        Element scratchPackage = createOwnedElement(modelPackage);

        Element enumeration = createIdentifiedEmptyElement(scratchPackage, "Enumeration").addAttribute("name", packageName).addElement("UML:Enumeration.literal", "omg.org/UML/1.4");

        // this is the real work
        String groupPrefix = packageName.substring(packageName.lastIndexOf(".")+1);
        processNodeTags(rootNode, enumeration, scratchPackage, null, rootNode, groupTagdef, new String[0], schemaDoc, groupPrefix);

        // finally, create the stereotypes that are defined in the mapping doc
        generateStereotypes(mappingDoc, scratchPackage, schemaDoc, schemaDoc);
    }

    private void generateStereotypes(Document mappingDoc, Element scratchPackage, Document jdoDoc, Branch schemaDoc) {
        List stereotypes = Util.selectNodes(mappingDoc, "//stereotype");
        for (Iterator it = stereotypes.iterator(); it.hasNext();) {
            Element s = (Element) it.next();
            Element stereotype = createIdentifiedEmptyElement(scratchPackage, "Stereotype").addAttribute("name", s.attributeValue("name"));

            LocalDefaultElement stereotypedElement = (LocalDefaultElement) Util.selectSingleNode(schemaDoc, ((LocalDefaultElement) s.getParent()).attributeValue("path"));
            for (Iterator locIter = stereotypedElement.getLocations().iterator(); locIter.hasNext();) {
                String location = (String) locIter.next();
                createTextElement(stereotype, "Stereotype.baseClass", mapLocationName(location));
            }

            Element se = s.getParent();
            Element xsdNode = (Element) jdoDoc.selectSingleNode(se.attributeValue("path"));

            Element tag = createEmptyUMLElement(stereotype, "Stereotype.definedTag");
            createTaggedValueDefinition(tag, xsdNode.attributeValue("name"), null, null, "String", false); // todo documentation value from XSD
            for (Iterator it1 = xsdNode.selectNodes("*/xs:attribute").iterator(); it1.hasNext();) {
                Element attribute = (Element) it1.next();
                String tagdefType = getType(attribute, tag);
                boolean required = attribute.attributeValue("use") != null && attribute.attributeValue("use").equals("required");
                createTaggedValueDefinition(tag, xsdNode.attributeValue("name") + "." + attribute.attributeValue("name"), null, null, tagdefType, required); // todo documentation value from XSD
            }
        }
    }

    private void processNodeTags(LocalDefaultElement xsdNode, Element enumeration, Element tagPackage, Element literal, LocalDefaultElement parentElement, Element groupTagdef, String[] locations, Branch schemaDoc, String groupPrefix) {
        String nodeType = xsdNode.getName();
        if (nodeType.equals("element") || nodeType.equals("attributeGroup")) {
            String ref = xsdNode.attributeValue("ref");
            if (ref != null) {
                LocalDefaultElement thisElem = (LocalDefaultElement) Util.selectSingleNode(schemaDoc, "/xs:schema/xs:" +
                        nodeType +
                        "[@name='" + ref + "']");
                processNodeTags(thisElem, enumeration, tagPackage, literal, parentElement, groupTagdef, locations, schemaDoc, groupPrefix);
                return;
            }

            if (nodeType.equals("element")) {
                String name = xsdNode.attributeValue("name");
                if (name != null && visited.contains(name)) {
                    return;
                }
                visited.add(name);
                literal = createIdentifiedEmptyElement(enumeration, "EnumerationLiteral").addAttribute("name", groupPrefix + "-" + name);
                parentElement = xsdNode;

                locations = (String[]) xsdNode.getLocations().toArray(new String[xsdNode.getLocations().size()]);
                createGroupedTagdef(tagPackage, parentElement.attributeValue("name"), locations, "String", false, literal, groupTagdef);
            }
        }

        for (Iterator it = xsdNode.elementIterator(); it.hasNext();) {
            LocalDefaultElement thisElem = (LocalDefaultElement) it.next();
            if (thisElem.getName().equals("attribute")) {
                if (locations.length > 0) {
                    String tagdefType = getType(thisElem, tagPackage);
                    boolean required = thisElem.attributeValue("use") != null && thisElem.attributeValue("use").equals("true");
                    createGroupedTagdef(tagPackage, parentElement.attributeValue("name") + "." + thisElem.attributeValue("name"), locations, tagdefType, required, literal, groupTagdef);
                }
            } else {
                processNodeTags(thisElem, enumeration, tagPackage, literal, parentElement, groupTagdef, locations, schemaDoc, groupPrefix);
            }
        }
    }

    private void createGroupedTagdef(Element tagPackage, String name, String[] locations, String tagdefType, boolean required, Element enumeration, Element groupTagdef) {
        Element tvDef = createTaggedValueDefinition(tagPackage, name, null, locations, tagdefType, required); // todo documentation value from XSD
        Element taggedValue = createIdentifiedEmptyElement(createEmptyUMLElement(tvDef, "ModelElement.taggedValue"), "TaggedValue");
        taggedValue.addAttribute("name", enumeration.attributeValue("name")).addAttribute("type", groupTagdef.attributeValue("xmi.id")).addAttribute("referenceValue", enumeration.attributeValue("xmi.id"));
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

            List types = Util.selectNodes(xsdAttributeRoot, "xs:simpleType/xs:restriction/xs:enumeration");
            if (types.size() > 0) {
                if (checkBooleanType(types)) {
                    result = "Boolean";
                } else {
                    Integer key = new Integer(types.hashCode());
                    result = (String) typeCache.get(key);
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
            Element current = (Element) Util.selectSingleNode(modelPackage, "UML:Namespace.ownedElement/UML:Package[@name='" + s + "']");
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
        String result = "String";
        if (location.equals("field")) {
            result = "Attribute";
        } else if (location.equals("method")) {
            result = "Operation";
        } else if (location.equals("class")) {
            result = "Class";
        } else if (location.equals("package")) {
            result = "Package";
        } else if (location.equals("root")) {
            // result = "String";
        } else {
            result = location.substring(0, 1).toUpperCase() + location.substring(1);
        }
        return result;
    }

    private Element createTaggedValueDefinition(Branch parent, String name, String documentation, String[] baseClass, String tagdefType, boolean required) {
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

        // add base classes
        if (baseClass != null) {
            for (int i = 0; i < baseClass.length; i++) {
                String baseName = mapLocationName(baseClass[i]);
                modelPackage.addElement("TagDefinition.baseClass").setText(baseName);
            }
        }

        // create a default value of the parent name if this tag suffix is ".name"
        // @todo there are a whole range of tags that are possible like this
        if (name.endsWith(".name")) {
            modelPackage.addElement("TagDefinition.defaultValue.dataValue").setText("${parent.name}");
        }
        modelPackage.addElement("TagDefinition.createTaggedValue").addAttribute("xmi.value", (required ? "true" : "false"));

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

    public String getDestdir() {
        return destdir;
    }

    public void setDestdir(String destdir) {
        this.destdir = destdir;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
