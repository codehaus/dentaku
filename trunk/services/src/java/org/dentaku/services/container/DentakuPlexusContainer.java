/*
 * DentakuPlexusContainer.java
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
package org.dentaku.services.container;

import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.codehaus.plexus.configuration.xml.xstream.PlexusTools;

import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

public class DentakuPlexusContainer extends DefaultPlexusContainer {
    private List configurationResources = null;

    public void setConfigurationResources(List configurationResources) {
        this.configurationResources = configurationResources;
    }

    protected void initializeConfiguration() throws Exception {
        super.initializeConfiguration();

        // this configuration is generated in the model and loaded from the classpath.  Since we can't load all configurations
        // in a certain class path, we need to have a single document that can point to all of the relevant documents that need
        // to be loaded.
        //InputStreamReader reader = new InputStreamReader(getClass().getConfigurationResource("/org/dentaku/conf/persistence/factory.xml"));
        if (configurationResources != null) {
            for (Iterator it = configurationResources.iterator(); it.hasNext();) {
                InputStreamReader config = (InputStreamReader) it.next();
                PlexusConfiguration conf =
                    PlexusTools.buildConfiguration( getInterpolationConfigurationReader( config ) );

                XmlPlexusConfiguration componentsConfiguration =
                    (XmlPlexusConfiguration) configuration.getChild( "components" );

                componentsConfiguration.addAllChildren( conf.getChild( "components" ) );

            }
        }

    }
}
