/*
 * GentakuTask.java
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
package org.dentaku.gentaku.ant;

import org.dentaku.gentaku.Gentaku;
import org.dentaku.services.metadata.MagicDrawRepositoryReader;
import org.generama.Generama;
import org.generama.ant.AbstractGeneramaTask;
import org.generama.defaults.FileWriterMapper;
import org.picocontainer.MutablePicoContainer;

import java.io.File;
import java.net.URL;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class GentakuTask extends AbstractGeneramaTask {
    private String encoding = System.getProperty("file.encoding");
    private URL modelURL;
    protected Generama createGenerama() {
        return new Gentaku(FileWriterMapper.class) {
            public void composeContainer(MutablePicoContainer pico, Object scope) {
                super.composeContainer(pico, scope);
                pico.registerComponentInstance(getProject());
                pico.registerComponentInstance(encoding);
                pico.registerComponentInstance(modelURL);
                pico.registerComponentImplementation(MagicDrawRepositoryReader.class);
            }
        };
    }

    public void setModel(String model) throws Exception {
        this.modelURL = new File(model).toURL();
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
