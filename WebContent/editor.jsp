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
	href="/olive/scripts/jquery-ui-1.8.9.custom/css/custom-theme/jquery-ui-1.8.11.custom.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/master.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/editor.css" />
<link href='http://fonts.googleapis.com/css?family=Lato: regular,bold'
	rel='stylesheet' type='text/css'>

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
	Boolean isAuthorized = (Boolean) session
			.getAttribute(Attribute.IS_AUTHORIZED.toString()); // Nasty cast
	String username = "";
	String projectName = "";
	if (isAuthorized == null || !isAuthorized) { // Short-circuiting
		response.sendRedirect("index.jsp");
	} else {
		projectName = (String) session
				.getAttribute(Attribute.PROJECT_NAME.toString());
		if (projectName == null) {
			response.sendRedirect("projects.jsp");
		} else {
			username = (String) session.getAttribute(Attribute.USERNAME
					.toString());
		}
	}
%>
<div id="header">
<div id="header-left"><img id="olive-icon"
	src="/olive/images/olive.png" />
<h1 id="olive-title">Olive</h1>
</div>
<!-- end #header-left -->
<div id="header-right">
<div><strong><a href="projects.jsp">Projects</a></strong>&nbsp;&nbsp;&nbsp;&nbsp;<a
	href="account.jsp">Account</a>&nbsp;&nbsp;&nbsp;&nbsp;<span
	id="help-dialog-opener"><a href="">Help</a></span>&nbsp;&nbsp;&nbsp;&nbsp;<span
	id="about-dialog-opener"><a href="">About</a></span>&nbsp;&nbsp;&nbsp;&nbsp;<a
	href="logout.jsp">Logout</a></div>
</div>
<!-- end #header-right --></div>
<!-- end #header -->

<div class="clear"></div>

<div id="main">

<div id="videos-header"><span id="videos-title"> <%=projectName%>
</span><span id="videos-controls">
<button id="upload-new-video-button" type="button"
	class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-state-hover"><span
	class="ui-button-text">Upload New Video</span></button>
</span></div>

<div id="videos-container">
<div id="videos"><span id="videos-background-text" class="hidden">Uploaded
videos will appear here</span></div>
<!-- end #videos --></div>
<!-- end #videos-container -->

<div id="player-container"></div>

<div class="clear"></div>

<div id="timeline"><span id="timeline-background-text"
	class="hidden">Drag videos here to be combined</span></div>
<div id="export">
<form id="combine-and-export-form" action="OliveServlet" name="process"
	method="post"><input type="hidden" name="FormName"
	value="combine-form"></input> <input id="export-button" type="submit"
	value="Combine Videos"
	class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only ui-state-hover"
	disabled="disabled"></input></form>
</div>
<!-- end #export --></div>
<!-- end #main -->

<div class="clear"></div>

<!-- Everything below this line will be hidden and inserted in pop-ups. -->
<div id="shared-dialogs" class="hidden"></div>

<div id="confirm-delete-video-dialog" class="hidden" title="Warning!">
<p>Delete video?</p>
</div>

<div id="confirm-add-to-timeline-dialog" class="hidden"
	title="Attention!">
<p>Add Video to Timeline</p>
</div>

<div id="invalid-split-dialog" class="hidden" title="Warning">
<p>The video may not be split at the beginning or end.</p>
</div>

<div id="new-video-dialog-form" class="hidden" title="Upload New Video">
<p class="validateTips">All form fields are required.</p>
<form id="new-video-form" action="OliveServlet" name="process"
	enctype="multipart/form-data" method="post">
<fieldset><input type="hidden" name="FormName"
	value="UploadVideo"></input><input type="file" id="new-video-file"
	name="file" /> <br />
<label for="new-video-name">Give the video a new name</label> <input
	type="text" name="new-video-name" id="new-video-name"
	class="text ui-widget-content ui-corner-all" maxlength="32" /></fieldset>
</form>
</div>
<!-- end #new-video-dialog-form -->

<div id="confirm-combine-videos-dialog" class="hidden" title="Confirm action">
<p>Combine videos and export them to your computer?</p>
</div>

</body>
</html>
