<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form id="add-project-form" action="OliveServlet" name="process" method="post">
<p><label for="new-name">Project Name</label> <input type="text"
	name="new-name" id="new-name" value="" size="32" maxlength="128" /></p>
<input type="hidden" name="FormName" value="AddProject"></input>
<input type="submit" value="Create Project" /></form>
</body>
</html>