/*
 * CartForm.java
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

package example.web.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

import example.entity.CreditCard;
import example.entity.Address;

/**
 * @struts.form name="cartForm"
 * @author topping
 * @version $Revision$
 */
public class CartForm extends ActionForm {
    private int page;
    private String action;
    private int quantity;
    private String sku;
    private Address billingAddress;
    private Address shippingAddress;
    private String email;
    private CreditCard creditCard;
//    private String cardOwnerName;
//    private int cardType;
//    private String cardNumber;
//    private int expMonth;
//    private int expYear;
//    private String CVV;
    private String shippingMethodCode;
    private boolean billingSameAsShipping = true;

    // page: which page in the cart flow are we on
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    // action: which button was clicked, in case of multiple buttons
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    // quantity: quantity of product being added to cart
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // sku: sku of proiduct being added to cart
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        billingSameAsShipping = false;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public boolean isBillingSameAsShipping() {
        return billingSameAsShipping;
    }

    public void setBillingSameAsShipping(boolean billingSameAsShipping) {
        this.billingSameAsShipping = billingSameAsShipping;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShippingMethodCode() {
        return shippingMethodCode;
    }

    public void setShippingMethodCode(String shippingMethodCode) {
        this.shippingMethodCode = shippingMethodCode;
    }

    // credit card related stuff

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

}
