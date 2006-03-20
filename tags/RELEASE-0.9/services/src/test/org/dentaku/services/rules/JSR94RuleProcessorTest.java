/*
 * JSR94RuleProcessorTest.java
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

import org.dentaku.services.ServiceTestBase;

import javax.rules.StatelessRuleSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Test for creditcard.CardProcessor services
 */
public class JSR94RuleProcessorTest extends ServiceTestBase {
    private StatelessRuleSession statelessSession;
    private JSR94RuleProcessor rp;

    protected void setUp() throws Exception {
        super.setUp();
        rp = (JSR94RuleProcessor)container.lookup(JSR94RuleProcessor.ROLE);
        assertNotNull(rp);
        statelessSession = rp.getStatelessRuleSession();
        assertNotNull(statelessSession);
    }

    public void tearDown() throws Exception {
        rp.releaseRuleSession(statelessSession);
        super.tearDown();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void testRuleProcessor() throws Exception {
        List inlist = new ArrayList();
        inlist.add(new RuleParameter());
        List outList = statelessSession.executeRules( inlist );
    }
}