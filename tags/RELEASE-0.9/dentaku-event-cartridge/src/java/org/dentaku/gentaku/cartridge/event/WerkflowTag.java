/*
 * WerkflowTag.java
 * Copyright 2004-2004 Bill2, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dentaku.gentaku.cartridge.event;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.dentaku.gentaku.cartridge.GenerationException;
import org.dentaku.gentaku.cartridge.event.graph.GraphException;
import org.dentaku.gentaku.cartridge.event.graph.GraphProcessor;
import org.dentaku.gentaku.cartridge.event.graph.JMIUMLIterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXWriter;
import org.netbeans.jmiimpl.omg.uml.foundation.core.ModelElementImpl;
import org.omg.uml.behavioralelements.activitygraphs.ActivityGraph;
import org.omg.uml.behavioralelements.statemachines.CompositeState;
import org.omg.uml.behavioralelements.statemachines.Pseudostate;
import org.omg.uml.behavioralelements.statemachines.SimpleState;
import org.omg.uml.behavioralelements.statemachines.StateVertex;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.datatypes.PseudostateKindEnum;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.Collection;
import java.util.Iterator;

public class WerkflowTag extends TagSupport {
    private Object metadataClass;

    public void setMetadata(Object metadataClass) {
        this.metadataClass = metadataClass;
    }

    public Tag getParent() {
        return null;
    }

    public void setParent(Tag tag) {
    }

    public Script getBody() {
        return null;
    }

    public void setBody(Script script) {
    }

    public JellyContext getContext() {
        return null;
    }

    public void setContext(JellyContext jellyContext) throws JellyTagException {
    }

    public void doTag(XMLOutput xmlOutput) throws MissingAttributeException, JellyTagException {
        if (metadataClass == null) {
            throw new MissingAttributeException("missing metadata attribute");
        }
        try {
            Document doc = generateDocument(metadataClass);
            SAXWriter writer = new SAXWriter();
            ContentHandler ctxHandler = new WerkflowContentHandler(xmlOutput);
            writer.setContentHandler(ctxHandler);
            writer.write(doc);

        } catch (Exception e) {
            throw new JellyTagException(e);
        }
    }

    public void invokeBody(XMLOutput xmlOutput) throws JellyTagException {
    }

    Document generateDocument(Object modelElement) throws JellyTagException {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("processes", "werkflow:basic")
                .addNamespace("j", "jelly:core")
                .addNamespace("jelly", "werkflow:jelly")
                .addNamespace("java", "werkflow:java")
                .addNamespace("python", "werkflow:python")
                .addNamespace("ognl", "werkflow:ognl");
        Element process = root.addElement("process");
        ActivityGraph ag = null;
        try {
            ag = (ActivityGraph)findOneItem(((Namespace)modelElement), ActivityGraph.class);
        } catch (GenerationException e) {
            throw new JellyTagException(e);
        }
        Element seq = process.addElement("sequence");
        Pseudostate startState = findInitialState(((CompositeState) ag.getTop()).getSubvertex());
        if (startState != null) {
            Collection subvertex = ((CompositeState) ag.getTop()).getSubvertex();
            GraphProcessor gp = new GraphProcessor();
            JMIUMLIterator nav = new JMIUMLIterator();
            try {
                gp.validate(subvertex, nav);
            } catch (GraphException e) {
                throw new JellyTagException(e);
            }
            Collection topological = gp.getTopological();
            for (Iterator it = topological.iterator(); it.hasNext();) {
                StateVertex vertex = (StateVertex) it.next();
                if (vertex instanceof SimpleState) {
                    String vertexClass = ((ModelElementImpl)modelElement).getFullyQualifiedName();
                    seq.addElement("java:action").setText(vertexClass+"Workflow.getInstance()."+((SimpleState)vertex).getEntry().getName()+"();");
                }
            }
        }
        return doc;
    }

    public Object findOneItem(Namespace modelElement, Class clazz) throws GenerationException {
        Object result = null;
        for (Iterator it = modelElement.getOwnedElement().iterator(); it.hasNext();) {
            Object o = (Object) it.next();
            if (clazz.isInstance(o)) {
                if (result != null) {
                    throw new GenerationException("multiple use cases exist");
                }
                result = o;
            }

        }
        return result;
    }

    public Pseudostate findInitialState(Collection states) {
        for (Iterator it = states.iterator(); it.hasNext();) {
            StateVertex state = (StateVertex) it.next();
            if (state instanceof Pseudostate && ((Pseudostate) state).getKind().equals(PseudostateKindEnum.PK_INITIAL)) {
                return (Pseudostate) state;
            }
        }
        return null;
    }


    /* jelly tag stuff */


    /**
     * This class intercepts calls to process the document, only emitting events
     * after the "sequence" element is hit.
     */
    private class WerkflowContentHandler implements ContentHandler {
        private boolean processing = false;
        private ContentHandler composite;

        public WerkflowContentHandler(ContentHandler composite) {
            this.composite = composite;
        }

        public void endDocument() throws SAXException {
        }

        public void startDocument() throws SAXException {
        }

        public void characters(char ch[], int start, int length) throws SAXException {
            if (processing == true) {
                composite.characters(ch, start, length);
            }
        }

        public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
            if (processing == true) {
                composite.ignorableWhitespace(ch, start, length);
            }
        }

        public void endPrefixMapping(String prefix) throws SAXException {
            if (processing == true) {
                composite.endPrefixMapping(prefix);
            }
        }

        public void skippedEntity(String name) throws SAXException {
            if (processing == true) {
                composite.skippedEntity(name);
            }
        }

        public void setDocumentLocator(Locator locator) {
            if (processing == true) {
                composite.setDocumentLocator(locator);
            }
        }

        public void processingInstruction(String target, String data) throws SAXException {
            if (processing == true) {
                composite.processingInstruction(target, data);
            }
        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            if (processing == true) {
                composite.startPrefixMapping(prefix, uri);
            }
        }

        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
            if (processing == true) {
                composite.endElement(namespaceURI, localName, qName);
            }

            if (localName.equals("sequence")) {
                processing = false;
            }
        }

        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
            if (localName.equals("sequence")) {
                processing = true;
            }

            if (processing == true) {
                composite.startElement(namespaceURI, localName, qName, atts);
            }
        }
    }


}
