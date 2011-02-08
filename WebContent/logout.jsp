<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.readytalk.olive.util.Attribute"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Logout | Olive</title>
<script src="/olive/scripts/google-analytics.js"></script>
</head>
<body>
<%
	// Modified from: http://stackoverflow.com/questions/1104975/for-loop-to-interate-over-enum-in-java
	for (Attribute attribute : Attribute.values()) {
		session.removeAttribute(attribute.toString());
	}
	response.sendRedirect("index.jsp");
%>
</body>
</html>