<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Olive | The Online Video Editor</title>
<link rel="shortcut icon" href="/olive/images/olive.ico">
<link rel="stylesheet" type="text/css" href="/olive/css/reset.css">
<link rel="stylesheet" type="text/css" href="/olive/css/style.css">
<script src="/olive/scripts/jquery-1.4.4.js"></script>

<!-- Google Analytics code. Leave intact just above closing head tag. -->
<script type="text/javascript">
	var _gaq = _gaq || [];
	_gaq.push( [ '_setAccount', 'UA-19623968-1' ]);
	_gaq.push( [ '_trackPageview' ]);

	(function() {
		var ga = document.createElement('script');
		ga.type = 'text/javascript';
		ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl'
				: 'http://www')
				+ '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(ga, s);
	})();
</script>
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

</div>
<!-- end #main-right --></div>
<!-- end #main -->

<div class="clear"></div>

<div id="footer">
<div id="footer-left"><a href="#">About Us</a></div>
<div id="footer-right">&copy; 2010 ReadyTalk</div>
</div>
</body>
</html>