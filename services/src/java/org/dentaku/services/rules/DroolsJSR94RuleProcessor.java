/*
 * DroolsJSR94RuleProcessor.java
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

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.util.DirectoryScanner;
import org.drools.io.RuleSetReader;
import org.drools.rule.RuleSet;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.RuleSession;
import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * Implementation of a JSR-94 Rules processor with Drools
 *
 * @todo this is a poor implementation WRT container neutrality.  The ideal setup would be to have URLs from getResource
 * managing the location of the configuration rules.  That way, we don't have to change configuraions when we redeploy.
 */
public class DroolsJSR94RuleProcessor implements JSR94RuleProcessor, Initializable {
    /** Drools <code>RuleServiceProvider</code> URI. */
    public static final String RULE_SERVICE_PROVIDER = "http://drools.org/";

    protected RuleServiceProvider ruleServiceProvider;
    private RuleRuntime ruleRuntime;

    // configuration parameters
    String ruleFilesDirectory;
    String ruleFilesIncludes;
    String bindUri;

    public void initialize() throws Exception {
        RuleServiceProviderManager.registerRuleServiceProvider( RULE_SERVICE_PROVIDER, org.drools.jsr94.rules.RuleServiceProviderImpl.class );
        ruleServiceProvider = RuleServiceProviderManager.getRuleServiceProvider( RULE_SERVICE_PROVIDER );

        RuleAdministrator ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
        LocalRuleExecutionSetProvider ruleSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(null);
        ruleRuntime = ruleServiceProvider.getRuleRuntime();
 
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir( ruleFilesDirectory );
        scanner.setIncludes( new String[]{ ruleFilesIncludes } );
        scanner.scan();

        String[] fileNames = scanner.getIncludedFiles();
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            // read rules and register with administrator
            InputStream resourceAsStream = this.getClass().getResourceAsStream(fileName);
            Reader ruleReader = new InputStreamReader(resourceAsStream);
            RuleExecutionSet ruleSet = ruleSetProvider.createRuleExecutionSet(ruleReader, null);
            ruleAdministrator.registerRuleExecutionSet(bindUri, ruleSet, null);
        }
    }

    public StatefulRuleSession getStatefulRuleSession() throws Exception {
        // obtain the stateless rule session
        return (StatefulRuleSession)ruleRuntime.createRuleSession(bindUri, null, RuleRuntime.STATEFUL_SESSION_TYPE);
    }

    public StatelessRuleSession getStatelessRuleSession() throws Exception {
        // obtain the stateless rule session
        return (StatelessRuleSession)ruleRuntime.createRuleSession(bindUri, null, RuleRuntime.STATELESS_SESSION_TYPE);
    }

    public void releaseRuleSession(RuleSession session) throws Exception {
        session.release();
    }

    /**
     * Get the requested resource from the ClassLoader.
     *
     * @see ClassLoader#getResource
     */
    protected URL getResource( String res )
    {
        return getClass().getClassLoader().getResource( res );
    }

    /**
     * Get the requested resource from the ClassLoader.
     *
     * @see ClassLoader#getResourceAsStream
     */
    protected InputStream getResourceAsStream( String res )
    {
        return getClass().getClassLoader().getResourceAsStream( res );
    }
}

