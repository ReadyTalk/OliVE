<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	Boolean isAuthorized = (Boolean) session
			.getAttribute("isAuthorized"); // Nasty cast
	if (isAuthorized == null) {
		response.sendRedirect("index.jsp");
	} else if (!isAuthorized) {
		response.sendRedirect("index.jsp");
	}

	Boolean isSafe = (Boolean) session.getAttribute("isSafe"); // Nasty cast
	String safeMessage;
	if (isSafe == null) {
		safeMessage = "";
	} else if (isSafe) {
		// Syntax: http://www.infimum.dk/HTML/JSwindows.html
		safeMessage = "<script> window.opener.location.reload(); window.close(); </script>";
	} else {
		safeMessage = "Invalid project name.";
	}

	session.removeAttribute("isSafe");
%>
<form id="add-project-form" action="OliveServlet" name="process"
	method="post">
<p><label for="ProjectName">Project Name</label> <input type="text"
	name="ProjectName" id="ProjectName" value="" size="32" maxlength="128" /></p>
<p><%=safeMessage%></p>
<input type="hidden" name="FormName" value="AddProject"></input> <input
	type="submit" value="Create Project" /></form>
</body>
</html>