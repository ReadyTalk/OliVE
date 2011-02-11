<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.readytalk.olive.util.Attribute"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create new project | Olive</title>
<script src="/olive/scripts/google-analytics.js"></script>
</head>
<body>
<%
	Boolean isAuthorized = (Boolean) session
			.getAttribute(Attribute.IS_AUTHORIZED.toString()); // Nasty cast
	if (isAuthorized == null) {
		response.sendRedirect("index.jsp");
	} else if (!isAuthorized) {
		response.sendRedirect("index.jsp");
	}

	Boolean isSafe = (Boolean) session.getAttribute(Attribute.IS_SAFE
			.toString()); // Nasty cast
	Boolean addedSuccesfully = (Boolean) session
			.getAttribute(Attribute.ADD_SUCCESSFULLY.toString());
	String safeMessage;
	if (isSafe == null) {
		safeMessage = "";
	} else if (isSafe) {
		if(addedSuccesfully){
			// Syntax: http://www.infimum.dk/HTML/JSwindows.html
			safeMessage = "<script> window.opener.location.reload(); window.close(); </script>";	
		}
		else{
			safeMessage = "Project already exists";	
		}
	} else {
		safeMessage = "Project name must be between 1 and 32 characters; must consist of a-z, 0-9, underscores; and must begin with a letter.";
	}
	session.removeAttribute(Attribute.ADD_SUCCESSFULLY.toString());
	session.removeAttribute(Attribute.IS_SAFE.toString());
%>
<form id="add-project-form" action="OliveServlet" name="process"
	method="post">
<p><label for="ProjectName">Project Name</label> <input type="text"
	name="ProjectName" id="ProjectName" value="" size="32" maxlength="128" />
</p>
<p><%=safeMessage%></p>
<input type="hidden" name="FormName" value="AddProject"></input> <input
	type="submit" value="Create Project" /></form>
</body>
</html>