/*
 * XMLUtil.java
 * Copyright 2002-2004 Bill2, Inc.
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
package org.dentaku.services.persistence;

import org.dom4j.Element;
import org.dentaku.services.exception.XMLBeanException;

import java.text.SimpleDateFormat;

public class XMLUtil {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void addChild(Element el, String name, Object prop) throws XMLBeanException {
        org.dom4j.Element child = el.addElement(name);

        if (prop instanceof XMLBean) {
            ((XMLBean)prop).getXML(child);
        }
        else if (prop instanceof java.util.Date) {
            child.addText(dateFormat.format(prop));
        }
        else {
            child.addText(prop.toString());
        }
    }
}
