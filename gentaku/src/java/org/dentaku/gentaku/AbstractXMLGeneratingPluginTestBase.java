/*
 * AbstractXMLGeneratingPluginTestBase.java
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
package org.dentaku.gentaku;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.generama.tests.AbstractPluginTestCase;
import org.generama.MetadataProvider;
import org.codehaus.plexus.embed.Embedder;

import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Baseclass for testing generation of XML content. Ignores
 * whitespace, ordering of attributes and comments.
 * Uses XMLUnit internally to compare equality of XML documents.
 * 
 */
public abstract class AbstractXMLGeneratingPluginTestBase extends AbstractPluginTestCase implements EntityResolver {
    private Map dtds = new HashMap();
    protected DocumentBuilder expectedParser;
    protected DocumentBuilder actualParser;

    protected void registerDtd(String publicId, URL dtd) {
        dtds.put(publicId, dtd);

        // now turn on validation
        XMLUnit.getControlDocumentBuilderFactory().setValidating(true);
        XMLUnit.getTestDocumentBuilderFactory().setValidating(true);
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        URL dtd = (URL) dtds.get(publicId);
        if (dtd != null) {
            InputSource inputSource = new InputSource(dtd.openStream());
            inputSource.setPublicId(publicId);
            inputSource.setSystemId(systemId);
            return inputSource;
        }
        return null;
    }

    protected final void compare(URL expected, URL actual) throws IOException, SAXException {
        Document expectedDocument = XMLUnit.buildDocument(expectedParser, new InputSource(expected.openStream()));
        Document actualDocument = XMLUnit.buildDocument(actualParser, new InputSource(actual.openStream()));

        Diff diff = new Diff(expectedDocument, actualDocument) {
            public int differenceFound(Difference difference) {
                if ("sequence of attributes".equals(difference.getDescription())) {
                    return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
                }
                return super.differenceFound(difference);
            }
        };
        XMLTestCase xmlunit = new XMLTestCase(getName());

        xmlunit.assertXMLIdentical(diff, true);
    }

    protected void setUp() throws Exception {
        super.setUp();
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.getControlDocumentBuilderFactory().setIgnoringComments(true);
        XMLUnit.getTestDocumentBuilderFactory().setIgnoringComments(true);
        ErrorHandler errorHandler = new ErrorHandler() {
            public void error(SAXParseException exception) throws SAXException {
                throw exception;
            }
            public void fatalError(SAXParseException exception) throws SAXException {
                throw exception;
            }
            public void warning(SAXParseException exception) {
            }
        };
        expectedParser = XMLUnit.getControlParser();
        expectedParser.setErrorHandler(errorHandler);
        expectedParser.setEntityResolver(this);

        actualParser = XMLUnit.getTestParser();
        actualParser.setErrorHandler(errorHandler);
        actualParser.setEntityResolver(this);
    }

    protected MetadataProvider createMetadataProvider() throws Exception {
        Embedder e = new Embedder();
        e.setConfiguration(getClass().getResource("TestConfiguration.xml"));
        e.start();
        return (MetadataProvider) e.lookup(MetadataProvider.ROLE);
    }
}
