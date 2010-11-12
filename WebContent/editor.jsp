<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Project | Olive</title>
<link rel="shortcut icon" href="/olive/images/olive.ico">
<link rel="stylesheet" type="text/css" href="/olive/css/reset.css">
<link rel="stylesheet" type="text/css" href="/olive/css/style.css" />

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
<div id="vacation">

<h3 class="center-text">My Vacation</h3>
<span class="center-text">
<button type="button" onclick="alert('Upload New')">Upload New</button>
<button type="button" onclick="alert('Edit')">Edit</button>
<button type="button" onclick="alert('Delete')">Delete</button>
<button type="button" onclick="alert('Select All')">Select All</button>
</span>

<div id="clips"><span><img src="/olive/images/olive.png"
	alt="Clip 1" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 2" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 3" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 4" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 5" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 6" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 7" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 8" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 9" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 10" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 11" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 12" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 13" height="80" width="80" /><img
	src="/olive/images/olive.png" alt="Clip 14" height="80" width="80" /></span></div>
</div>

<div id="player"><video id="vid1" width="780" height="467"
	poster="http://cdn.kaltura.org/apis/html5lib/kplayer-examples/media/bbb480.jpg"
	controls> <source
	src="http://cdn.kaltura.org/apis/html5lib/kplayer-examples/media/bbb_trailer_iphone.m4v">

<source
	src="http://cdn.kaltura.org/apis/html5lib/kplayer-examples/media/bbb400p.ogv" />

</video></div>

<div class="clear"></div>

<div id="timeline">
<h1>Timeline</h1>
</div>

<div class="clear"></div>

<div id="export">
<button type="button" onclick="JavaScript:alert('Export Video')">Export
Video</button>

</div>
</div>
<!-- end #main -->

<div class="clear"></div>

<div id="footer">
<div id="footer-left"><a href="#">About Us</a></div>
<div id="footer-right">&copy; 2010 ReadyTalk</div>
</div>
</body>
</html>