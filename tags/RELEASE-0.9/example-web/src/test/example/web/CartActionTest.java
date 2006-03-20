/*
 * CartActionTest.java
 * Copyright 2004-2004 Bill2, Inc.
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

import example.entity.Invoice;

public class CartActionTest extends TestBase {

    public void testAddRemove () {
        int size = 0;
        Invoice inv = ((Invoice)getSession().getAttribute("invoice"));
        if (inv != null) {
            size = inv.getLineItems().size();
        }
        setRequestPathInfo("/cart");
        addRequestParameter("sku", "1");
        addRequestParameter("quantity", "1");
        addRequestParameter("m", "add");
        actionPerform();

        // confirm that there was an item added to the basket
        inv = ((Invoice)getSession().getAttribute("invoice"));
        assertEquals("inv size", inv.getLineItems().size(), size+1);
        verifyForward("basket");

        // now, with the context filled out, text remove
        setRequestPathInfo("/cart");
        addRequestParameter("item", "1");
        addRequestParameter("m", "remove");
        actionPerform();

        inv = ((Invoice)getSession().getAttribute("invoice"));
        assertEquals("inv size", inv.getLineItems().size(), size);
        verifyForward("basket");

    }
}
