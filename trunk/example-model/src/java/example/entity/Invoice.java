/**
 * This class is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
/*
 * Invoice.java
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
package example.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @hibernate.class
 *       table="Invoice"
 */
public class Invoice
       extends InvoiceBase
{
    public Invoice() {
        setCcCharges(new LinkedList());
        setLineItems(new LinkedList());
    }

    // concrete business methods that were declared
    // abstract in class Invoice ...

    public void compute() {
        // subtotal
        double subTotal = 0.0;
        for (Iterator it = getLineItems().iterator(); it.hasNext();) {
            LineItem lineItem = (LineItem) it.next();
            subTotal += lineItem.getAmount().doubleValue();
        }
        double total = subTotal;

        // taxes
        Address shipAddress = getShipAddress();
        if (shipAddress != null && shipAddress.getState() != null && shipAddress.getState().equals("NY")) {
            double taxTotal = .0825 * subTotal;
            this.taxTotal = new BigDecimal(taxTotal).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            total += taxTotal;
        }

        // shipping
        if (getShippingMethod() != null) {
            total += getShippingMethod().getPrice().doubleValue();
        }
        this.subTotal = new BigDecimal(subTotal).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        this.total = new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

}
