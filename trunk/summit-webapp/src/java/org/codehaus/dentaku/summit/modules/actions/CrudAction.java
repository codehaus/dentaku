/*
 * Created on Dec 7, 2004
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
package org.codehaus.dentaku.summit.modules.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.context.Context;
import org.codehaus.plexus.summit.rundata.RunData;


/**
 */
public class CrudAction {
    protected static Log Log = LogFactory.getLog(CrudAction.class);

    protected void log(String aMessage) {
        Log.debug(aMessage);
    }

    /**
 */
    protected static void logParameters(RunData aData, String aMessage) {
        Log.debug(aMessage + ": parameters are:" +
            aData.getParameters().toString());
    }

    /**
 */
    protected static void contextMessage(Context aContext, String aMessage) {
        aContext.put("errMessage", aMessage);
    }

    /**
 */
    protected static void handleException(Context aContext, String aHeader,
        String aMessage, Exception e) {
        Log.error(aHeader + ":" + aMessage);
        Log.error(e);
        contextMessage(aContext, aMessage);
    }
}
