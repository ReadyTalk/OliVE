<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.readytalk.olive.util.Attribute"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Forgot Password? | The Online Video Editor</title>

<link rel="shortcut icon" href="/olive/images/olive.ico" />

<link rel="stylesheet" type="text/css" href="/olive/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="/olive/scripts/jquery-ui-1.8.9.custom/css/custom-theme/jquery-ui-1.8.11.custom.css"/>
<link rel="stylesheet" type="text/css" href="/olive/css/master.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/account.css" />
<link href='http://fonts.googleapis.com/css?family=Lato' rel='stylesheet' type='text/css'>

<script src="/olive/scripts/jquery-1.5.min.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.9.custom/js/jquery-ui-1.8.9.custom.min.js"></script>
<script src="/olive/scripts/master.js"></script>
<script src="/olive/scripts/account.js"></script>
<script src="/olive/scripts/google-analytics.js"></script>
</head>
<body>
<%
	Boolean isSafe = (Boolean) session.getAttribute(Attribute.IS_SAFE
			.toString());
	Boolean passwordsMatch = (Boolean) session
			.getAttribute(Attribute.PASSWORDS_MATCH.toString());
	Boolean editSuccess = (Boolean) session
			.getAttribute(Attribute.EDIT_SUCCESSFULLY.toString());
	String confirmation = "";
	String index = "'index.jsp'";
	if (editSuccess == null) {
		confirmation = "";
	} else if (editSuccess == false) {
		if (isSafe) {
			if (passwordsMatch) {
				confirmation = "We're sorry, there is an error in inputting to the database. Please try again";
			} else {
				confirmation = "Passwords do not match";
			}
		} else {
			confirmation = "Unsafe input";
		}
	} else if (editSuccess) {
		confirmation = "Congratulations. Your new password has been set. You "
				+ "may now sign in to <a href = "
				+ index
				+ ">Olive</a>";
	}
	session.removeAttribute(Attribute.IS_CORRECT.toString());
	session.removeAttribute(Attribute.IS_SAFE.toString());
	session.removeAttribute(Attribute.PASSWORDS_MATCH.toString());
	session.removeAttribute(Attribute.EDIT_SUCCESSFULLY.toString());
%>
<div id="header">
<div id="header-left">
<img id="olive-icon"
	src="/olive/images/olive.png" />
<h1 id= "olive-title">Olive</h1>
</div>
<!-- end #header-left -->
<div id="header-right"><span id="help-dialog-opener"><a
	href="">Help</a></span></div>
<!-- end #header-right --></div>
<!-- end #header -->

<div class="clear"></div>

<div id="main">
<div id="edit-account-container">
<h2>New Password</h2>
<p>Please enter a new password for your account</p>
<!-- end #about-title -->

<form id="password-form" action="OliveServlet" name="process"
	method="post">
<p><label for="password">Password</label><br />
<input type="password" name="password" id="password" value="" size="32"
	maxlength="128" /></p>
<p><label for="password">Confirm Password</label><br />
<input type="password" name="confirm_password" id="confirm_password"
	value="" size="32" maxlength="128" /></p>
<input type="hidden" name="FormName" value="new_password"></input><br />
<input type="submit" value="Submit New Password" /><span><%=confirmation%></span></form>
</div>
<!-- end #main --></div>

<div id="footer"></div>

<!-- Everything below this line will be hidden and inserted in pop-ups. -->
<div id="help-dialog" class="hidden" title="How to use Olive">
<ul>
	<li>1. Create a new account.</li>
	<li>2. Create a new project.</li>
	<li>3. Upload your videos.</li>
	<li>4. Edit your videos.</li>
	<li>5. Export to your computer.</li>
</ul>
</div>

</body>
</html>