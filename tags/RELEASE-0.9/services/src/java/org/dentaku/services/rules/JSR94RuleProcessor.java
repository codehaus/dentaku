/*
 * JSR94RuleProcessor.java
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
package org.dentaku.services.rules;

import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;
import javax.rules.RuleSession;

public interface JSR94RuleProcessor {
    public static final String ROLE = JSR94RuleProcessor.class.getName();

    StatefulRuleSession getStatefulRuleSession() throws Exception;
    StatelessRuleSession getStatelessRuleSession() throws Exception;
    void releaseRuleSession(RuleSession session) throws Exception;

}
