/*
 * EventValve.java
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
package org.dentaku.foundation.event;

import org.dentaku.services.rules.JSR94RuleProcessor;
import org.dentaku.foundation.pipeline.Valve;
import org.codehaus.werkflow.Wfms;
import org.codehaus.werkflow.SimpleAttributes;
import org.codehaus.werkflow.definition.ProcessDefinition;
import org.codehaus.werkflow.personality.Personality;
import org.codehaus.werkflow.personality.basic.BasicPersonality;
import org.codehaus.werkflow.engine.WorkflowEngine;
import org.codehaus.werkflow.service.SimpleWfmsServices;
import org.codehaus.werkflow.service.persistence.fleeting.FleetingPersistenceManager;
import org.codehaus.werkflow.service.messaging.simple.SimpleMessagingManager;

import javax.rules.StatelessRuleSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.URL;

public class EventValve implements Valve {
    public boolean execute(AbstractEvent event) throws Exception {
        return event.execute(null);
    }
}
