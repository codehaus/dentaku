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

import org.generama.MetadataProvider;
import org.generama.GeneramaException;
import org.omg.uml.UmlPackage;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TaggedValue;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.jmi.model.MofPackage;
import javax.jmi.model.ModelPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.jmi.reflect.RefPackage;
import java.util.Collection;
import java.util.Iterator;
import java.net.URL;
import java.io.IOException;

import com.thoughtworks.qdox.model.DocletTagFactory;

public class JMIUMLMetadataProvider extends JMIMetadataProviderBase {

    public JMIUMLMetadataProvider() {
//        throw new RuntimeException("Configuration Problem: is your RepositoryReader registered?");
    }

    public JMIUMLMetadataProvider(RepositoryReader reader) {
        super(reader);
    }

    public Collection getMetadata() throws GeneramaException {
        try {
            return getModel().getCore().getModelElement().refAllOfType();
        } catch (RepositoryException e) {
            throw new GeneramaException("problem getting metadata", e);
        }
    }
}
