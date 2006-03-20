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

import org.dentaku.services.metadata.validator.VisitorException;
import org.dom4j.Branch;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.VisitorSupport;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class JellyTemplateGeneratingVisitor extends VisitorSupport {
    private Stack parentStack = new Stack();
    private File rootDir;
    private String encoding = System.getProperty("file.encoding");
    private Map topLevelElements = new HashMap();

    public JellyTemplateGeneratingVisitor(File rootDir) {
        this.rootDir = rootDir;
    }

    public void visit(Element node) {
        String nodeName = node.getName();
        try {
            if (nodeName.equals("attribute")) {
                handleAttribute(node);
            } else if (nodeName.equals("choice")) {
                handleChoice(node);
            } else if (nodeName.equals("complexType")) {
                handleComplexType(node);
            } else if (nodeName.equals("element")) {
                handleElement(node);
            } else if (nodeName.equals("sequence")) {
                handleSequence(node);
            } else if (nodeName.equals("schema")) {
                handleSchema(node);
            }
        } catch (VisitorException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleAttribute(Element attribute) throws VisitorException {
        Branch parent = getCurrentParent();
        ((org.dom4j.Element) parent).addAttribute(attribute.getName(), "foo");

    }

    public void handleChoice(Element choice) throws VisitorException {
        Branch parent = getCurrentParent();
        QName qName = DocumentFactory.getInstance().createQName("forEach", "j", "jelly:core");
        Branch current = parent.addElement(qName).addAttribute("var", "val").addAttribute("items", "${items}");
        pushParent(current);
        for (Iterator it = choice.elementIterator(); it.hasNext();) {
            visit((Element) it.next());
        }
        popParent();
    }

    public void handleComplexType(Element complexType) throws VisitorException {
        for (Iterator it = complexType.elementIterator(); it.hasNext();) {
            visit((Element) it.next());
        }
    }

    public void handleElement(Element element) throws VisitorException {
        if (element.getParent().getName().equals("schema")) {
            QName rootName = DocumentFactory.getInstance().createQName("jelly", "j", "jelly:core");
            Branch parent = DocumentHelper.createDocument().addElement(rootName).addNamespace("x", "jelly:xml");

            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(getEncoding());
            format.setSuppressDeclaration(false);
            format.setExpandEmptyElements(false);

            pushParent(parent.addElement(element.getName()));
            for (Iterator it = element.elementIterator(); it.hasNext();) {
                visit((Element) it.next());
            }
            popParent();

            try {
                String filename = element.getName() + ".jelly";

                Writer out = new FileWriter(new File(getRootDir(), filename));
                final XMLWriter xmlWriter = new XMLWriter(out, format);
                xmlWriter.setEscapeText(false);

                xmlWriter.write(parent);
                xmlWriter.flush();
                xmlWriter.close();
            } catch (Exception e) {
                throw new VisitorException("Exception occurred when running Jelly", e);
            }

            topLevelElements.put(element.getName(), element);

        } else {
            String refName = element.attributeValue("ref");
            if (refName != null) {
                Branch parent = getCurrentParent();
                // create an include
                QName qName = DocumentFactory.getInstance().createQName("import", "j", "jelly:core");
                parent.addElement(qName).addAttribute("uri", refName + ".jelly").addAttribute("inherit", "true");
            }
        }
    }

    public void handleSequence(Element sequence) throws VisitorException {
        for (Iterator it = sequence.elementIterator(); it.hasNext();) {
            visit((Element) it.next());
        }
    }

    public void handleSchema(Element schema) throws VisitorException {
        for (Iterator it = schema.elementIterator(); it.hasNext();) {
            visit((Element) it.next());
        }
    }

    public Branch getCurrentParent() throws VisitorException {
        Branch parent = null;
        if (parentStack.size() == 0) {
            QName rootName = DocumentFactory.getInstance().createQName("jelly", "j", "jelly:core");
            parent = DocumentHelper.createDocument().addElement(rootName).addNamespace("x", "jelly:xml");
            parentStack.push(parent);
        } else {
            parent = (Branch) parentStack.peek();
        }
        return parent;
    }

    public void pushParent(Branch newParent) {
        parentStack.push(newParent);
    }

    public Branch popParent() {
        return (Branch) parentStack.pop();
    }

    public File getRootDir() {
        return rootDir;
    }

    public void setRootDir(File rootDir) {
        this.rootDir = rootDir;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
