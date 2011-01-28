<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Olive | The Online Video Editor</title>

<link rel="shortcut icon" href="/olive/images/olive.ico" />

<link rel="stylesheet" type="text/css" href="/olive/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="/olive/scripts/jquery-ui-1.8.6.custom/css/ui-lightness/jquery-ui-1.8.6.custom.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/master.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/account.css" />

<script src="/olive/scripts/jquery-1.4.4.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.core.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.widget.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.mouse.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.button.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.position.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.dialog.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.draggable.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.droppable.js"></script>
<script src="/olive/scripts/master.js"></script>
<script src="/olive/scripts/account.js"></script>
<script src="/olive/scripts/google-analytics.js"></script>
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

	Boolean editSuccessfully = (Boolean) session
			.getAttribute("editSuccessfully");
	Boolean passwordsMatch = (Boolean) session
			.getAttribute("passwordsMatch");
	String editConfirmation;
	if (editSuccessfully == null) {
		editConfirmation = "";
	} else if (editSuccessfully) {
		editConfirmation = "Your information has been changed successfully.";
	} else {
		if (passwordsMatch == null) { // Not safe
			editConfirmation = "Invalid characters or length on one or more fields.";
		} else { // Safe, but differing passwords
			editConfirmation = "Passwords do not match.";
		}
	}
	session.removeAttribute("editSuccessfully");
	session.removeAttribute("passwordsMatch");

	String username = (String) session.getAttribute("username");
	String name = (String) session.getAttribute("name");
	String email = (String) session.getAttribute("email");
	String password = (String) session.getAttribute("password");
%>
<div id="header">
<div id="header-left">
<h1>Olive</h1>
</div>
<!-- end #header-left -->
<div id="header-right">
<div>Welcome, <%=username%>!&nbsp;<a href="logout.jsp">Logout</a></div>
<div><strong><a href="projects.jsp">My Projects</a></strong>&nbsp;<a
	href="#">Help</a></div>
</div>
<!-- end #header-right --></div>
<!-- end #header -->

<div class="clear"></div>

<div id="main">

<div id="edit-account-container">
<h2>Edit account information</h2>
<form id="edit-account-form" action="OliveServlet" name="process"
	method="post">
<p><label for="new-name">Name</label> <input type="text"
	name="new-name" id="new-name" value="<%=name%>" size="32" maxlength="128" /></p>
<p><label for="new-email">Email</label> <input type="text"
	name="new-email" id="new-email" value="<%=email%>" size="32" maxlength="128" /></p>
<p><label for="new-password">Password</label> <input type="password"
	name="new-password" id="new-password" value="<%=password%>" size="32"
	maxlength="128" /></p>
<p><label for="confirm-new-password">Confirm password</label> <input
	type="password" name="confirm-new-password" id="confirm-new-password"
	value="<%=password%>" size="32" maxlength="128" /></p>
<input type="hidden" name="FormName" value="EditUser"></input> <input
	type="submit" value="Update information" /><span><%=editConfirmation%></span></form>

</div>
<!-- end #login-form-container --></div>
<!-- end #main -->

<div id="footer">
<div id="footer-left"><a href="#">About Us</a></div>
<div id="footer-right">&copy; 2010 ReadyTalk</div>
</div>
</body>
</html>