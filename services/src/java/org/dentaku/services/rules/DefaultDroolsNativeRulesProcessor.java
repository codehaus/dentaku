/*
 * DefaultDroolsNativeRulesProcessor.java
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

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.util.DirectoryScanner;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseBuilder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class DefaultDroolsNativeRulesProcessor
        implements DroolsNativeRulesProcessor, Initializable, LogEnabled {
    private static Logger log;
    /* The store that keeps a reference to all ruleBases */
    private HashMap ruleBaseStore;
    String ruleFilesDirectory;
    String ruleFilesIncludes;

    public void enableLogging(Logger logger) {
        log = logger;
    }
    // ----------------------------------------------------------------------
    // Lifecylce Management
    // ----------------------------------------------------------------------

    public void initialize() throws Exception {
        String identifier = null;

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir( ruleFilesDirectory );
        scanner.setIncludes( new String[]{ ruleFilesIncludes } );
        scanner.scan();

        String[] fileNames = scanner.getIncludedFiles();
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            // read rules and register with administrator
            // save way, expect filename including .drl extension,
            // but in case not, then add it
            if (fileName.indexOf(".drl") > 0) {
                identifier = fileName.substring(0, fileName.length() - 4);
            } else {
                identifier = fileName;
                fileName = fileName + ".drl";
            }
            identifier = identifier.substring(identifier.lastIndexOf("/")+1);
            log.debug("Found rulebase filename " + fileName + ", identified as " + identifier);

            InputStream resourceAsStream = this.getClass().getResourceAsStream(fileName);

            try {
                RuleBase ruleBase = RuleBaseBuilder.buildFromInputStream(resourceAsStream);
                setRuleBase(identifier, ruleBase);
            } catch (Exception e) {
                log.error("RuleBase " + identifier + " could not be loaded", e);
                setRuleBase(identifier, null);
            }
        }
    }

    /*
     * Makes a working memory available from this rulebase
     */
    public WorkingMemory getWorkingMemory(String ruleBaseIdentifier)
            throws Exception {
        log.debug("Create working memory for rulebase " + ruleBaseIdentifier);
        RuleBase ruleBase = getRuleBase(ruleBaseIdentifier);
        WorkingMemory workingMemory = ruleBase.newWorkingMemory();
        return workingMemory;
    }


    public RuleBase getRuleBase(String identifier) {
        return (RuleBase) getRuleBaseStore().get(identifier);
    }

    public void setRuleBase(String identifier, RuleBase ruleBase) {
        getRuleBaseStore().put(identifier, ruleBase);
    }

    public HashMap getRuleBaseStore() {
        if (this.ruleBaseStore == null) {
            this.ruleBaseStore = new HashMap();
        }
        return this.ruleBaseStore;
    }

}