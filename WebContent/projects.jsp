<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.readytalk.olive.logic.DatabaseApi"%>
<%@ page import="com.readytalk.olive.util.Attribute"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>My Projects | Olive</title>

<link rel="shortcut icon" href="/olive/images/olive.ico">

<link rel="stylesheet" type="text/css" href="/olive/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="/olive/scripts/jquery-ui-1.8.9.custom/css/custom-theme/jquery-ui-1.8.11.custom.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/master.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/projects.css" />
<link href='http://fonts.googleapis.com/css?family=Lato: regular,bold'
	rel='stylesheet' type='text/css'>

<script src="/olive/scripts/jquery-1.5.min.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.9.custom/js/jquery-ui-1.8.9.custom.min.js"></script>
<script src="/olive/scripts/jquery.editable-1.3.3.min.js"></script>
<script src="/olive/scripts/master.js"></script>
<script src="/olive/scripts/projects.js"></script>
<script src="/olive/scripts/google-analytics.js"></script>
</head>
<body>
<%
	Boolean isAuthorized = (Boolean) session
			.getAttribute(Attribute.IS_AUTHORIZED.toString()); // Nasty cast
	String username = "";
	if (isAuthorized == null) {
		response.sendRedirect("index.jsp");
	} else if (!isAuthorized) {
		response.sendRedirect("index.jsp");
	} else {
		username = (String) session.getAttribute(Attribute.USERNAME
				.toString());
	}
%>
<div id="header">
<div id="header-left"><img id="olive-icon"
	src="/olive/images/olive.png" />
<h1 id="olive-title">Olive</h1>
</div>
<div id="header-right">
<div><a href="account.jsp">Account</a>&nbsp;&nbsp;&nbsp;&nbsp;<span
	id="help-dialog-opener"><a href="">Help</a></span>&nbsp;&nbsp;&nbsp;&nbsp;<span
	id="about-dialog-opener"><a href="">About</a></span>&nbsp;&nbsp;&nbsp;&nbsp;<a
	href="logout.jsp">Logout</a></div>
</div>
</div>

<div class="clear"></div>

<div id="main">
<div id="projects-container">

<div id="projects-header"><span id="projects-title">Projects</span>
<span id="projects-controls">
<button id="create-new-project" class="hidden" type="button">Create
New Project</button>
</span></div>

<div class="clear"></div>
<div id="projects"></div>
<!-- end #projects --></div>
<!-- end #projects-container --></div>
<!-- end #main -->

<div class="clear"></div>

<!-- Everything below this line will be hidden and inserted in pop-ups. -->
<div id="shared-dialogs" class="hidden"></div>

<div id="first-sign-in-dialog" class="hidden" title="Welcome to Olive!">
<p>Welcome to Olive. This is the projects page where you can add,
edit, and delete your projects. We would like to remind you to go to the
Account Information page by clicking on your username at the top right
and adding a security question and answer in case you forget your
password.<br />
Thanks!<br />
The Olive Team</p>
</div>

<div id="confirm-delete-project-dialog" class="hidden" title="Warning!">
<p>Delete project? This will also delete the project's videos.</p>
</div>

<div id="new-project-dialog-form" class="hidden"
	title="Create New Project">
<p class="validateTips">All form fields are required.</p>
<form id="new-project-form">
<fieldset><label for="new-project-name">Project Name</label>
<input type="text" name="new-project-name" id="new-project-name"
	class="text ui-widget-content ui-corner-all" maxlength="32" /><!-- Add an extra input to get around a jQuery UI bug: http://bugs.jqueryui.com/ticket/6967 --><input
	type="text" class="hidden" /></fieldset>
</form>
</div>
<!-- end #new-project-dialog-form -->

</body>
</html>
