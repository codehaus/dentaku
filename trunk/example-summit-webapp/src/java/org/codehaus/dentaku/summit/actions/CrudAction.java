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
package org.codehaus.dentaku.summit.actions;

import java.io.IOException;

import org.apache.velocity.context.Context;
import org.codehaus.plexus.summit.rundata.RunData;
import org.codehaus.plexus.summit.exception.SummitException;
import org.codehaus.plexus.summit.pipeline.valve.AbstractValve;

/**
 */
public class CrudAction extends AbstractValve
 {

    protected void log(String aMessage) {
        getLogger().debug(aMessage);
    }

    /**
 */
    protected void logParameters(RunData aData, String aMessage) {
        getLogger().debug(aMessage + ": parameters are:" +
            aData.getParameters().toString());
    }

    /**
 */
    protected void contextMessage(Context aContext, String aMessage) {
        aContext.put("errMessage", aMessage);
    }

    /**
 */
    protected void handleException(Context aContext, String aHeader,
        String aMessage, Exception e) {
        getLogger().error(aHeader + ":" + aMessage, e);
        contextMessage(aContext, aMessage);
    }

	/* (non-Javadoc)
	 * @see org.codehaus.plexus.summit.pipeline.valve.AbstractValve#invoke(org.codehaus.plexus.summit.rundata.RunData)
	 */
	public void invoke(RunData arg0) throws IOException, SummitException {
		// TODO Auto-generated method stub
		
	}
}
