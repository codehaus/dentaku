<%@ page import="java.util.ArrayList,
                 java.util.Iterator,
                 java.math.BigDecimal,
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
                <td colspan="3"><img src="/images/spacer.gif" height="5" width="1"></td>
            </tr>
        </table>
    </tiles:put>

    <tiles:put name="mainTile" direct="true">
       <table width="700" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td class="bodytitle">Shipping Address<br><br></td>
            </tr>
            <tr>
                <html:form action="/cart.do?m=setShippingMethod">
                <td>
                    <table border="0" cellspacing="2" cellpadding="2">
                        <tr>
                          <td class="bodyText" align="right">Name:</td>
                          <td><html:text property='shippingAddress.name'/></td>
                          </tr>
                        <tr>
                          <td class="bodyText" align="right">Address1:</td>
                          <td><html:text property='shippingAddress.address1'/>></td>
                        </tr>
                        <tr>
                          <td class="bodyText" align="right">Address2:</td>
                          <td><html:text property='shippingAddress.address2'/></td>
                        </tr>
                        <tr>
                          <td class="bodyText" align="right">City:</td>
                          <td><html:text property='shippingAddress.city'/>></td>
                        </tr>
                        <tr>
                          <td class="bodyText" align="right">State:</td>
                          <td><html:text property='shippingAddress.state'/>></td>
                        </tr>
                        <tr>
                          <td class="bodyText" align="right" nowrap>Postal Code:</td>
                          <td><html:text property='shippingAddress.postalCode'/>></td>
                        </tr>
                        <tr>
                          <td align="right"><br><html:submit property="action" value="Back"/></td>
                          <td align="left"><br><html:submit property="action" value="Continue"/></td>
                        </tr>
                    </table>
                </td>
                </html:form>
            </tr>
        </table>
    </tiles:put>
</tiles:insert>




