/*
 * WerkflowHelper.java
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
package org.dentaku.foundation.workflow;

import org.codehaus.werkflow.SimpleAttributes;
import org.codehaus.werkflow.Wfms;
import org.codehaus.werkflow.definition.ProcessDefinition;
import org.codehaus.werkflow.engine.WorkflowEngine;
import org.codehaus.werkflow.personality.Personality;
import org.codehaus.werkflow.personality.basic.BasicPersonality;
import org.codehaus.werkflow.service.SimpleWfmsServices;
import org.codehaus.werkflow.service.messaging.simple.SimpleMessagingManager;
import org.codehaus.werkflow.service.persistence.fleeting.FleetingPersistenceManager;
import org.dentaku.foundation.event.WerkflowEvent;

import java.net.URL;
import java.util.HashMap;

public class WerkflowHelper {
    private static WerkflowHelper instance = new WerkflowHelper();

    public static WerkflowHelper getInstance() {
        return instance;
    }

    private WerkflowHelper() {
    }
    private Wfms wfms;
    private HashMap workflows = new HashMap();

    public Wfms getWfms()
    {
        if ( this.wfms == null )
        {
            SimpleWfmsServices services = new SimpleWfmsServices();

            services.setMessagingManager( new SimpleMessagingManager() );
            services.setPersistenceManager( new FleetingPersistenceManager() );

            this.wfms = new WorkflowEngine( services );
        }

        return this.wfms;
    }

    public void deploy(URL url)
        throws Exception
    {
        if (workflows.get(url) == null) {
            Personality personality = BasicPersonality.getInstance();

            ProcessDefinition[] processDefs = personality.load( url );

            for ( int i = 0 ; i < processDefs.length ; ++i )
            {
                getWfms().getAdmin().deployProcess( processDefs[i] );
            }

            workflows.put(url, null);
        }
    }

    public void dispatch(WerkflowEvent event) throws Exception {
        deploy(event.getWorkflowURL());
        SimpleAttributes attrs = new SimpleAttributes();

        attrs.setAttribute( "event", event );
        getWfms().getRuntime().callProcess("", "EventDispatch", attrs);
    }
}
