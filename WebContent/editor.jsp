<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.readytalk.olive.logic.DatabaseApi"%>
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
<script src="/olive/scripts/contextMenu.js"></script>
<script src="/olive/scripts/jquery.editable-1.3.3.min.js"></script>
<script src="/olive/scripts/master.js"></script>
<script src="/olive/scripts/editor.js"></script>
<script src="/olive/scripts/google-analytics.js"></script>
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

		int accountId = DatabaseApi.getAccountId(username);
		int projectId = DatabaseApi
				.getProjectId(projectName, accountId);
		videosHtml = DatabaseApi.populateVideos(projectId);
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
<div id="videos-header"><span id="videos-title"> <%=projectName%>
</span> <span id="videos-controls">
<button id="upload-new-button" type="button"
	class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-state-hover"><span
	class="ui-button-text">Upload New Video</span></button>
</span></div>
<div id="videos"><%=videosHtml%></div>
<!-- end #videos --></div>
<!-- end #videos-container -->

<div class="contextMenu" id="video-context-menu">
<ul>
	<li id="split-video-menu-item">Split Video</li>
</ul>
</div>
<!-- end #context-menu -->

<div id="player-container"><video id="player" preload controls></video></div>

<div class="clear"></div>

<div id="timeline">
<div id="ticks-container"></div>
</div>

<div class="clear"></div>

<div id="export">
<button id="export-button" type="button" disabled="disabled">Export
to Computer</button>

</div>
<!-- end #export --></div>
<!-- end #main -->

<div class="clear"></div>

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
<div id="confirm-delete-video-dialog" class="hidden" title="Warning!">
<p>Delete video?</p>
</div>
<div id="confirm-add-to-timeline-dialog" class="hidden"
	title="Attention!">
<p>Add Video to Timeline</p>
</div>
<div id="invalid-split-dialog" class="hidden" title="Warning">
<p>Please pause the video at a valid split location.</p>
</div>
<!-- end #dialog-form -->

</body>
</html>
