/*
 * AdminAction.java
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

package example.web;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @struts.action	name="adminForm"
 *                      path="/admin/admin"
 * 						scope="session"
 * 						validate="false"
 * 						unknown="false"
 *                      parameter="m"
 * @struts.action-forward name="viewTable" path="/admin/viewTable.jsp"
 */
public class AdminAction extends DispatchAction {
    public ActionForward viewItem(ActionMapping mapping,
                                  ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response)
            throws Exception {

        String className = request.getParameter("class");
        String column = request.getParameter("col");
        Long id = Long.decode(request.getParameter("id"));
        //////////////////////////////
        // do some error checking here
        //////////////////////////////
        String query = "select obj." + column + ".elements from obj in class " + className + " where obj.id=" + id.toString();
//        Scroller s = ScrollerUtil.getHome().create(query);
//        request.getSession().setAttribute("scroller", s);

        return mapping.findForward("viewTable");
    }

    /**
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception Exception if an exception occurs
     */
    public ActionForward viewTable(ActionMapping mapping,
                                   ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
            throws Exception {

        String className = request.getParameter("class");

        //////////////////////////////
        // do some error checking here
        //////////////////////////////

        // this query should be more like the OSUser query with parameterized and aliased Class object
        String query = null;

        String idStr = request.getParameter("id");
        String column = request.getParameter("col");
        query = "from " + className;
        if (idStr != null && column != null) {
            try {
                Long id = Long.decode(idStr);
                query = "select obj." + column + ".elements from obj in class " + className + " where obj.id=" + id.toString();
            } catch (Exception e) {
            }
        } else {
            query = "select obj from obj in class " + className;
        }

//        Scroller s = ScrollerUtil.getHome().create(query);
//        request.getSession().setAttribute("scroller", s);

        return mapping.findForward("viewTable");
    }

    public ActionForward browse(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        ScrollerBean scroller = (ScrollerBean) request.getSession().getAttribute("scroller");
//        if (scroller == null) {
//            return mapping.findForward("listSearch");
//        }

        if (request.getParameter("pager.offset") != null) {
            try {
                int offset = Integer.parseInt(request.getParameter("pager.offset"));
//                scroller.setFirstResult(offset);
            } catch (NumberFormatException e) {
            }
        }
        return mapping.findForward("mailBrowser");
    }


}