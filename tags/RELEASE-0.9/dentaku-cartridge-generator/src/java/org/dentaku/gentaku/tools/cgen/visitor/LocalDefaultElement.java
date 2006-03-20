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
import java.util.Set;

/**
 * The class that is instantiated by the factory.  Really only does something with the accept method.
 */
public class LocalDefaultElement extends DefaultElement {
    private LocalDefaultElement annotation;
    private Generator generator;
    private Set locations;

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
     * @param location
     * @return
     */
    public boolean accept(PluginOutputVisitor visitor, Branch newParent, ModelElement parent, String location) throws GenerationException {
        return visitor.visit(this, newParent, parent, location);
    }

    public LocalDefaultElement getAnnotation() {
        return annotation;
    }

    public void setAnnotation(LocalDefaultElement annotation) {
        this.annotation = annotation;
    }

    /**
     * Used for GenGenPlugin, holds a live Generator object
     * @return
     */
    public Generator getGenerator() {
        return generator;
    }

    public void setGenerator(Generator generator) {
        this.generator = generator;
    }

    /**
     * Used for XMIGenTask, keeps a cache of strings of the locations that this element is valid
     * @return
     */
    public Set getLocations() {
        return locations;
    }

    public void setLocations(Set locations) {
        this.locations = locations;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (getPath().length() > 75) {
            sb.append("..." + getPath().substring(getPath().length() - 75));
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
