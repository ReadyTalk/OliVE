<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>My Projects | Olive</title>

<link rel="shortcut icon" href="/olive/images/olive.ico">
<link rel="stylesheet" type="text/css" href="/olive/css/reset.css">
<link rel="stylesheet" type="text/css" href="/olive/css/style.css">

<script src="/olive/scripts/jquery-1.4.4.js"></script>
<script src="/olive/scripts/script.js"></script>
<script src="/olive/scripts/google-analytics.js"></script>
</head>
<body>
<div id="header">
<div id="header-left">
<h1>Olive</h1>
</div>
<!-- end #header-left -->
<div id="header-right">
<div>Welcome, User!&nbsp;<a href="#">Logout</a></div>
<div><strong>My Projects</strong>&nbsp;<a href="#">Help</a></div>
</div>
<!-- end #header-right --></div>
<!-- end #header -->

<div class="clear"></div>

<div id="main">
<div id="main-center">

<div id="title">
<h1>My Projects</h1>
</div>
<!-- end #title -->

<div id="projects">
<table>
	<tr>
		<td class="images"><img src="images/wedding.jpg" alt="My Wedding"
			height="80" width="80" /></td>
		<td><img src="images/summer-flower.png" alt="My Summer"
			height="80" width="80" /></td>
		<td><img src="images/vacation.jpg" alt="My Vacation" height="80"
			width="80" /></td>
	</tr>
	<tr>
		<td><a href="#">My Wedding</a></td>
		<td><a href="#">My Summer</a></td>
		<td><a href="#">My Vacation</a></td>
	</tr>
	<tr>
		<td><a href="#" class="warning">Delete</a></td>
		<td><a href="#" class="warning">Delete</a></td>
		<td><a href="#" class="warning">Delete</a></td>
	</tr>
</table>
</div>
<!-- end #projects -->

<div id="controls">
<button type="button"
	onclick="alert('You clicked the New Project button.')">New
Project</button>
</div>
<!-- end #controls --></div>
<!-- end #main-center --></div>
<!-- end #main -->

<div class="clear"></div>

<div id="footer">
<div id="footer-left"><a href="#">About Us</a></div>
<div id="footer-right">&copy; 2010 ReadyTalk</div>
</div>
</body>
</html>