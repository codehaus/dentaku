<%@ page import="java.util.ArrayList,
                 java.util.Iterator,
                 java.math.BigDecimal,
                 java.util.Collection,
                 example.entity.Invoice,
                 example.entity.LineItem"%>


<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" %>

<tiles:insert page="/tiles/mainTile.jsp">
    <tiles:put name="title" value="Checkout"/>

    <!-- order summary -->
    <tiles:put name="lowerLeftNavTile" direct="true">
        <table border="0" cellpadding="0" cellspacing="4" bgcolor="#FFFFFF">
            <tr>
                <td class="orderSummaryTitle" align="center" colspan="3">Order Summary</td>
            </tr>
            <tr>
                <td colspan="3"><hr width="75%" size="1" color="#000000"></td>
            </tr>
            <%
                Invoice inv = (Invoice)request.getSession().getAttribute("invoice");
                double subTotal = 0.0;

                for (Iterator it = inv.getLineItems().iterator(); it.hasNext();) {
                    LineItem lineItem = (LineItem) it.next();
                    subTotal += lineItem.getAmount().doubleValue();
            %>

            <tr>
                <td class="orderSummaryText" align="center" valign="top"><%=(int)lineItem.getQuantity()%></td>
                <td class="orderSummaryText" width="100%"><%=lineItem.getSku().getName()%></td>
                <td class="orderSummaryText" align="right" valign="top">$<%=lineItem.getAmount()%></td>
            </tr>

            <%
                }
            %>

            <tr>
                <td colspan="3"><img src="/images/spacer.gif" height="5" width="1"></td>
            </tr>
            <tr>
                <td class="orderSummaryText">&nbsp</td>
                <td class="orderSummaryTextBold" width="100%">Subtotal</td>
                <td class="orderSummaryTextBold" align="right" valign="top">$<%=inv.getSubTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString() %></td>
            </tr>
            <tr>
                <td colspan="3"><hr width="75%" size="1" color="#000000"></td>
            </tr>
            <tr>
                <td class="orderSummaryText" colspan="3">
                    <span class="orderSummaryTextBold">Ship to:</span><br>
                    <%=inv.getShipAddress().getName()%><br>
                    <%=inv.getShipAddress().getAddress1()%><br>
                    <%
                        if (inv.getShipAddress().getAddress2() != null && !inv.getShipAddress().getAddress2().equals("")) {
                            out.write(inv.getShipAddress().getAddress2()+"<br>");
                        }
                    %>
                    <%=inv.getShipAddress().getCity()%>, <%=inv.getShipAddress().getState()%> <%=inv.getShipAddress().getPostalCode()%>
                </td>
            </tr>
            <tr>
                <td colspan="3"><img src="/images/spacer.gif" height="5" width="1"></td>
            </tr>
        </table>
    </tiles:put>

    <tiles:put name="mainTile" direct="true">
       <table width="700" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <html:form action='/cart.do?m=setPaymentMethod'>
                <td>
                    <table border="0" cellspacing="2" cellpadding="2">
                        <tr>
                            <td class="bodytitle" colspan="2" valign="bottom">Shipping Method</td>
                            <td class="bodytitle" align="center">Business<br>Days</td>
                            <td class="bodytitle" align="center" valign="bottom">Price</td>
                        </tr>
                        <!-- loop over all of the shipping methods -->
                        <%
                            Collection shippingResults = (Collection)session.getAttribute("shippingResults");
                            for (Iterator iterator = shippingResults.iterator(); iterator.hasNext();) {
                                UPSConnectorBean.RatedPackage ratedPackage = (UPSConnectorBean.RatedPackage) iterator.next();
                        %>
                        <tr>
                            <td align="center"><html:radio property="shippingMethodCode" value="<%=ratedPackage.getServiceCode() %>"/></td>
                            <td class="bodytext" align="left"><%=ratedPackage.getServiceName()%></td>
                            <td class="bodytext" align="center"><%=ratedPackage.getDaysToDelivery()%></td>
                            <td class="bodytext" align="right"><%=ratedPackage.getTotalCharges()%></td>
                        </tr>
                        <% } %>
                        <tr>
                            <td colspan="4" align="center"><br><input type="submit" value="Continue"></td>
                        </tr>
                    </table>
                </td>
                </html:form>
            </tr>
        </table>
    </tiles:put>
</tiles:insert>




