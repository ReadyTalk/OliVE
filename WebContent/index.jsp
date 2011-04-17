<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.readytalk.olive.util.Attribute"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Olive | The Online Video Editor</title>

<link rel="shortcut icon" href="/olive/images/olive.ico" />

<link rel="stylesheet" type="text/css" href="/olive/css/reset.css" />
<link rel="stylesheet" type="text/css"
	href="/olive/scripts/jquery-ui-1.8.9.custom/css/custom-theme/jquery-ui-1.8.11.custom.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/master.css" />
<link rel="stylesheet" type="text/css" href="/olive/css/index.css" />
<link href='http://fonts.googleapis.com/css?family=Lato: regular,bold'
	rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Molengo'
	rel='stylesheet' type='text/css'>
<link href="/olive/css/skin/jplayer.blue.monday.css" rel="stylesheet"
	type="text/css" />

<script src="/olive/scripts/jquery-1.5.min.js"></script>
<script
	src="/olive/scripts/jquery-ui-1.8.9.custom/js/jquery-ui-1.8.9.custom.min.js"></script>
<script src="/olive/scripts/jquery.jplayer.min.js"></script>
<script src="/olive/scripts/master.js"></script>
<script src="/olive/scripts/index.js"></script>
<script src="/olive/scripts/google-analytics.js"></script>
</head>
<body>
<%
	Boolean isAuthorized = (Boolean) session
			.getAttribute(Attribute.IS_AUTHORIZED.toString()); // Nasty cast
	String loginMessage;
	if (isAuthorized == null) {
		loginMessage = "Please log in.";
	} else if (isAuthorized) {
		loginMessage = "You have been successfully logged in.";
	} else {
		loginMessage = "Incorrect username and/or password.";
	}
%>
<div id="header">
<div id="header-left"><a href="projects.jsp"><img
	id="olive-icon" src="/olive/images/olive.png" /></a>
<h1 id="olive-title"><a href="projects.jsp">Olive</a></h1>
</div>
<!-- end #header-left -->
<div id="header-right"><span id="help-dialog-opener"><a
	href="">Help</a></span></div>
<!-- end #header-right --></div>
<!-- end #header -->

<div class="clear"></div>

<div id="main">
<div id="screencast-container">
<div class="jp-video jp-video-360p">
<div class="jp-type-playlist">
<div id="jquery_jplayer_1" class="jp-jplayer"></div>
<div id="jp_interface_1" class="jp-interface">
<div class="jp-video-play"></div>
<ul class="jp-controls">

	<li><a href="#" class="jp-play" tabindex="1">play</a></li>
	<li><a href="#" class="jp-pause" tabindex="1">pause</a></li>
	<li><a href="#" class="jp-stop" tabindex="1">stop</a></li>
	<li><a href="#" class="jp-mute" tabindex="1">mute</a></li>
	<li><a href="#" class="jp-unmute" tabindex="1">unmute</a></li>
	<li><a href="#" class="jp-previous" tabindex="1">previous</a></li>

	<li><a href="#" class="jp-next" tabindex="1">next</a></li>
</ul>
<div class="jp-progress">
<div class="jp-seek-bar">
<div class="jp-play-bar"></div>
</div>
</div>
<div class="jp-volume-bar">

<div class="jp-volume-bar-value"></div>
</div>
<div class="jp-current-time"></div>
<div class="jp-duration"></div>
</div>
<div id="playlist-container">
<div id="jp_playlist_1" class="jp-playlist">
<ul>
	<!-- The method Playlist.displayPlaylist() uses this unordered list -->
	<li></li>

</ul>
</div>
</div>
</div>
</div>


</div>
<!-- end #screencast-container -->

<div id="login-form-container">
<form id="login-form" action="OliveServlet" name="process" method="post">
<p><label for="username">Username</label> <input type="text"
	name="username" id="login-username" size="32" maxlength="16" /></p>
<p><label for="password">Password</label> <input type="password"
	name="password" id="login-password" size="32" maxlength="128" /></p>
<input type="hidden" name="FormName" value="LoginUser"></input> <input
	type="submit" value="Login" /><br />
<span><%=loginMessage%> <a href="forgot.jsp">Forgot password?</a></span></form>
<p>Don't have an account? <a id="create-user" href="javascript:;"
	title="">Sign up!</a></p>
</div>
<!-- end #login-form-container --></div>
<!-- end #main -->

<!-- Everything below this line will be hidden and inserted in pop-ups. -->
<div id="shared-dialogs" class="hidden"></div>

<div id="dialog-form" class="hidden" title="Create New Account">
<p class="validateTips">All form fields are required.</p>
<form id="register-form">
<fieldset><label for="register-username">Username</label> <input
	type="text" name="register-username" id="register-username"
	class="text ui-widget-content ui-corner-all" maxlength="16" /> <label
	for="register-email">Email</label> <input type="text"
	name="register-email" id="register-email" value=""
	class="text ui-widget-content ui-corner-all" maxlength="64" /> <label
	for="register-password">Password</label> <input type="password"
	name="register-password" id="register-password" value=""
	class="text ui-widget-content ui-corner-all" maxlength="128" /> <label
	for="confirm-register-password">Confirm Password</label> <input
	type="password" name="confirm-register-password"
	id="confirm-register-password" value=""
	class="text ui-widget-content ui-corner-all" maxlength="128" /></fieldset>
</form>
</div>
<!-- end #dialog-form -->

</body>
</html>