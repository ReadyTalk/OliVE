<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>My Projects | Olive</title>

<link rel="shortcut icon" href="/olive/images/olive.ico">

<link rel="stylesheet" type="text/css" href="/olive/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="/olive/scripts/jquery-ui-1.8.6.custom/css/ui-lightness/jquery-ui-1.8.6.custom.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/master.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/projects.css" />

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
<script src="/olive/scripts/projects.js"></script>
<script src="/olive/scripts/google-analytics.js"></script>
</head>
<body>
<div id="header">
<div id="header-left">
<h1>Olive</h1>
</div>
<div id="header-right">
<div>Welcome, <a href="account.jsp">User!</a>&nbsp;<a href="#">Logout</a></div>
<div><a href="#">Help</a></div>
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
<button type="button" onclick="alert('Create New Project')">Create
New Project</button>
</div>
<!-- end #controls -->

<div class="clear"></div>

<div id="project-clips">
<div id="project-1" class="project-icon-container"><img
	src="images/wedding.jpg" class="project-icon" alt="My Wedding" />
<p><a href="editor.jsp">My Wedding</a></p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>
<div id="project-2" class="project-icon-container"><img
	src="images/summer-flower.png" class="project-icon" alt="My Summer" />
<p><a href="editor.jsp">My Summer</a></p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>
<div id="project-3" class="project-icon-container"><img
	src="images/vacation.jpg" class="project-icon" alt="My Vacation" />
<p><a href="editor.jsp">My Vacation</a></p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>
<div id="project-4" class="project-icon-container"><img
	src="images/wedding.jpg" class="project-icon" alt="My Wedding" />
<p><a href="editor.jsp">My Other Wedding</a></p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>
<div id="project-5" class="project-icon-container"><img
	src="images/summer-flower.png" class="project-icon" alt="My Summer" />
<p><a href="editor.jsp">My Other Summer</a></p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>
<div id="project-6" class="project-icon-container"><img
	src="images/vacation.jpg" class="project-icon" alt="My Vacation" />
<p><a href="editor.jsp">My Other Vacation</a></p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>
<div id="project-7" class="project-icon-container"><img
	src="images/wedding.jpg" class="project-icon" alt="My Wedding" />
<p><a href="editor.jsp">Yet Another Wedding</a></p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>
<div id="project-8" class="project-icon-container"><img
	src="images/summer-flower.png" class="project-icon" alt="My Summer" />
<p><a href="editor.jsp">Yet Another Summer</a></p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>
<div id="project-9" class="project-icon-container"><img
	src="images/vacation.jpg" class="project-icon" alt="My Vacation" />
<p><a href="editor.jsp">Yet Another Vacation</a></p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>

</div>
<!-- end #project-clips --></div>
<!-- end #projects-container --></div>
<!-- end #main -->

<div class="clear"></div>

<div id="footer">
<div id="footer-left"><a href="#">About Us</a></div>
<div id="footer-right">&copy; 2010 ReadyTalk</div>
</div>
</body>
</html>