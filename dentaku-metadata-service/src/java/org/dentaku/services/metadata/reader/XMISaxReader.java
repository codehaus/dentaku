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
package org.dentaku.services.metadata.reader;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

public class XMISaxReader extends DefaultHandler {
    private Locator locator;

    public Collection read(InputSource input) throws IOException, SAXException, ParserConfigurationException {
        URL docURL = null;
        if (docURL == null) {
            String systemId = input.getSystemId();
            if (systemId != null) {
                try {
                    docURL = new URL(systemId);
                } catch (MalformedURLException e) {
                } catch (IllegalArgumentException e) {
                }
            }
        }
//        if (context == null)
//            context = new XmiContext(extents, docURL, config);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(input, this);
//        return context.getOutermostObjects();
        return null;
    }

    public void startDocument() throws SAXException {
        super.startDocument();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    private String getLineNumber() {
        if (locator != null) {
            return new Integer(locator.getLineNumber()).toString();
        } else {
            return "null";
        }
    }
}
