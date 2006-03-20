<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java"%>

<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><tiles:getAsString name="title"/></title>
    <link rel="STYLESHEET" type="text/css" href="<%= request.getContextPath() %>/tiles/styles.css">
<tiles:insert attribute="javascript" ignore="true"/>
</head>
<body bgcolor="#000000">

<br><br>

<!-- page body -->
<div align="left">
<table width="950" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top">
            <!-- upper left nav -->
            <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <img src="/images/spacer.gif" height="150" width="150">
                    </td>
                </tr>
            </table>
            <!-- end upper left nav -->

            <br><br>

            <!-- lower left nav -->
            <tiles:insert attribute="lowerLeftNavTile" ignore="true"/>
            <!-- end lower left nav -->
        </td>

        <td valign="top">
            <!-- top logo -->
            <!-- end top logo -->

            <br><br><br>

             <!-- top navbar -->
            <!-- end top navbar -->

            <br><br>

            <!-- page content -->
            <div align="center">
            <tiles:insert attribute="mainTile" />
            </div>
            <!-- end page content -->

        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
        <td>

             <br><br>

            <!-- footer -->
            <!-- end footer -->

        </td>
    </tr>
</table>
</div>
<!-- end page body -->

</body>
</html:html>
