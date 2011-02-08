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
<p><label for="security_questions">Security Question</label></p>	
<select name="security_questions">
<option selected="selected">Please select a security Question</option>
<option>What is your first pet's name?</option>
<option>What is your maternal grandmother's maiden name?</option>
<option>What was your dream job as a child?</option>
<option>What city or town did your parents meet?</option>
<option>What is favorite childhood superhero?</option>
<option>In what town was your first job?</option>
<option>What is the last name of your fourth grade teacher?</option>
</select>
<p><label for="security_answer">Security Answer</label><br />
<input type="text" name="security_answer"
	id="security_question_answer" value="" size="32"
	maxlength="128" /></p>
<input type="hidden" name="FormName" value="EditUser"></input><br />
<input type="submit" value="Update information" /><span><%=editConfirmation%></span></form>

</div>
<!-- end #login-form-container --></div>
<!-- end #main -->

<div id="footer"></div>
</body>
</html>