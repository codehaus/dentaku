/*
 * DroolsNativeRuleProcessorTest.java
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
import org.drools.WorkingMemory;

/**
 * Test for creditcard.CardProcessor services
 */
public class DroolsNativeRuleProcessorTest extends ServiceTestBase {
    private DroolsNativeRulesProcessor rp;

    protected void setUp() throws Exception {
        super.setUp();
        rp = (DroolsNativeRulesProcessor)container.lookup(DroolsNativeRulesProcessor.ROLE);
        assertNotNull(rp);
    }

    public void tearDown() throws Exception {
        rp = null;
        super.tearDown();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void testRuleProcessor() throws Exception {
        final WorkingMemory workingMemory = rp.getRuleBase("TestRule").newWorkingMemory();
        workingMemory.assertObject(new RuleParameter());
        workingMemory.fireAllRules();
    }
}