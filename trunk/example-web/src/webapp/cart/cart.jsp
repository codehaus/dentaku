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
    <tiles:put name="title" value="Shopping Cart"/>
    <tiles:put name="mainTile" direct="true">
       <table width="700" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td class="bodyTitle">Cart Contents<br><br></td>
            </tr>
            <tr>
                <td class="bodyText">

                    <%
                        Invoice inv = (Invoice)request.getSession().getAttribute("invoice");
                        int cartSize = inv.getLineItems().size();
                        if (cartSize == 0) {
                            out.write("Your cart is empty");
                        } else {
                    %>

                    <table width= "690'" border="1" bordercolor="#FFFFFF" cellspacing="0" cellpadding="8">
                        <tr>
                            <td class="bodyTitle" align="left" width="100%">Description</td>
                            <td class="bodyTitle" align="center">Quantity</td>
                            <td class="bodyTitle" align="right">Price</td>
                            <td class="bodyTitle" align="right">Total</td>
                            <td class="bodyTitle">&nbsp;</td>
                        </tr>

                        <%
                            int counter = 0;
                            for (Iterator it = inv.getLineItems().iterator(); it.hasNext();) {
                                LineItem lineItem = (LineItem) it.next();
                        %>

                        <tr>
                            <td class="bodyText" width="100%"><%=lineItem.getSku().getName()%></td>
                            <td class="bodyText" align="center" valign="top"><%=(int)lineItem.getQuantity()%></td>
                            <td class="bodyText" align="right" valign="top">$<%=lineItem.getPrice()%></td>
                            <td class="bodyText" align="right" valign="top">$<%=lineItem.getAmount()%></td>
                            <td class="bodyText" align='center'><a href="/cart.do?m=remove&item=<%=lineItem.getSku().getId() %>">remove</a></td>
                        </tr>

                        <%  }  %>

                        <tr>
                            <td class="bodytitle" colspan="3" align="right">Sub-Total</td>
                            <td class="bodytext" align="right"><%=inv.getSubTotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString() %></td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                    <!-- end cart contents -->

                    <%  }  %>

                    <br><br>

                </td>
            </tr>
            <tr>
                <td>
                    <table border="0" cellpadding="0" cellspacing="2">
                        <tr>
                        <form action="/index.jsp">
                            <td <% if (cartSize != 0) out.write("width='50%'"); %> class="bodytext" align="right"><input type="submit" value="Continue Shopping"></td>
                        </form>
                        <%
                            if (cartSize != 0) {
                                out.write("<form method='post' action='/cart.do?m=setShipping'>");
                                out.write("<td class='bodytext' align='left'><input type='submit' value='Check Out'></td>");
                                out.write("</form>");
                            }
                        %>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </tiles:put>
</tiles:insert>


