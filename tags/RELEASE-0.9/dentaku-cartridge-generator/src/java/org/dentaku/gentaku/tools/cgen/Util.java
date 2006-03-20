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
package org.dentaku.gentaku.tools.cgen;

import org.dom4j.Branch;
import org.jaxen.XPath;
import org.jaxen.JaxenException;
import org.jaxen.dom4j.Dom4jXPath;

import java.util.List;

public class Util {
    static public Object selectSingleNode(Branch document, String path) {
        Object result = null;
        try {
            XPath xpath = setupPath(path);
            result = xpath.selectSingleNode(document);
        } catch (JaxenException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    static public List selectNodes(Branch document, String path) {
        List result = null;
        try {
            XPath xpath = setupPath(path);
            result = xpath.selectNodes(document);
        } catch (JaxenException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    static public XPath setupPath(String path) throws JaxenException {
        XPath xpath = new Dom4jXPath(path);
        xpath.addNamespace("xs", "http://www.w3.org/2001/XMLSchema");
        xpath.addNamespace("UML", "omg.org/UML/1.4");
        return xpath;
    }
}
