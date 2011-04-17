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
<link rel="stylesheet" type="text/css"
	href="/olive/scripts/valums-file-uploader-0c701eb/client/fileuploader.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/master.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/editor.css" />
<link href='http://fonts.googleapis.com/css?family=Lato: regular,bold'
	rel='stylesheet' type='text/css'>

<script src="/olive/scripts/jquery-1.5.min.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.9.custom/js/jquery-ui-1.8.9.custom.min.js"></script>
<script src="/olive/scripts/jquery.editable-1.3.3.min.js"></script>
<script
	src="/olive/scripts/valums-file-uploader-0c701eb/client/fileuploader.js"></script>
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
<div id="header-left"><a href="projects.jsp"><img
	id="olive-icon" src="/olive/images/olive.png" /></a>
<h1 id="olive-title"><a href="projects.jsp">Olive</a></h1>
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

<div id="videos-header">
<div id="videos-title"><%=projectName%></div>
<div id="videos-controls">
<button id="upload-new-video-button" class="hidden" type="button">Upload
New Video</button>
</div>
</div>

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
<button id="export-dialog-opener" class="hidden" disabled="disabled">Combine
Videos</button>
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
<p class="validateTips"></p>
<button id="choose-video-button">Upload a video</button>
<div id="fancy-uploader"></div>
<br />
<div id="upload-list"></div>
</div>
<!-- end #new-video-dialog-form -->

<div id="confirm-combine-videos-dialog" class="hidden"
	title="Combine Videos">
<p>Choose a format:</p>
<form id="combine-and-export-form" action="OliveServlet" name="process"
	method="post">
<fieldset><input type="hidden" name="FormName"
	value="combine-form"></input> <input type="radio"
	name="output-extension" value="mp4" class="inline" checked="checked" /><label
	for="mp4" class="inline">.mp4</label><br />
<input type="radio" name="output-extension" value="wmv" class="inline" /><label
	for="wmv" class="inline">.wmv</label><br />
<input type="radio" name="output-extension" value="avi" class="inline" /><label
	for="avi" class="inline">.avi</label><br />
<input type="radio" id="ogv" name="output-extension" value="ogv"
	class="inline" /><label for="ogv" class="inline">.ogv</label></fieldset>
</form>
</div>

<div id="combine-pending-dialog" class="hidden"
	title="Combining videos...">
<p>Please wait while your videos are being combined. This may take
several minutes.</p>
</div>

<div id="preloader-videos" class="hidden"></div>

</body>
</html>
