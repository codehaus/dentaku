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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.core.DefaultClassMapper;
import org.dentaku.services.metadata.nbmdr.XMIInputConfigImpl;
import org.dentaku.gentaku.tools.cgen.model.Schema;
import org.dentaku.gentaku.tools.cgen.model.Element;
import org.dentaku.gentaku.tools.cgen.model.ComplexType;
import org.dentaku.gentaku.tools.cgen.model.Sequence;
import org.dentaku.gentaku.tools.cgen.model.Attribute;
import org.dentaku.gentaku.tools.cgen.model.SimpleType;
import org.dentaku.gentaku.tools.cgen.model.Restriction;
import org.dentaku.gentaku.tools.cgen.model.Enumeration;
import org.dentaku.gentaku.tools.cgen.model.Choice;
import org.dentaku.gentaku.tools.cgen.model.Notation;
import org.dentaku.gentaku.tools.cgen.model.SchemaConverter;
import org.dentaku.gentaku.tools.cgen.model.ElementConverter;
import org.dentaku.gentaku.tools.cgen.model.AttributeConverter;
import org.dentaku.gentaku.tools.cgen.model.ComplexTypeConverter;
import org.dentaku.gentaku.tools.cgen.model.NotationConverter;
import org.dentaku.gentaku.tools.cgen.model.ChoiceConverter;
import org.dentaku.gentaku.tools.cgen.model.EnumerationConverter;
import org.dentaku.gentaku.tools.cgen.model.RestrictionConverter;
import org.dentaku.gentaku.tools.cgen.model.SimpleTypeConverter;
import org.dentaku.gentaku.tools.cgen.model.SequenceConverter;

import java.io.FileReader;

public class CodeGenTest extends junit.framework.TestCase {
    protected void setUp() throws Exception {

    }

    protected void tearDown() throws Exception {

    }

    public void testGen() throws Exception {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        XStream xs = new XStream(null, classMapper, new XppDriver());

        xs.alias("xs:schema", Schema.class);
        xs.registerConverter(new SchemaConverter(classMapper, "xs:schema"));

        xs.alias("xs:element", Element.class);
        xs.registerConverter(new ElementConverter(classMapper, "xs:element"));

        xs.alias("xs:complexType", ComplexType.class);
        xs.registerConverter(new ComplexTypeConverter(classMapper, "xs:complexType"));

        xs.alias("xs:attribute", Attribute.class);
        xs.registerConverter(new AttributeConverter(classMapper, "xs:attribute"));

        xs.alias("xs:sequence", Sequence.class);
        xs.registerConverter(new SequenceConverter(classMapper, "xs:sequence"));

        xs.alias("xs:simpleType", SimpleType.class);
        xs.registerConverter(new SimpleTypeConverter(classMapper, "xs:simpleType"));

        xs.alias("xs:restriction", Restriction.class);
        xs.registerConverter(new RestrictionConverter(classMapper, "xs:restriction"));

        xs.alias("xs:enumeration", Enumeration.class);
        xs.registerConverter(new EnumerationConverter(classMapper, "xs:enumeration"));

        xs.alias("xs:choice", Choice.class);
        xs.registerConverter(new ChoiceConverter(classMapper, "xs:choice"));

        xs.alias("xs:notation", Notation.class);
//        xs.registerConverter(new NotationConverter(classMapper, "xs:notation"));

        Object o = xs.fromXML(new FileReader(XMIInputConfigImpl.checkURL("dentaku-cartridge-generator/src/xml/jdo_2_0.xsd").getFile()));
        assertNotNull(o);
    }
}
