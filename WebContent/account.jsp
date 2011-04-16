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
	href="/olive/scripts/jquery-ui-1.8.9.custom/css/custom-theme/jquery-ui-1.8.11.custom.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/master.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/account.css" />
<link href='http://fonts.googleapis.com/css?family=Lato: regular,bold'
	rel='stylesheet' type='text/css'>

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
	
	Boolean editNameSuccessfully = (Boolean) session
			.getAttribute(Attribute.EDIT_NAME_SUCCESSFULLY.toString());
	Boolean editPwdSuccessfully = (Boolean) session
			.getAttribute(Attribute.EDIT_PWD_SUCCESSFULLY.toString());
	Boolean editSecSuccessfully = (Boolean) session
			.getAttribute(Attribute.EDIT_QA_SUCCESSFULLY.toString());

	
	Boolean passwordsMatch = (Boolean) session
			.getAttribute(Attribute.PASSWORDS_MATCH.toString());
	String editNameEmailConfirmation = "";
	String editPasswordConfirmation = "";
	String editSecurityConfirmation = "";
	if (editNameSuccessfully == null) {
		editNameEmailConfirmation = "";
		
		if(editPwdSuccessfully==null) {
			editPasswordConfirmation = "";
			
			
			if(editSecSuccessfully==null) {	
				editSecurityConfirmation = "";
			} else if(editSecSuccessfully){
				editSecurityConfirmation = "Your security information has been changed successfully.";
				session.removeAttribute(Attribute.EDIT_QA_SUCCESSFULLY.toString());
			} else {
				editSecurityConfirmation = "Invalid characters or length in entry.";
				session.removeAttribute(Attribute.EDIT_QA_SUCCESSFULLY.toString());
			}
			
			
		} else if(editPwdSuccessfully){
			editPasswordConfirmation = "Your password has been changed successfully.";
			session.removeAttribute(Attribute.EDIT_PWD_SUCCESSFULLY.toString());
		} else {
			if (passwordsMatch == null) { // Not safe
				editPasswordConfirmation = "Invalid characters or length in entry.";
			} else { // Safe, but differing passwords
				editPasswordConfirmation = "Passwords do not match.";
				session.removeAttribute(Attribute.PASSWORDS_MATCH.toString());
			}
			session.removeAttribute(Attribute.EDIT_PWD_SUCCESSFULLY.toString());
		}
	} else if (editNameSuccessfully) {
		editNameEmailConfirmation = "Your name and/or email have been changed successfully.";
		session.removeAttribute(Attribute.EDIT_NAME_SUCCESSFULLY.toString());
	} else {
		editNameEmailConfirmation = "Invalid characters or length in entry.";
		session.removeAttribute(Attribute.EDIT_NAME_SUCCESSFULLY.toString());
	}
	String username = (String) session.getAttribute(Attribute.USERNAME
			.toString());
%>
<div id="header">
<div id="header-left"><img id="olive-icon"
	src="/olive/images/olive.png" />
<h1 id="olive-title">Olive</h1>
</div>
<!-- end #header-left -->
<div id="header-right">
<div>&nbsp;&nbsp;&nbsp;&nbsp;<strong><a
	href="projects.jsp">My Projects</a></strong>&nbsp;&nbsp;&nbsp;&nbsp;<span
	id="help-dialog-opener"><a href="">Help</a></span>&nbsp;&nbsp;&nbsp;&nbsp;<span
	id="about-dialog-opener"><a href="">About</a></span>&nbsp;&nbsp;&nbsp;&nbsp;<a
	href="logout.jsp">Logout</a></div>
</div>
<!-- end #header-right --></div>
<!-- end #header -->

<div class="clear"></div>

<div id="main">
<div id="edit-account-container">
<h2>Edit account: <%=username%></h2>
<br />
<div id="edit-account-container-small">
<form id="edit-account-form-name-and-email" action="OliveServlet" name="process"
	method="post">
<p><label for="new-name">Name</label> <input type="text"
	name="new-name" id="new-name" size="32" maxlength="32" /></p>
<p><label for="new-email">Email</label> <input type="text"
	name="new-email" id="new-email" size="32" maxlength="64" /></p>
<input type="hidden" name="FormName" value="EditUser-NameEmail"></input> <input
	type="submit" value="Update Name and Email" /><span><%=editNameEmailConfirmation%></span></form>
</div>

<div id="edit-account-container-small">
<form id="edit-account-form-password" action="OliveServlet" name="process"
	method="post">
<p><label for="new-password">Password</label> <input type="password"
	name="new-password" id="new-password" size="32" maxlength="128" /></p>
<p><label for="confirm-new-password">Confirm password</label> <input
	type="password" name="confirm-new-password" id="confirm-new-password"
	size="32" maxlength="128" /></p>
	<input type="hidden" name="FormName" value="EditUserPassword"></input> <input
	type="submit" value="Update password" /><span><%=editPasswordConfirmation%></span></form>
</div>

<div id="edit-account-container-small">
<form id="edit-account-form-security" action="OliveServlet" name="process"
	method="post">
<p><label for="new-security-question">Security Question</label> <input
	type="text" name="new-security-question" id="new-security-question"
	size="32" maxlength="128" /></p>
<p><label for="new-security-answer">Security Answer</label> <input
	type="text" name="new-security-answer" id="new-security-answer"
	size="32" maxlength="128" /></p>
<input type="hidden" name="FormName" value="EditUserSecurity"></input> <input
	type="submit" value="Update Security Q&A" /><span><%=editSecurityConfirmation%></span></form>
</div>

</div>

<!-- end #edit-account-container -->
<div id="delete-account">
<p><small><a id="delete-account"
	class="warning delete-project">Delete account</a></small></p>
</div>

<!-- end #main --></div>

<div class="clear"></div>

<!-- Everything below this line will be hidden and inserted in pop-ups. -->
<div id="shared-dialogs" class="hidden"></div>

<div id="confirm-delete-account-dialog" class="hidden" title="Warning!">
<p>Delete account? This will also delete the account's projects and
videos.</p>
</div>

</body>
</html>
