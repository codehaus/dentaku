/*
 *  Generator.java
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
package org.dentaku.gentaku.cartridge;

import org.dentaku.gentaku.tools.cgen.visitor.LocalDefaultElement;
import org.dom4j.Branch;
import org.dom4j.Element;
import org.dom4j.Document;
import org.omg.uml.foundation.core.ModelElement;
import org.netbeans.jmiimpl.omg.uml.modelmanagement.ModelImpl;

public interface Generator {
    void preProcessModel(ModelImpl model) throws GenerationException;

    void preGenerate(LocalDefaultElement mappingNode, Branch parentOutput, ModelElement modelElement) throws GenerationException;
    boolean generate(LocalDefaultElement mappingNode, Branch parentOutput, ModelElement modelElement) throws GenerationException;
    void postGenerate(LocalDefaultElement mappingNode, Branch parentOutput, ModelElement modelElement, Element outputElement) throws GenerationException;

    void postProcessModel(ModelImpl model) throws GenerationException;

    void touchupOutputDocument(Document outputDocument) throws GenerationException;
}