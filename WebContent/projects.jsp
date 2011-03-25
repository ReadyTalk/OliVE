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
	href="/olive/scripts/jquery-ui-1.8.9.custom/css/ui-lightness/jquery-ui-1.8.9.custom.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/master.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/projects.css" />

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
	String projectsHtml = "";
	if (isAuthorized == null) {
		response.sendRedirect("index.jsp");
	} else if (!isAuthorized) {
		response.sendRedirect("index.jsp");
	} else {
		Boolean isFirstSignIn = (Boolean) session.getAttribute(Attribute.IS_FIRST_SIGN_IN.toString());
		username = (String) session.getAttribute(Attribute.USERNAME
				.toString());
		int accountId = DatabaseApi.getAccountId(username);
		if(isFirstSignIn){
			projectsHtml = "<p>Welcome to Olive. This is the projects page where you "+
			"can add, edit, and delete your projects. We would like to "+
				"remind you to go to the Account Information page by clicking "+
				"on your username at the top right and adding a security question "+
				"and answer in case you forget your password.<br /><br />Thanks!<br /><br />The Olive Team</p>";
		}
	}
%>
<div id="header">
<div id="header-left">
<h1>Olive</h1>
</div>
<div id="header-right">
<div>Welcome, <a href="account.jsp"><%=username%>!</a>&nbsp;<a
	href="logout.jsp">Logout</a></div>
<div><strong><a href="projects.jsp">My Projects</a></strong>&nbsp;<span
	id="help-dialog-opener"><a href="">Help</a></span></div>
</div>
</div>

<div class="clear"></div>

<div id="main">
<div id="projects-container">

<div id="projects-title">
<h1>My Projects</h1>
</div>
<!-- end #projects-title -->

<div id="projects-controls">
<button id="create-new-project" type="button"
	class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-state-hover"><span
	class="ui-button-text">Create New Project</span></button>
</div>
<!-- end #controls -->

<div class="clear"></div>
<div id="projects"><%=projectsHtml%></div>
<!-- end #projects --></div>
<!-- end #projects-container --></div>
<!-- end #main -->

<div class="clear"></div>

<div id="footer"></div>

<!-- Everything below this line will be hidden and inserted in pop-ups. -->
<div id="first-sign-in-dialog" class="hidden" title="Welcome to Olive!">
<p>Welcome to Olive. This is the projects page where you
can add, edit, and delete your projects. We would like to
remind you to go to the Account Information page by clicking
on your username at the top right and adding a security question
and answer in case you forget your password.<br />Thanks!<br />The Olive Team</p>
</div>

<div id="help-dialog" class="hidden" title="How to use Olive">
<ul>
	<li>1. Create a new account.</li>
	<li>2. Create a new project.</li>
	<li>3. Upload your videos.</li>
	<li>4. Edit your videos.</li>
	<li>5. Export to your computer.</li>
</ul>
</div>

<div id="confirm-delete-project-dialog" class="hidden" title="Warning!">
<p>Delete project? This will also delete the project's videos.</p>
</div>

<div id="new-project-dialog-form" class="hidden" title="Create new user">
<p class="validateTips">All form fields are required.</p>
<form id="new-project-form" action="OliveServlet" name="process"
	method="post">
<fieldset><label for="new-project-name">Project Name</label>
<input type="text" name="new-project-name" id="new-project-name"
	class="text ui-widget-content ui-corner-all" maxlength="32" /></fieldset>
<input type="hidden" name="FormName" value="AddProject"></input></form>
</div>
<!-- end #new-project-dialog-form -->

</body>
</html>
