/*
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
 * 
 * Created on Dec 7, 2004
 *
 * Copyright STPenable Ltd. (c) 2004
 * 
 */
package org.codehaus.dentaku.summit.actions;

import java.util.HashMap;

import org.apache.velocity.context.Context;
import org.codehaus.plexus.action.Action;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.summit.rundata.RunData;
import org.codehaus.plexus.summit.activity.DefaultActionEvent;

/**
 */
public class CrudAction extends DefaultActionEvent 
 {
	private String preAction;
	private String postAction;
	
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
	/**
	 * @return Returns the postAction.
	 */
	public String getPostAction() {
		return postAction;
	}
	/**
	 * @param postAction The postAction to set.
	 */
	public void setPostAction(String postAction) {
		this.postAction = postAction;
	}
	/**
	 * @return Returns the preAction.
	 */
	public String getPreAction() {
		return preAction;
	}
	/**
	 * @param preAction The preAction to set.
	 */
	public void setPreAction(String preAction) {
		this.preAction = preAction;
	}
	protected void doPreAction(RunData aData, Context aContext) {
		if(preAction!=null) {
			HashMap aMap = new HashMap();
			try {
				// Get the component and run it
				Action aAction = (Action)aData.lookup(Action.ROLE, preAction);
				aMap.put("data", aData);
				aMap.put("context", aContext);
				aAction.execute(aMap);
			} catch (ComponentLookupException e) {
				// ignore it, as the action does not exist 
			} catch (Exception e) {
				// Report error
				aContext.put("errMessage", "preAction Groovy script failed with this exception " + e.getMessage());
			}
		}
	}
	protected void doPostAction(RunData aData, Context aContext) {
		if(postAction!=null) {
			HashMap aMap = new HashMap();
			try {
				// Get the component and run it
				Action aAction = (Action)aData.lookup(Action.ROLE, postAction);
				aMap.put("data", aData);
				aMap.put("context", aContext);
				aAction.execute(aMap);
			} catch (ComponentLookupException e) {
				// ignore it, as the action does not exist 
			} catch (Exception e) {
				// Report error
				aContext.put("errMessage", "postAction Groovy script failed with this exception " + e.getMessage());
			}
		}
	}
}
