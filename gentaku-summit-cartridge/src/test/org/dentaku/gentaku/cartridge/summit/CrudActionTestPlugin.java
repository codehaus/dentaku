/*
 * Created on Nov 29, 2004
 *
 * Copyright STPenable Ltd. (c) 2004
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

package org.dentaku.gentaku.cartridge.summit;

import java.io.File;
import java.util.Collection;

import org.dentaku.gentaku.cartridge.JavaPluginBase;
import org.dentaku.services.metadata.JMICapableMetadataProvider;
import org.dentaku.services.metadata.JMIUMLMetadataProvider;
import org.dentaku.services.metadata.RepositoryReader;
import org.dentaku.services.metadata.Utils;
import org.dentaku.services.metadata.nbmdr.MagicDrawRepositoryReader;
import org.generama.TemplateEngine;
import org.generama.VelocityTemplateEngine;
import org.generama.WriterMapper;

/**
 * @author David Wynter
 *
 * Add comment here
 */
public class CrudActionTestPlugin extends JavaPluginBase {
    /**
	 * @author David Wynter
	 *
	 * Add comment here
	 *
	 * @param templateEngine
	 * @param metadataProvider
	 * @param writerMapper
	 */
	public CrudActionTestPlugin(TemplateEngine templateEngine, JMICapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
		super(templateEngine, metadataProvider, writerMapper);
        setStereotype("Entity");
		this.templateEngine = templateEngine;
		this.writerMapper = writerMapper;
	}

    public CrudActionPlugin plugin;
    private TemplateEngine templateEngine;
    private WriterMapper writerMapper;

    protected void setUp() throws Exception {
        String filename = "summit-cartridge/src/uml/SummitTestModel.xml.zip";
        RepositoryReader rr = new MagicDrawRepositoryReader(Utils.checkURL(new File(Utils.getRootDir(), filename).toURL()));
        JMICapableMetadataProvider mp = new JMIUMLMetadataProvider(rr);
        plugin = new CrudActionPlugin((VelocityTemplateEngine)templateEngine, mp, writerMapper);
    }

    public void testSomething() throws Exception {
        plugin.start();
    }
    public Collection getMetadata() {
        return ((JMICapableMetadataProvider)metadataProvider).getJMIMetadata();
    }
}
