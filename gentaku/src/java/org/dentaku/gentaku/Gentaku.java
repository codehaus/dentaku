/*
 * Gentaku.java
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
package org.dentaku.gentaku;

import org.generama.Generama;
import org.picocontainer.MutablePicoContainer;
import org.dentaku.services.metadata.JMIUMLMetadataProvider;

/**
 * This class installs the core Gentaku components in a <a href="http://www.picocontainer.org/">PicoContainer</a>.
 * (It relies on the superclass to composeContainer the rest).
 *
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class Gentaku extends Generama {

    public Gentaku(Class writerMapperClass) {
        super(JMIUMLMetadataProvider.class, writerMapperClass);
    }

    public void composeContainer(MutablePicoContainer pico, Object scope) {
        super.composeContainer(pico, scope);
    }
}
