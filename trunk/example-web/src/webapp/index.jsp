
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" %>

<tiles:insert page="/tiles/mainTile.jsp">
    <tiles:put name="title" value="Products"/>
    <tiles:put name="mainTile" direct="true">
        <table width="800" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="150" valign="top">
                    <!-- left products -->
                    <table border="0" cellpadding="0" cellspacing="3" width="100%">
                        <tr>
                            <td class="bodytext" valign="center" align="middle">
                                <a href="/index.jsp?productID=<%=1%>"><img src="" border="0" width="150" height="117"></a>
                                <a href="/index.jsp?productID=<%=1%>">Product #1</a>
                                <br><br>
                            </td>
                        <tr>
                        <tr>
                            <td class="bodytext" valign="center" align="middle">
                                <a href="/index.jsp?productID=<%=2%>"><img src="" border="0" width="150" height="117"></a>
                                <a href="/index.jsp?productID=<%=2%>">Product #2</a>
                                <br><br>
                            </td>
                        <tr>
                        <tr>
                            <td class="bodytext" valign="center" align="middle">
                                <a href="/index.jsp?productID=<%=3%>"><img src="" border="0" width="150" height="117"></a>
                                <a href="/index.jsp?productID=<%=3%>">Product #3</a>
                                <br><br>
                            </td>
                        <tr>
                    </table>
                    <!-- end left products -->
                </td>

                <td><img src="/images/spacer.gif" width="15" height="1"></td>

                <td width="450" valign="top">
                    <!-- this product -->
                    <table border="0" cellpadding="0" cellspacing="3">
                        <tr>
                            <td colspan="2">
                                <img src="" width="450" height="350">
                            </td>
                        </tr>
                        <tr>
                            <td class="bodytitle" nowrap>Sample Product</td>
                            <td class="bodytitle" align="right">$249.99</td>
                        </tr>
                        <tr>
                            <td class="bodytext" colspan="2">
                                Here's where a description goes.
                            </td>
                        </tr>
                        <tr>
                            <form method="post" action="/cart.do?m=add">
                            <td class="bodytitle" colspan="2" align="middle">
                                <input name="sku" type="hidden" value="1"> Quantity:
                                <input name="quantity" type="text" value="1" size="4">
                                <input type="submit" name="Submit" value="Add to Cart">
                            </td>
                            </form>
                        </tr>
                        </form>
                    </table>
                    <!-- end this product -->
                </td>

                <td><img src="/images/spacer.gif" width="15" height="1"></td>


                <td width="150" valign="top">
                    <!-- right products -->
                    <table border="0" cellpadding="0" cellspacing="3" width="100%">
                        <tr>
                            <td class="bodytext" valign="center" align="middle">
                                <a href="/index.jsp?productID=<%=1%>"><img src="" border="0" width="150" height="117"></a>
                                <a href="/index.jsp?productID=<%=1%>">Product #4</a>
                                <br><br>
                            </td>
                        <tr>
                        <tr>
                            <td class="bodytext" valign="center" align="middle">
                                <a href="/index.jsp?productID=<%=1%>"><img src="" border="0" width="150" height="117"></a>
                                <a href="/index.jsp?productID=<%=1%>">Product #5</a>
                                <br><br>
                            </td>
                        <tr>
                        <tr>
                            <td class="bodytext" valign="center" align="middle">
                                <a href="/index.jsp?productID=<%=1%>"><img src="" border="0" width="150" height="117"></a>
                                <a href="/index.jsp?productID=<%=1%>">Product #6</a>
                                <br><br>
                            </td>
                        <tr>
                    </table>
                    <!-- end right products -->
                </td>
            </tr>
        </table>
    </tiles:put>
</tiles:insert>

