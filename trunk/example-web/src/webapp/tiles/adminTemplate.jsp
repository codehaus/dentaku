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
<body bgcolor="#FFFFFF">


<table cellpadding=0 cellspacing=0 border="0" width=800>
<tr>
<td width="50%"><a href="/admin/"><img src="/images/logo.gif" width="353" height="62" border="0"></a></td>
<td width="50%" align="right">

	<table cellpadding=0 cellspacing=0 border=0 width=185>
	<tr>
	<td><h2>Admin</h2></td>
	</tr>
	<tr>
	<td bgcolor="#bfd6e9" align="right" valign="top">
	<font face="Arial, Helvetica, sans-serif" size="2"><a href="/">Site Home</a>&nbsp;</font><br>
	<font face="Arial, Helvetica, sans-serif" size="2"><a href="/admin/">Admin Home</a>&nbsp;</font><br>
	</td>
	</tr>
	</table>
</td>
</tr>
</table>

<tiles:insert attribute="mainTile" />

</body>
</html:html>
