/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
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
