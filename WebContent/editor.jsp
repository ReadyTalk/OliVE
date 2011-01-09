<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
</head>
<body>

<div id="header">
<div id="header-left">
<h1>Olive</h1>
</div>
<!-- end #header-left -->
<div id="header-right">
<div>Welcome, <a href="account.jsp">User!</a>&nbsp;<a href="#">Logout</a></div>
<div><strong><a href="projects.jsp">My Projects</a></strong>&nbsp;<a
	href="#">Help</a></div>
</div>
<!-- end #header-right --></div>
<!-- end #header -->

<div class="clear"></div>

<div id="main">

<div id="videos-container">
<div id="videos-title">
<h3>My Vacation</h3>
</div>
<!-- end #videos-title -->
<div id="videos-controls">
<button type="button" onclick="alert('Upload New')">Upload New</button>
<button type="button" onclick="alert('Edit')">Edit</button>
<button type="button" onclick="alert('Delete')">Delete</button>
<button type="button" onclick="alert('Select All')">Select All</button>
</div>
<!-- end #videos-controls -->
<div id="videos">

<div id="video-1" class="video-icon-container"><img id="olive1"
	class="video-icon" src="/olive/images/olive.png" alt="olive1" />
<p>Video 1</p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>

<div id="video-2" class="video-icon-container"><img id="olive2"
	class="video-icon" src="/olive/images/olive.png" alt="olive2" />
<p>Video 2</p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>

<div id="video-3" class="video-icon-container"><img id="olive3"
	class="video-icon" src="/olive/images/olive.png" alt="olive3" />
<p>Video 3</p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>

<div id="video-4" class="video-icon-container"><img id="olive4"
	class="video-icon" src="/olive/images/olive.png" alt="olive4" />
<p>Video 4</p>
<p><small><a href="" class="warning">Delete</a></small></p>
</div>

</div>
<!-- end #videos -->
<div id="player-videos-controls"><span id="videos-playpause">Play/pause</span>
<span id="videos-volume-up">Volume up</span> <span
	id="videos-volume-down">Volume down</span></div>
</div>
<!-- end #videos-container -->

<div id="player-div"><video id="player-video"
	poster="/olive/images/bbb480.jpg" preload="preload"
	src="/olive/videos/bbb_trailer_iphone.m4v"></video></div>
<!-- end #player -->

<div class="clear"></div>

<div id="timeline">
<ul id="timeline-sortable">
	<li class="timeline-video">Video 5</li>
	<li class="timeline-video">Video 6</li>
</ul>
</div>

<div class="clear"></div>

<div id="export">
<button type="button" onclick="JavaScript:alert('Export Video')">Export
Video</button>

</div>
<!-- end #export --></div>
<!-- end #main -->

<div class="clear"></div>

<div id="footer">
<div id="footer-left"><a href="#">About Us</a></div>
<div id="footer-right">&copy; 2010 ReadyTalk</div>
</div>
</body>
</html>
