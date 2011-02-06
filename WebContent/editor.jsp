<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.readytalk.olive.util.Attribute"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Project | Olive</title>

<link rel="shortcut icon" href="/olive/images/olive.ico">

<link rel="stylesheet" type="text/css" href="/olive/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="/olive/scripts/jquery-ui-1.8.6.custom/css/ui-lightness/jquery-ui-1.8.6.custom.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/master.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/editor.css" />

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
<script
	src="/olive/scripts/jquery-ui-1.8.6.custom/development-bundle/ui/jquery.ui.sortable.js"></script>
<script src="/olive/scripts/master.js"></script>
<script src="/olive/scripts/editor.js"></script>
<script src="/olive/scripts/google-analytics.js"></script>
<script src="/olive/scripts/contextMenu.js"></script>
</head>
<body>
<%
	String user = "";
	String projectTitle = "";
	Boolean isAuthorized = (Boolean) session
			.getAttribute(Attribute.IS_AUTHORIZED.toString()); // Nasty cast
	if (isAuthorized == null) {
		response.sendRedirect("index.jsp");
	} else if (!isAuthorized) {
		response.sendRedirect("index.jsp");
	} else {
		user = (String) session.getAttribute(Attribute.USERNAME
				.toString());
		projectTitle = (String) session
				.getAttribute(Attribute.PROJECT_TITLE.toString());
		if (projectTitle == null) {
			response.sendRedirect("projects.jsp");
		}
	}
%>
<div id="header">
<div id="header-left">
<h1>Olive</h1>
</div>
<!-- end #header-left -->
<div id="header-right">
<div>Welcome, <a href="account.jsp"><%=user%>!</a>&nbsp;<a
	href="logout.jsp">Logout</a></div>
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

<div id="videos-container">
<div id="videos-title">
<h3><%=projectTitle%></h3>
</div>
<!-- end #videos-title -->
<div id="videos-controls"><!-- http://stackoverflow.com/questions/1106720/how-to-display-html-form-as-inline-element/1106747#1106747 -->

<!-- This should be refactored to be multiple forms, but then the CSS should be
	changed so the buttons all stay on the same line. -->
<form id="split-form" action="OliveServlet" name="process" method="post">
<input type="button" value="Upload New" onclick="openNewVideoForm();" />
<input type="submit" value="Split" onclick="alert('Split');" /> <input
	type="hidden" name="FormName" value="SplitVideo"></input> <input
	type="button" value="Delete" onclick="alert('Delete');" /> <input
	type="button" value="Select All" onclick="alert('Select All');" /></form>
</div>
<!-- end #videos-controls -->
<div id="videos"><!-- div id="video-1" class="video-icon-container"  img id="olive1"
	class="video-icon" src="/olive/images/olive.png" alt="olive1" / 
p Video 1 /p
p small  a href="" class="warning" Delete /a  /small  /p 
 /div--> <span id="video-2" class="video-icon-container"><img
	id="olive2" class="video-icon" src="/olive/images/olive.png"
	alt="olive2" /><br />
Video 2<br />
<small><a href="" class="warning">Delete</a></small> </span> <span id="video-3"
	class="video-icon-container"><img id="olive3" class="video-icon"
	src="/olive/images/olive.png" alt="olive3" /><br />
Video 3<br />
<small><a href="" class="warning">Delete</a></small> </span> <span id="video-4"
	class="video-icon-container"><img id="olive4" class="video-icon"
	src="/olive/images/olive.png" alt="olive4" /><br />
Video 4<br />
<small><a href="" class="warning">Delete</a></small> </span></div>
<!-- end #videos --></div>
<!-- end #videos-container -->

<div class="contextMenu" id="videoMenu">
<ul>
	<li id="split">Split Video</li>
</ul>
</div>
<!-- end #contextMenu -->

<div id="player-div">
<div id="player-container"><video id="player-video"
	poster="/olive/images/bbb480.jpg" preload="preload"
	src="/olive/videos/bbb_trailer_iphone.m4v"></video></div>
<div id="player-controls" class="center-text">
<button id="videos-playpause">Play/pause</button>
<button id="videos-volume-down">Volume down</button>
<button id="videos-volume-up" disabled="disabled">Volume up</button>
</div>
</div>
<!-- end #player -->

<div class="clear"></div>

<div id="timeline">

<div id="video-5" class="video-icon-container">
<p><img id="olive5" class="video-icon" src="/olive/images/olive.png"
	alt="olive5" /></p>
Video 5<br />
<small><a href="" class="warning">Delete</a></small></div>
<div id="video-6" class="video-icon-container">
<p><img id="olive6" class="video-icon" src="/olive/images/olive.png"
	alt="olive6" /></p>
Video 6<br />
<small><a href="" class="warning">Delete</a></small></div>
</div>

<div class="clear"></div>

<div id="export">
<button type="button" onclick="alert('Export Video')">Export to
Computer</button>

</div>
<!-- end #export --></div>
<!-- end #main -->

<div class="clear"></div>

<div id="footer"></div>
</body>
</html>
