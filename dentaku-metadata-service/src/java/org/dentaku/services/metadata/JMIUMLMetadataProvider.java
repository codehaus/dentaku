/*
 * JMIUMLMetadataProvider.java
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
package org.dentaku.services.metadata;

import org.generama.GeneramaException;
import org.omg.uml.foundation.core.Classifier;

import java.util.Collection;

public class JMIUMLMetadataProvider extends JMIMetadataProviderBase {

    public JMIUMLMetadataProvider() {
//        throw new RuntimeException("Configuration Problem: is your RepositoryReader registered?");
    }

    public JMIUMLMetadataProvider(RepositoryReader reader) {
        super(reader);
    }

    public Collection getJMIMetadata() throws GeneramaException {
        try {
            return getModel().getCore().getModelElement().refAllOfType();
        } catch (RepositoryException e) {
            throw new GeneramaException("problem getting metadata", e);
        }
    }

    /**
     * This really belongs as a mixin in a superclass interface or as a
     * @param qdox
     * @return
     */
    public Classifier mapObjectToClassifier(Object qdox) {
        return null;
    }
}
