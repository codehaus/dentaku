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
                
                for (Iterator it = inv.getLineItems().iterator(); it.hasNext();) {
                    LineItem lineItem = (LineItem) it.next();
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
                <td class="bodytitle">Payment Method<br><br></td>
            </tr>
            <tr>
                <html:form action="/cart.do?m=confirmOrder">
                <td>
                    <table border="0" cellspacing="2" cellpadding="2" width="400">
                        <tr>
                            <td class="bodytext" align="right">Name on Card:</td>
                            <td><html:text property="creditCard.cardName"/></td>
                        </tr>
                        <tr>
                            <td class="bodytext" align="right">Card Type:</td>
                            <td>
                                <html:select property="creditCard.cardType">
                                    <html:option value="">Select One...</html:option>
                                    <html:option value="Visa">Visa</html:option>
                                    <html:option value="MasterCard">MasterCard</html:option>
                                    <html:option value="JCB">JCB</html:option>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td class="bodytext" align="right">Card Number:</td>
                            <td><html:text property="creditCard.cardNumber"/></td>
                        </tr>
                        <tr>
                            <td class="bodytext" align="right">Expiration:</td>
                            <td class="bodytext">
                                <html:select property="creditCard.expMonth">
                                    <html:option value="1">Jan</html:option>
                                    <html:option value="2">Feb</html:option>
                                    <html:option value="3">Mar</html:option>
                                    <html:option value="4">Apr</html:option>
                                    <html:option value="5">May</html:option>
                                    <html:option value="6">Jun</html:option>
                                    <html:option value="7">Jul</html:option>
                                    <html:option value="8">Aug</html:option>
                                    <html:option value="9">Sep</html:option>
                                    <html:option value="10">Oct</html:option>
                                    <html:option value="11">Nov</html:option>
                                    <html:option value="12">Dec</html:option>
                                </html:select>
                                &nbsp;/&nbsp;
                                <html:select property="creditCard.expYear">
                                    <html:option value="2003">2003</html:option>
                                    <html:option value="2004">2004</html:option>
                                    <html:option value="2005">2005</html:option>
                                    <html:option value="2006">2006</html:option>
                                    <html:option value="2007">2007</html:option>
                                </html:select>
                            </td>
                        </tr>
                        <tr>
                            <td class="bodytext" align="right">CVV:</td>
                            <td><html:text property="creditCard.CVV" size="5" /></td>
                        </tr>
                        <tr>
                            <td class="bodytext" width="150">&nbsp;</td>
                            <td class="bodytext">&nbsp;</td>
                        </tr>
                    </table>
                </td>
            </tr>

            <!-- billing address -->
            <tr>
                <td class="bodytitle">Billing Address<br><br></td>
            </tr>
            <tr>
                <td class="bodytext">
                    &nbsp;&nbsp;&nbsp;Check here if different than shipping address <html:checkbox property="billingSameAsShipping" />
                </td>
            </tr>
            <tr>
                <td><img src="/images/spacer.gif" height="5" width="5"></td>
            </tr>
            <tr>
                <td>
                    <table border="0" cellspacing="2" cellpadding="2" width="400">
                        <tr>
                            <td class="bodytext" align="right">Name:</td>
                            <td><html:text property='billingAddress.name'/></td>
                        </tr>
                        <tr>
                            <td class="bodytext" align="right">Address1:</td>
                            <td><html:text property="billingAddress.address1"/></td>
                        </tr>
                        <tr>
                            <td class="bodytext" align="right">Address2:</td>
                            <td><html:text property="billingAddress.address2"/></td>
                        </tr>
                        <tr>
                            <td class="bodytext" align="right">City:</td>
                            <td><html:text property="billingAddress.city"/></td>
                        </tr>
                        <tr>
                            <td class="bodytext" align="right">State:</td>
                            <td><html:text property="billingAddress.state"/></td>
                        </tr>
                        <tr>
                            <td class="bodytext" align="right">Postal Code:</td>
                            <td><html:text property="billingAddress.postalCode"/></td>
                        </tr>
                        <tr>
                            <td class="bodytext" align="right">Email:</td>
                            <td><html:text property="email"/></td>
                        </tr>
                        <tr>
                            <td width="150"><br>&nbsp;</td>
                            <td><br><input type="submit" name="Submit" value="Continue"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            </html:form>
        </table>
    </tiles:put>
</tiles:insert>


