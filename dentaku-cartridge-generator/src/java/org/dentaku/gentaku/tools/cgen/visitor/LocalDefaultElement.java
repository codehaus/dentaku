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

import org.dom4j.tree.DefaultElement;
import org.dom4j.QName;
import org.dom4j.Branch;
import org.dom4j.Attribute;
import org.dentaku.gentaku.cartridge.Generator;
import org.dentaku.gentaku.cartridge.GenerationException;
import org.omg.uml.foundation.core.ModelElement;

import java.util.Iterator;

/**
 * The class that is instantiated by the factory.  Really only does something with the accept method.
 */
public class LocalDefaultElement extends DefaultElement {
    private LocalDefaultElement annotation;
    private Generator generator;
    public LocalDefaultElement(QName qname) {
        super(qname);
    }

    /**
     * Basic accept method, adding a couple of state parameters we need and *not* calling all the childen of a node as the
     * defualt impl does.  We want to be discriminate about how we do that and update state parameters for children (using the
     * stack in the process for recursive state)
     * @param visitor
     * @param newParent
     * @param parent
     * @return
     */
    public boolean accept(PluginOutputVisitor visitor, Branch newParent, ModelElement parent) throws GenerationException {
        return visitor.visit(this, newParent, parent);
    }

    public LocalDefaultElement getAnnotation() {
        return annotation;
    }

    public void setAnnotation(LocalDefaultElement annotation) {
        this.annotation = annotation;
    }

    public Generator getGenerator() {
        return generator;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (getPath().length() > 75) {
            sb.append("..." + getPath().substring(getPath().length() - 50));
        } else {
            sb.append(getPath());
        }
        for (Iterator attrIter = attributes().iterator(); attrIter.hasNext();) {
            Attribute attribute = (Attribute) attrIter.next();
            sb.append("["+attribute.getName()+"="+attribute.getValue()+"] ");
        }
        return sb.toString();
    }
}
