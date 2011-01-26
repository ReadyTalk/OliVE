<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
	session.removeAttribute("isAuthorized");
	session.removeAttribute("username");
	session.removeAttribute("password");
	session.removeAttribute("editSuccesfully");
	session.removeAttribute("addSuccesfully");
	session.removeAttribute("projectTitle");
	response.sendRedirect("index.jsp");
%>
</body>
</html>