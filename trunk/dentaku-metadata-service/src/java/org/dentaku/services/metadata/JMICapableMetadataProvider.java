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

import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.UmlPackage;
import org.generama.MetadataProvider;
import com.thoughtworks.qdox.model.AbstractJavaEntity;

import java.util.Collection;

public interface JMICapableMetadataProvider extends MetadataProvider {
    public Classifier mapQDoxToClassifier(AbstractJavaEntity qdox);
    public UmlPackage getModel() throws RepositoryException;

    public Collection getJMIMetadata();
}
