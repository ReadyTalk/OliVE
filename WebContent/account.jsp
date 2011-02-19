<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.readytalk.olive.util.Attribute"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Olive | The Online Video Editor</title>

<link rel="shortcut icon" href="/olive/images/olive.ico" />

<link rel="stylesheet" type="text/css" href="/olive/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="/olive/scripts/jquery-ui-1.8.9.custom/css/ui-lightness/jquery-ui-1.8.9.custom.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/master.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/account.css" />

<script src="/olive/scripts/jquery-1.5.min.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.9.custom/js/jquery-ui-1.8.9.custom.min.js"></script>
<script src="/olive/scripts/master.js"></script>
<script src="/olive/scripts/account.js"></script>
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

	Boolean editSuccessfully = (Boolean) session
			.getAttribute(Attribute.EDIT_SUCCESSFULLY.toString());
	Boolean passwordsMatch = (Boolean) session
			.getAttribute(Attribute.PASSWORDS_MATCH.toString());
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
	session.removeAttribute(Attribute.EDIT_SUCCESSFULLY.toString());
	session.removeAttribute(Attribute.PASSWORDS_MATCH.toString());

	String username = (String) session.getAttribute(Attribute.USERNAME
			.toString());
	String name = (String) session.getAttribute(Attribute.NAME
			.toString());
	String email = (String) session.getAttribute(Attribute.EMAIL
			.toString());
	String password = (String) session.getAttribute(Attribute.PASSWORD
			.toString());
	String securityQuestion = (String) session
			.getAttribute(Attribute.SECURITY_QUESTION.toString());
	String securityAnswer = (String) session
			.getAttribute(Attribute.SECURITY_ANSWER.toString());
%>
<div id="header">
<div id="header-left">
<h1>Olive</h1>
</div>
<!-- end #header-left -->
<div id="header-right">
<div>Welcome, <%=username%>!&nbsp;<a href="logout.jsp">Logout</a></div>
<div><strong><a href="projects.jsp">My Projects</a></strong>&nbsp;<span
	id="help-dialog-opener"><a href="">Help</a></span></div>
<div id="help-dialog" title="How to use Olive">
<ul>
	<li>1. Create a new account.</li>
	<li>2. Create a new project.</li>
	<li>3. Upload your videos.</li>
	<li>4. Edit your videos.</li>
	<li>5. Export to your computer.</li>
</ul>
</div>
</div>
<!-- end #header-right --></div>
<!-- end #header -->

<div class="clear"></div>

<div id="main">

<div id="edit-account-container">
<h2>Edit account information</h2>
<form id="edit-account-form" action="OliveServlet" name="process"
	method="post">
<p><label for="new-name">Name</label><br />
<input type="text" name="new-name" id="new-name" value="<%=name%>"
	size="32" maxlength="32" /></p>
<p><label for="new-email">Email</label><br />
<input type="text" name="new-email" id="new-email" value="<%=email%>"
	size="32" maxlength="64" /></p>
<p><label for="new-password">Password</label><br />
<input type="password" name="new-password" id="new-password"
	value="<%=password%>" size="32" maxlength="128" /></p>
<p><label for="confirm-new-password">Confirm password</label><br />
<input type="password" name="confirm-new-password"
	id="confirm-new-password" value="<%=password%>" size="32"
	maxlength="128" /></p>
<p><label for="security_question">Security Question</label><br />
<input type="text" name="security_question" id="security_question"
	value="<%=securityQuestion%>" size="32" maxlength="128" /></p>
<p><label for="security_answer">Security Answer</label><br />
<input type="text" name="security_answer" id="security_question_answer"
	value="<%=securityAnswer%>" size="32" maxlength="128" /></p>
<input type="hidden" name="FormName" value="EditUser"></input><br />
<input type="submit" value="Update information" /><span><%=editConfirmation%></span></form>
<form id="delete-account" action="OliveServlet" name="process"
	method="post"><input type="hidden" name="FormName"
	value="DeleteAccount"></input><br />
<input type="submit" value="Delete Account" /> <!-- TODO: Add a "Are you sure you want to delete your account window?" here  -->
</form>
</div>
<!-- end #login-form-container --></div>
<!-- end #main -->

<div class="clear"></div>

<div id="footer"></div>
</body>
</html>