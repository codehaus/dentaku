/*
 * WebUserContext.java
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
package example.web.global;


import javax.servlet.http.HttpServletRequest;

/**
 * @author topping
 * @version $Revision$
 */
public class WebUserContext {
    public static final String WEB_USER_CONTEXT = "WEB_USER_CONTEXT";
//    private UserFullValue userFullValue;
//
//    public UserFullValue getUserFullValue() {
//        return userFullValue;
//    }
//
//    public void setUserFullValue(UserFullValue userFullValue) {
//        this.userFullValue = userFullValue;
//    }

    public static WebUserContext getUserContext(HttpServletRequest request) {
        WebUserContext webUserContext = (WebUserContext) request.getSession().getAttribute(WEB_USER_CONTEXT);
        if (webUserContext == null) {
            webUserContext = new WebUserContext();
            request.getSession().setAttribute(WEB_USER_CONTEXT, webUserContext);
        }
        return webUserContext;
    }
}
