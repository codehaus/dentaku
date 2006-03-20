/*
 *  DentakuRequestProcessor.java
 *  Copyright 2004 Brian Topping
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dentaku.foundation.ui.struts;

import org.apache.struts.action.RequestProcessor;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.codehaus.plexus.embed.Embedder;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public class DentakuRequestProcessor extends RequestProcessor {
    protected Embedder container;

    public void init(ActionServlet actionServlet, ModuleConfig moduleConfig) throws ServletException {
        super.init(actionServlet, moduleConfig);
        container = new Embedder();
        try {
            container.start();
        } catch (Exception e) {
            throw new ServletException("could not start container", e);
        }
    }

    protected Action processActionCreate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ActionMapping actionMapping) throws IOException {
        try {
            return (Action) container.lookup(ActionComponent.ROLE, actionMapping.getType());
        } catch (ComponentLookupException e) {
            log.error(getInternal().getMessage("actionCreate", actionMapping.getPath()), e);
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, getInternal().getMessage("actionCreate", actionMapping.getPath()));
            return (null);
        }
    }
}
