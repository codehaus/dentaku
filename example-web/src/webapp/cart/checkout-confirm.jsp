<%@ page import="java.util.ArrayList,
                 java.util.Iterator,
                 java.math.BigDecimal,
                 example.entity.Invoice,
                 example.entity.LineItem"%>


<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" %>

<%
    Invoice inv = (Invoice)request.getSession().getAttribute("invoice");
%>
<tiles:insert page="/tiles/mainTile.jsp">
    <tiles:put name="title" value="Checkout"/>
    <tiles:put name="mainTile" direct="true">
        <table width="700" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td class="bodytitle">Confirm Your Order<br><br></td>
            </tr>
            <tr>
                <td>
                    <table width="600" border="0" cellspacing="3" cellpadding="3">
                        <tr>
                            <td class="bodyTitle" align="center">Qty.</td>
                            <td class="bodyTitle">Description</td>
                            <td class="bodyTitle" align="right">Price</td>
                            <td><img src="/images/spacer.gif" width="15" height="1"></td>
                            <td class="bodyTitle" align="right">Amount</td>
                        </tr>
                        <tr>
                            <td colspan="5"><hr width="100%" size="1" color="#FFFFFF"></td>
                        </tr>
                        <%
                            for (Iterator it = inv.getLineItems().iterator(); it.hasNext();) {
                                LineItem lineItem = (LineItem) it.next();
                        %>

                        <!-- loop over items in the cart -->
                        <tr>
                            <td class="bodyText" align="center" valign="top"><%=(int)lineItem.getQuantity()%></td>
                            <td class="bodyText" width="100%"><%=lineItem.getSku().getName()%></td>
                            <td class="bodyText" align="right" valign="top">$<%=lineItem.getPrice()%></td>
                            <td><img src="/images/spacer.gif" width="15" height="1"></td>
                            <td class="bodyText" align="right" valign="top">$<%=lineItem.getAmount()%></td>
                        </tr>
                        <%
                            }
                        %>

                        <!-- subtotal -->
                        <tr>
                            <td colspan="5"><hr width="100%" size="1" color="#FFFFFF"></td>
                        </tr>
                        <tr>
                            <td class="bodyText" align="right" valign="top">&nbsp;</td>
                            <td class="bodyTextBold" width="100%">Subtotal</td>
                            <td class="bodyText" align="right" valign="top" colspan="2">&nbsp;</td>
                            <td class="bodyTextBold" align="right" valign="top"><%=inv.getSubTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString() %></td>
                        </tr>

                        <!-- taxes (if applicable) -->
                        <tr>
                            <td class="bodyText" align="right" valign="top">&nbsp;</td>
                            <td class="bodyText" width="100%">Taxes</td>
                            <td class="bodyText" align="right" valign="top" colspan="2">&nbsp;</td>
                            <td class="bodyText" align="right" valign="top"><%=inv.getTaxTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString() %></td>
                        </tr>

                        <!-- shipping -->
<%--                        <tr>--%>
<%--                            <td class="bodyText" align="right" valign="top">&nbsp;</td>--%>
<%--                            <td class="bodyText" width="100%">Shipping (<%=inv.getShipMethod().getDescription() %>)</td>--%>
<%--                            <td class="bodyText" align="right" valign="top" colspan="2">&nbsp;</td>--%>
<%--                            <td class="bodyText" align="right" valign="top"><%=inv.getShippingTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString() %></td>--%>
<%--                        </tr>--%>
<%--                        <tr>--%>
<%--                            <td colspan="5"><hr width="100%" size="1" color="#FFFFFF"></td>--%>
<%--                        </tr>--%>

                        <!-- TOTAL -->
                        <tr>
                            <td class="bodyText" align="right" valign="top">&nbsp;</td>
                            <td class="bodyTitle" width="100%">TOTAL</td>
                            <td class="bodyText" align="right" valign="top" colspan="2">&nbsp;</td>
                            <td class="bodyTitle" align="right" valign="top"><%=inv.getTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString() %></td>
                        </tr>
                    </table>

                    <br><br>

                    <table border="0" cellspacing="3" cellpadding="3">
                        <tr>
                            <td class="bodyTitle" width="34%">Ship To</td>
                            <td class="bodyTitle" width="34%">Billing Address</td>
                            <td class="bodyTitle" width="32%">Payment Method</td>
                        </tr>
                        <tr>
                            <td class="bodyTextSmall" valign="top" nowrap>
                                <%=inv.getShipAddress().getName()%><br>
                                <%=inv.getShipAddress().getAddress1()%><br>
                                <%
                                    if (inv.getShipAddress().getAddress2() != null && !inv.getShipAddress().getAddress2().equals("")) {
                                        out.write(inv.getShipAddress().getAddress2()+"<br>");
                                    }
                                %>
                                <%=inv.getShipAddress().getCity()%>, <%=inv.getShipAddress().getState()%> <%=inv.getShipAddress().getPostalCode()%>
                            </td>
                            <td class="bodyTextSmall" valign="top" nowrap>
                                <%=inv.getBillAddress().getName()%><br>
                                <%=inv.getBillAddress().getAddress1()%><br>
                                <%
                                    if (inv.getBillAddress().getAddress2() != null && !inv.getBillAddress().getAddress2().equals("")) {
                                        out.write(inv.getBillAddress().getAddress2()+"<br>");
                                    }
                                %>
                                <%=inv.getBillAddress().getCity()%>, <%=inv.getBillAddress().getState()%> <%=inv.getBillAddress().getPostalCode()%>
                            </td>
                            <td class="bodyTextSmall" nowrap>
                                <%=inv.getCreditCard().getCardName()%><br>
                                <%=inv.getCreditCard().getCardType()%><br>
                                <%=inv.getCreditCard().getCardNumber()%><br>
                                Exp: <%=inv.getCreditCard().getExpMonth()%>/<%=inv.getCreditCard().getExpYear()%>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3"><img src="/images/spacer.gif" height="1" width="1"></td>
                        </tr>
                        <tr>
                             <td colspan="3"><hr width="100%" size="1" color="#FFFFFF"></td>
                        </tr>
                        <tr>
                            <td colspan="3"><img src="/images/spacer.gif" height="1" width="1"></td>
                        </tr>
                        <tr>
                            <td colspan="3" class="bodyText">
                                Please verify that the information above is correct, before submitting your order by clicking the
                                "Submit Order" button below.
                                <br><br>
                                Be sure to only click this button once, to avoid double billing.
                                <br><br>
                            </td>
                        </tr>
                        <html:form action="/cart.do?m=processOrder">
                        <tr>
                            <td colspan="3" align="center"><html:submit property="submit" value="Submit Order"/></td>
                        </tr>
                        </html:form>
                    </table>
                </td>
            </tr>
        </table>
    </tiles:put>
</tiles:insert>



