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
package org.dentaku.gentaku.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicConfigurator;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.util.JAXPUtils;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;

/**
 * I haven't looked, but this seems to be necessary because Ant is just a SAX event listener
 * and can't arbitrarily stop generating events for an XML stream after it starts parsing a
 * document.  This code was primarily swiped from Ant XMLFragment.java, with a conversion to
 * use Dom4J.
 */
public class PlexusAntConfig implements DynamicElement {
    private Document doc;
    private DocumentFragment fragment;

    public PlexusAntConfig() {
        doc = JAXPUtils.getDocumentBuilder().newDocument();
        fragment = doc.createDocumentFragment();
        Element e = doc.createElement("plexus");
        fragment.appendChild(e);
    }

    public Object createDynamicElement(String string) throws BuildException {
        Element e = doc.createElement(string);
        fragment.appendChild(e);
        return new Plexus(e);
    }

    public void addText(String s) {
        addText(fragment, s);
    }

    public String getConfig() throws TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        Writer strWriter = new StringWriter();
        StreamResult result = new StreamResult(strWriter);
        transformer.transform(new DOMSource(fragment), result);
        return strWriter.toString();
    }

    public void addText(Node n, String s) {
        if (s != null && !s.trim().equals("")) {
            Text t = doc.createTextNode(s);
            n.appendChild(t);
        }
    }

    public class Plexus implements DynamicConfigurator {
        private Element e;

        public Plexus(Element e) {
            this.e = e;
        }

        public void addText(String s) {
            PlexusAntConfig.this.addText(e, s);
        }

        public void setDynamicAttribute(String name, String value) throws BuildException {
            e.setAttribute(name, value);
        }

        public Object createDynamicElement(String name) throws BuildException {
            Element e2 = doc.createElement(name);
            e.appendChild(e2);
            return new Plexus(e2);
        }
    }
}
