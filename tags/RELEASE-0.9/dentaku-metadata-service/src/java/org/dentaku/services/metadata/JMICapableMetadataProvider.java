/*
 * JMICapableMetadataProvider.java
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

import java.util.Collection;

import org.generama.MetadataProvider;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.Classifier;

public interface JMICapableMetadataProvider extends MetadataProvider {
    public static final String ROLE = JMICapableMetadataProvider.class.getName();

    public Classifier mapObjectToClassifier(Object qdox);
    public UmlPackage getModel() throws RepositoryException;

    public Collection getJMIMetadata();
}
