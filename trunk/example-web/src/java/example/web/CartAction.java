/*
 * CartAction.java
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

import example.command.SubmitInvoiceEvent;
import example.command.SubmitInvoiceEventBase;
import example.entity.Address;
import example.entity.CreditCard;
import example.entity.Invoice;
import example.entity.LineItem;
import example.entity.SKU;
import example.entity.SKUFactory;
import example.web.form.CartForm;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.dentaku.foundation.connector.DirectConnector;
import org.dentaku.services.container.ContainerManager;
import org.dentaku.services.persistence.PersistenceManagerStorage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Iterator;

/**
 * @struts.action	name="cartForm" path="/cart"
 * scope="session"
 * validate="false"
 * unknown="false"
 * parameter="m"
 * @struts.action-forward name="basket" path="/cart/cart.jsp"
 * @struts.action-forward name="billing" path="/cart/checkout-billing.jsp"
 * @struts.action-forward name="shipping" path="/cart/checkout-shipping.jsp"
 * @struts.action-forward name="shippingMethods" path="/cart/checkout-shippingMethods.jsp"
 * @struts.action-forward name="confirm" path="/cart/checkout-confirm.jsp"
 * @struts.action-forward name="confirmation" path="/cart/checkout-confirmation.jsp"
 */
public class CartAction extends DispatchAction {
    /**
     * Add an item to the cart based on the information in the form.
     */
    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        CartForm cartForm = ((CartForm) form);

        HttpSession session = request.getSession();
        Invoice inv = (Invoice) session.getAttribute("invoice");
        if (inv == null) {
            inv = new Invoice();
            session.setAttribute("invoice", inv);
        }

        Long skuID = new Long(cartForm.getSku());
        LineItem item = null;
        for (Iterator it = inv.getLineItems().iterator(); it.hasNext();) {
            item = (LineItem) it.next();
            if (item.getId().equals(skuID)) {
                break;
            }
        }

        if (item == null) {
            // create and populate a new line item entity
            PersistenceManagerStorage pm = (PersistenceManagerStorage) ContainerManager.getInstance().getContainer().lookup(PersistenceManagerStorage.ROLE);
            SKUFactory skuFactory = (SKUFactory) pm.getPersistenceFactory(SKU.class.getName());
            SKU sku = (SKU) skuFactory.findByPrimaryKey(skuID);
            pm.releaseSession();

            item = new LineItem();
            item.setSku(sku);
            BeanUtils.copyProperties(item, sku);

            inv.getLineItems().add(item);
        }

        // we have a line item associated to a SKU, but don't have the quantity yet.  fill that out and calculate the other fields.
        item.setQuantity(item.getQuantity() + cartForm.getQuantity());
        item.calculate();

        inv.compute();

        return mapping.findForward("basket");
    }

    /**
     * Action to remove an item from the invoice, takes a URL parameter of 'item' with the SKU item id
     */
    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        Invoice inv = (Invoice) session.getAttribute("invoice");

        // update invoice
        Long skuID = new Long(request.getParameter("item"));
        for (Iterator it = inv.getLineItems().iterator(); it.hasNext();) {
            LineItem item = (LineItem) it.next();
            if (item.getSku().getId().equals(skuID)) {
                inv.getLineItems().remove(item);
                break;
            }
        }
        inv.compute();

        return mapping.findForward("basket");
    }

    /**
     * Set up for the checkout process
     * Display form to collect shipping info.
     * BRAIN DAMAGE: may want to reset the form here.
     */
    public ActionForward setShipping(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CartForm cartForm = ((CartForm) form);
        cartForm.setShippingAddress(new Address());

        return mapping.findForward("shipping");
    }

    /**
     * collect shipping Method info from UPS, based on what's in the cart.
     * Display form to let user choose which method
     */
    public ActionForward setShippingMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CartForm cartForm = ((CartForm) form);

        if (cartForm.getAction().equals("Back")) {
            return mapping.findForward("basket");
        } else {

            HttpSession session = request.getSession();
            Invoice inv = (Invoice) session.getAttribute("invoice");

            // add info from form to invoice
            inv.setShipAddress(cartForm.getShippingAddress());
            inv.compute();
            return setPaymentMethod(mapping, form, request, response);

//            // shipping methods and prices from UPS
//            UPSConnector ups = UPSConnectorUtil.getHome().create();
//            List results = ups.getPackageRatingList((LineItem [])inv.getLineItems().toArray(new LineItem[inv.getLineItems().size()]), cartForm.getShippingAddress().getPostalCode());
//            session.setAttribute("shippingResults", results);
//
//            return mapping.findForward("shippingMethods");
        }
    }

    /**
     * Display form to collect payment method and billing info (if applicable)
     */
    public ActionForward setPaymentMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CartForm cartForm = ((CartForm) form);
        HttpSession session = request.getSession();
        Invoice inv = (Invoice) session.getAttribute("invoice");

//        Collection shippingResults = (Collection) session.getAttribute("shippingResults");
//        for (Iterator iterator = shippingResults.iterator(); iterator.hasNext();) {
//            UPSConnectorBean.RatedPackage ratedPackage = (UPSConnectorBean.RatedPackage) iterator.next();
//
//            if (cartForm.getShippingMethodCode().equals(ratedPackage.getServiceCode())) {
//                ShippingMethod shipMethod = new ShippingMethodImpl();
//                shipMethod.setPrice(ratedPackage.getTotalCharges());
//                shipMethod.setDescription(ratedPackage.getServiceName());
//                inv.setShipMethod(shipMethod);
//                inv.compute();
//            }
//        }

        cartForm.setCreditCard(new CreditCard());
        cartForm.setBillingAddress(new Address());

        return mapping.findForward("billing");
    }

    /**
     * Validate and process the checkout view, set up for the confirm view.  We just go to the order review screen for final confirmation.
     * BRAIN DAMAGE:  This action needs to calculate tax and shipping amounts before going to the confirmation screen, something it is
     * not currently doing properly.
     */
    public ActionForward confirmOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = form.validate(mapping, request);
//        if (!errors.isEmpty()) {
//            saveErrors(request, errors);
//            return mapping.findForward("billing");
//        }

        CartForm cartForm = ((CartForm) form);
        HttpSession session = request.getSession();
        Invoice inv = (Invoice) session.getAttribute("invoice");

        inv.setCreditCard(cartForm.getCreditCard());

        // set billing address as appropriate
        if (!cartForm.isBillingSameAsShipping()) {
            inv.setBillAddress(inv.getShipAddress());
        } else {
            inv.setBillAddress(cartForm.getBillingAddress());
        }

        return mapping.findForward("confirm");
    }

    /**
     * Process the previous view, set up for the confirmation screen.  This places the order.
     */
    public ActionForward processOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = form.validate(mapping, request);
//        if (!errors.isEmpty()) {
//            // this should never happen since there are no fields on the confirmation page, it's just to handle hackers that are trying
//            // to circumvent the pages earlier
//            saveErrors(request, errors);
//            return mapping.findForward("confirm");
//        }
        // this does all the work in the database and returns a fully aggregated Invoice object if all of the payment and order
        // processing succeeds.  We should have complete shipping details from the shipper at this point.
        Invoice inv = (Invoice) request.getSession().getAttribute("invoice");

        SubmitInvoiceEventBase event = new SubmitInvoiceEvent();
        event.setInvoice(inv);
        DirectConnector.getInstance().fireEvent(event);
        request.getSession().setAttribute("invoice", inv);

        return mapping.findForward("confirmation");
    }
}
