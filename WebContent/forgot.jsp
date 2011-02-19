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
	Boolean isSafe = (Boolean) session.getAttribute(Attribute.IS_SAFE.toString());
	Boolean isCorrect = (Boolean) session.getAttribute(Attribute.IS_CORRECT.toString());
	String editConfirmation = "";
	if(isCorrect == null){
		editConfirmation = "";
	}
	else if(isCorrect == false){
		if(isSafe == null){
			editConfirmation = "";
		}
		else if(isSafe){
			editConfirmation = "Question or answer do not match database records";	
		}
		else{
			editConfirmation = "Unsafe input";		
		}
	}
	else if(isCorrect){
		editConfirmation = "";
		response.sendRedirect("new-password-form.jsp");
	}
	session.removeAttribute(Attribute.IS_SAFE.toString());
	session.removeAttribute(Attribute.IS_CORRECT.toString());
%>
<div id="header">
<div id="header-left">
<h1>Olive</h1>
</div>
<!-- end #header-left -->
<div id="header-right"><span
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
<!-- end #header-right --></div>
<!-- end #header -->

<div class="clear"></div>

<div id="main">
<div id="edit-account-container">
<h2>Forgot Password?</h2>
<p> Please enter your username and your security question and answer. Thank you</p>
<!-- end #about-title -->

<form id="security-question-form" action="OliveServlet" name="process"
	method="post">
	<p><label for="username">Username</label><br />
<input type="text" name="username" id="username" value=""
	size="32" maxlength="32" /></p>
	<p><label for="security_question">Security Question</label><br />	
<input type="text" name="security_question"
	id="security_question" value="" size="32"
	maxlength="128" /></p>
<p><label for="security_answer">Security Answer</label><br />
<input type="text" name="security_answer"
	id="security_question_answer" value="" size="32"
	maxlength="128" /></p>
<input type="hidden" name="FormName" value="security-question-form"></input><br />
<input type="submit" value="Recover Password" /><span><%=editConfirmation%></span>
</form>
</div>
<!-- end #main --></div>

<div id="footer"></div>
</body>
</html>