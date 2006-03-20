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
package org.dentaku.gentaku.tools.cgen.plugin;

import org.dentaku.gentaku.cartridge.GenerationException;
import org.dentaku.gentaku.cartridge.Generator;
import org.dentaku.gentaku.tools.cgen.Util;
import org.dentaku.gentaku.tools.cgen.visitor.LocalDefaultElement;
import org.dentaku.gentaku.tools.cgen.visitor.PluginOutputVisitor;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.dentaku.services.metadata.RepositoryException;
import org.dentaku.services.metadata.Utils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.netbeans.jmiimpl.omg.uml.modelmanagement.ModelImpl;
import org.picocontainer.Startable;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.io.FileReader;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This XDoclet2-ready plugin generates output based on the setup of the model that it is given.  This class deals with document IO
 * and firing up the visitor.   All of the heavy lifting is done in the PluginOutputVisitor.
 */
public class GenGenPlugin implements Startable {
    protected String destdir;
    protected String destinationfilename = "output.xml";
    protected JMICapableMetadataProvider mp;
    protected boolean validate = true;
    private String encoding = System.getProperty("file.encoding");

    static {
        System.setProperty("org.dom4j.factory", PluginOutputVisitor.PluginDocumentFactory.class.getName());
    }
    public GenGenPlugin(JMICapableMetadataProvider metadataProvider) {
        mp = metadataProvider;

        // set up dom4j factory to use our factory, inserting a subclass of DefaultElement with a different visit() method.
    }

    public void start() {
        System.out.println("Running " + getClass().getName());
        Collection metadata = mp.getJMIMetadata();
        for (Iterator it = metadata.iterator(); it.hasNext();) {
            Object o = (Object) it.next();
            if (o instanceof ModelElementImpl && ((ModelElementImpl) o).getStereotypeNames().contains("GenGenPackage")) {
                ModelElementImpl pkg = (ModelElementImpl) o;
                try {
                    // get the two documents out of the model
                    Document mappingDoc = DocumentHelper.parseText((String) pkg.getTaggedValue("gengen.mapping").getDataValue().iterator().next());
                    final Document xsdDoc = DocumentHelper.parseText((String) pkg.getTaggedValue("gengen.XSD").getDataValue().iterator().next());

                    Collection generators = mappingDoc.selectNodes("/mapping/generator");
                    final Map generatorMap = new HashMap();
                    for (Iterator getIterator = generators.iterator(); getIterator.hasNext();) {
                        Element element = (Element) getIterator.next();
                        try {
                            Generator g = (Generator) Class.forName(element.attributeValue("canonicalName")).newInstance();
                            generatorMap.put(element.attributeValue("name"), g);
                        } catch (InstantiationException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    // annotate XSD with mapping document
                    mappingDoc.accept(new VisitorSupport() {
                        public void visit(Element node) {
                            String path = node.attributeValue("path");
                            if (path != null) {
                                LocalDefaultElement xsdVisit = (LocalDefaultElement) Util.selectSingleNode(xsdDoc, path);
                                xsdVisit.setAnnotation((LocalDefaultElement) node);
                            }
                            String key = node.attributeValue("ref");
                            if (node.getName().equals("generator") && key != null) {
                                if (generatorMap.keySet().contains(key)) {
                                    ((LocalDefaultElement) node).setGenerator((Generator)generatorMap.get(key));
                                } else {
                                    throw new RuntimeException("generator key '"+key+"' not created or not found");
                                }
                            }
                        }
                    });

                    // create a new output document
                    Document outputDocument = DocumentHelper.createDocument();

                    // find element root of mapping document
                    Element rootElementCandidate = (Element) Util.selectSingleNode(mappingDoc, "/mapping/element[@location='root']");
                    if (rootElementCandidate == null) {
                        throw new RuntimeException("could not find root element of XSD");
                    }
                    String rootPath = rootElementCandidate.attributeValue("path");
                    LocalDefaultElement rootElement = (LocalDefaultElement) Util.selectSingleNode(xsdDoc, rootPath);

                    // get the model root (there can be only one...)
                    ModelImpl m = Utils.getModelRoot(mp.getModel());

                    // pregenerate
                    for (Iterator genIterator = generatorMap.values().iterator(); genIterator.hasNext();) {
                        Generator generator = (Generator) genIterator.next();
                        generator.preProcessModel(m);
                    }

                    // create a visitor and visit the document
                    PluginOutputVisitor visitor = new PluginOutputVisitor(xsdDoc, mp.getModel());
                    rootElement.accept(visitor, outputDocument, null, null);

                    // postegenerate
                    for (Iterator genIterator = generatorMap.values().iterator(); genIterator.hasNext();) {
                        Generator generator = (Generator) genIterator.next();
                        generator.postProcessModel(m);
                    }

                    // generate the output document
                    File outputFile;
                    Writer destination;
                    if (getDestdir() != null) {
                        String dirPath = getDestdir();
                        File dir = new File(dirPath);
                        dir.mkdirs();
                        outputFile = new File(dir, getDestinationfilename());
                        destination = new FileWriter(outputFile);
                    } else {
                        outputFile = File.createTempFile("GenGen", ".xml");
                        outputFile.deleteOnExit();
                        destination = new OutputStreamWriter(System.out);
                    }

                    if (outputDocument.getRootElement() != null) {
                        generateOutput(outputFile, outputDocument, xsdDoc, destination, generatorMap);
                    } else {
                        System.out.println("WARNING: no output generated, do you have root tags defined?");
                    }

                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SAXException e) {
                    throw new RuntimeException(e);
                } catch (RepositoryException e) {
                    throw new RuntimeException(e);
                } catch (GenerationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Method is a bit tricky, because we can't add the actual touchup code until the validation with our schema has happened first.  Why?  Because
     * the touchup might add a DTD, not a Schema, which fails.  Further, if we are validating, we want to write the output to a file first, in case
     * validation fails.  Need to be able to look at the file in case something fails.
     * @param outputFile
     * @param outputDocument
     * @param xsdDoc
     * @param destination
     * @param generatorMap
     * @throws IOException
     * @throws SAXException
     * @throws DocumentException
     * @throws GenerationException
     */
    private void generateOutput(File outputFile, Document outputDocument, final Document xsdDoc, Writer destination, Map generatorMap) throws IOException, SAXException, DocumentException, GenerationException {
        if (validate) {
            // we write the document once here, in case validation fails...
            Writer writer = new FileWriter(outputFile);
            prettyPrint(outputDocument, writer);
            writer.close();

            outputDocument = validate(xsdDoc, new FileReader(outputFile));
            touchupOutput(generatorMap, outputDocument);

            prettyPrint(outputDocument, destination);
        } else {
            // just write it out
            touchupOutput(generatorMap, outputDocument);
            Writer writer = new FileWriter(outputFile);
            prettyPrint(outputDocument, writer);
            writer.close();
        }
    }

    private void touchupOutput(Map generatorMap, Document outputDocument) throws GenerationException {
// touch up final file after valiadation
        for (Iterator genIterator = generatorMap.values().iterator(); genIterator.hasNext();) {
            Generator generator = (Generator) genIterator.next();
            generator.touchupOutputDocument(outputDocument);
        }
    }

    private void prettyPrint(Document document, Writer writer) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(encoding);
        format.setSuppressDeclaration(false);
        format.setExpandEmptyElements(false);

        XMLWriter xmlWriter = new XMLWriter(writer, format);
        xmlWriter.write(document);
        xmlWriter.flush();
    }

    private Document validate(Document xsdDoc, Reader fileReader) throws IOException, SAXException, DocumentException {
        File temp = File.createTempFile("schema", ".xsd");
        temp.deleteOnExit();
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));
        out.write(xsdDoc.asXML());
        out.flush();
        out.close();

        SAXReader saxReader = new SAXReader(true);
        saxReader.setFeature("http://apache.org/xml/features/validation/schema", true);
        URL schemaPath = temp.toURL();
        saxReader.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", schemaPath.toString());
        return saxReader.read(fileReader);
    }

    public String getDestdir() {
        return destdir;
    }

    public void setDestdir(String destdir) {
        this.destdir = destdir;
    }

    public String getDestinationfilename() {
        return destinationfilename;
    }

    public void setDestinationfilename(String destinationfilename) {
        this.destinationfilename = destinationfilename;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setValidate(boolean validate) {
        this.validate = new Boolean(validate).booleanValue();
    }

    public void stop() {
    }
}
