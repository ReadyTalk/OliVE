<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.readytalk.olive.logic.OliveDatabaseApi"%>
<%@ page import="com.readytalk.olive.util.Attribute"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Project | Olive</title>

<link rel="shortcut icon" href="/olive/images/olive.ico">

<link rel="stylesheet" type="text/css" href="/olive/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="/olive/scripts/jquery-ui-1.8.9.custom/css/ui-lightness/jquery-ui-1.8.9.custom.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/master.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/editor.css" />

<script src="/olive/scripts/jquery-1.5.min.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.9.custom/js/jquery-ui-1.8.9.custom.min.js"></script>
<script src="/olive/scripts/master.js"></script>
<script src="/olive/scripts/editor.js"></script>
<script src="/olive/scripts/google-analytics.js"></script>
<script src="/olive/scripts/contextMenu.js"></script>
</head>
<body>
<%
	String username = "";
	Boolean isAuthorized = (Boolean) session
			.getAttribute(Attribute.IS_AUTHORIZED.toString()); // Nasty cast
	String projectName = "";
	String videosHtml = "";
	if (isAuthorized == null) {
		response.sendRedirect("index.jsp");
	} else if (!isAuthorized) {
		response.sendRedirect("index.jsp");
	} else {
		username = (String) session.getAttribute(Attribute.USERNAME
				.toString());
		projectName = (String) session
				.getAttribute(Attribute.PROJECT_NAME.toString());
		if (projectName == null) {
			response.sendRedirect("projects.jsp");
		}

		int accountId = OliveDatabaseApi.getAccountId(username);
		int projectId = OliveDatabaseApi.getProjectId(projectName,
				accountId);
		videosHtml = OliveDatabaseApi.populateVideos(projectId);
	}
%>
<div id="header">
<div id="header-left">
<h1>Olive</h1>
</div>
<!-- end #header-left -->
<div id="header-right">
<div>Welcome, <a href="account.jsp"><%=username%>!</a>&nbsp;<a
	href="logout.jsp">Logout</a></div>
<div><strong><a href="projects.jsp">My Projects</a></strong>&nbsp;<span
	id="help-dialog-opener"><a href="">Help</a></span></div>
</div>
<!-- end #header-right --></div>
<!-- end #header -->

<div class="clear"></div>

<div id="main">

<div id="videos-container">
<div id="videos-title">
<h3><%=projectName%></h3>
</div>
<!-- end #videos-title -->
<div id="videos-controls"><!-- http://stackoverflow.com/questions/1106720/how-to-display-html-form-as-inline-element/1106747#1106747 -->
<button id="upload-new-button" type="button">Upload New Video</button>
<button id="split-button" type="button">Split Video</button>
</div>
<!-- end #videos-controls -->
<div id="videos"><%=videosHtml%></div>
<!-- end #videos --></div>
<!-- end #videos-container -->

<div class="contextMenu" id="video-context-menu">
<ul>
	<li id="split-video-menu-item">Split Video</li>
</ul>
</div>
<!-- end #context-menu -->

<div id="player-div">
<div id="player-container"><video id="player-video"
	preload="preload"></video></div>
<div id="player-controls" class="center-text">
<button id="videos-playpause">Play/pause</button>
<button id="videos-volume-down">Volume down</button>
<button id="videos-volume-up" disabled="disabled">Volume up</button>
</div>
</div>
<!-- end #player -->

<div class="clear"></div>

<div id="timeline">
<div id="ticks-container">
<div id="tick-anchor"></div>
</div>
</div>

<div class="clear"></div>

<div id="export">
<button id="export-button" type="button" disabled="disabled"
	onclick="alert('Export to Computer');">Export to Computer</button>

</div>
<!-- end #export --></div>
<!-- end #main -->

<div class="clear"></div>

<div id="footer"></div>

<!-- Everything below this line will be hidden and inserted in pop-ups. -->
<div id="help-dialog" title="How to use Olive">
<ul>
	<li>1. Create a new account.</li>
	<li>2. Create a new project.</li>
	<li>3. Upload your videos.</li>
	<li>4. Edit your videos.</li>
	<li>5. Export to your computer.</li>
</ul>
</div>
<div id="confirm-delete-video-dialog" title="Warning!">
<p>Delete video?</p>
</div>
<!-- type="number", min, and max are valid in HTML5: http://dev.w3.org/html5/markup/input.number.html -->
<div id="split-video-dialog-form" title="Split video">
<p class="validateTips">All form fields are required.</p>
<form id="split-video-form" action="OliveServlet" name="process"
	method="post">
<fieldset><label for="video-name">Video name</label> <input
	type="text" name="video-name" id="video-name"
	class="text ui-widget-content ui-corner-all" maxlength="32" /> <label
	for="split-time-in-seconds">Split time (in seconds)</label> <input
	type="number" min=0 max=14400 name="split-time-in-seconds"
	id="split-time-in-seconds" value=""
	class="text ui-widget-content ui-corner-all" /></fieldset>
<input type="hidden" name="FormName" value="SplitVideo"></input></form>
</div>
<!-- end #dialog-form -->

</body>
</html>
