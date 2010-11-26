<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Olive | The Online Video Editor</title>

<link rel="shortcut icon" href="/olive/images/olive.ico" />

<link rel="stylesheet" type="text/css" href="/olive/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="/olive/css2/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css"
	href="/olive/scripts/jquery-ui-1.8.6.custom/css/ui-lightness/jquery-ui-1.8.6.custom.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/style.css" />

<script src="/olive/scripts/jquery-1.4.4.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.core.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.widget.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.button.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.position.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.dialog.js"></script>
<script src="/olive/scripts/script.js"></script>
<script src="/olive/scripts/google-analytics.js"></script>
</head>
<body>
<%
	Object validity = session.getAttribute("validity");
	String loginMessage = (validity != null) ? (String) validity
			: "Please log in.";
%>
<div id="header">
<div id="header-left">
<h1>Olive</h1>
</div>
<!-- end #header-left -->
<div id="header-right"><a href="#">Help</a></div>
<!-- end #header-right --></div>
<!-- end #header -->

<div class="clear"></div>

<div id="main">
<div id="main-left">
<p>Olive Description</p>
</div>
<!-- end #main-left -->

<div id="main-right">

<form action="OliveServlet" name="process" method="POST">
<p><label for="username">Username</label> <input type="text"
	name="username" id="username" size="32" maxlength="128" /></p>
<p><label for="password">Password</label> <input type="password"
	name="password" id="password" size="32" maxlength="128" /></p>
<input type="submit" value="Login" /> <span><%=loginMessage%></span></form>
<p>Don't have an account? <a id="create-user" href="javascript:;"
	title="">Sign up for one now!</a></p>
</div>
<!-- end #main-right -->

<div class="clear"></div>

<div id="dialog-form" title="Create new user">
<p class="validateTips">All form fields are required.</p>
<form>
<fieldset><label for="name">Name</label> <input type="text"
	name="name" id="name" class="text ui-widget-content ui-corner-all" />
<label for="email">Email</label> <input type="text" name="email"
	id="email" value="" class="text ui-widget-content ui-corner-all" /> <label
	for="password">Password</label> <input type="password" name="password"
	id="password" value="" class="text ui-widget-content ui-corner-all" />
</fieldset>
</form>
</div>
<!-- end #dialog-form --></div>
<!-- end #main -->

<div class="clear"></div>

<div class="demo"></div>
<!-- End form -->

<div id="footer">
<div id="footer-left"><a href="#">About Us</a></div>
<div id="footer-right">&copy; 2010 ReadyTalk</div>
</div>
</body>
</html>