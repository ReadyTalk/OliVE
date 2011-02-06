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
<link rel="stylesheet" type="text/css" href="/olive/css/index.css" />

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
<script src="/olive/scripts/index.js"></script>
<script src="/olive/scripts/google-analytics.js"></script>
</head>
<body>
<%
	Boolean isAuthorized = (Boolean) session
			.getAttribute("isAuthorized"); // Nasty cast
	String loginMessage;
	if (isAuthorized == null) {
		loginMessage = "Please log in.";
	} else if (isAuthorized) {
		loginMessage = "You have been successfully logged in.";
	} else {
		loginMessage = "Incorrect username and/or password.";
	}
%>
<div id="header">
<div id="header-left">
<h1>Olive</h1>
</div>
<!-- end #header-left -->
<div id="header-right"><a href="" onclick="openHelpWindow();">Help</a></div>
<!-- end #header-right --></div>
<!-- end #header -->

<div class="clear"></div>

<div id="main">
<div id="splash-container"><img id="splash-image"
	src="/olive/images/splash-simple.png" /></div>
<!-- end #splash-container -->

<div id="login-form-container">

<form id="login-form" action="OliveServlet" name="process" method="post">
<p><label for="username">Username</label> <input type="text"
	name="username" id="login-username" size="32" maxlength="128" /></p>
<p><label for="password">Password</label> <input type="password"
	name="password" id="login-password" size="32" maxlength="128" /></p>
<input type="hidden" name="FormName" value="LoginUser"></input> <input
	type="submit" value="Login" /> <span><%=loginMessage%> <a
	href="forgot.jsp">Forgot password?</a></span></form>
<p>Don't have an account? <a id="create-user" href="javascript:;"
	title="">Sign up for one now!</a></p>
</div>
<!-- end #login-form-container -->

<div id="dialog-form" title="Create new user">
<p class="validateTips">All form fields are required.</p>
<form id="register-form" action="OliveServlet" name="process"
	method="post">
<fieldset><label for="name">Username</label> <input
	type="text" name="name" id="register-name"
	class="text ui-widget-content ui-corner-all" /> <label for="email">Email</label>
<input type="text" name="email" id="register-email" value=""
	class="text ui-widget-content ui-corner-all" /> <label for="password">Password</label>
<input type="password" name="password" id="register-password" value=""
	class="text ui-widget-content ui-corner-all" /></fieldset>
<input type="hidden" name="FormName" value="AddUser"></input></form>
</div>
<!-- end #dialog-form --></div>
<!-- end #main -->

<div id="footer"></div>
</body>
</html>