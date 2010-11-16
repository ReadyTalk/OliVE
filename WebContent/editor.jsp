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
<script type="text/javascript" src="scripts/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="scripts/jquery-ui-1.8.6.custom.min.js"></script>
<script type="text/javascript">
	var _gaq = _gaq || [];
	_gaq.push( [ '_setAccount', 'UA-19623968-1' ]);
	_gaq.push( [ '_trackPageview' ]);

	$(function() {
		var ga = document.createElement('script');
		ga.type = 'text/javascript';
		ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl'
				: 'http://www')
				+ '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(ga, s);

		$("img").draggable();

		$(".timeline").droppable({
		    drop: function() { 
		    	alert('dropped'); 
		    }
		});
				
	})();
</script>
</head>
<body>

<div class="welcome"><a href="#">Logout</a></div>
<div class="welcome">Welcome, User!</div>
<div class="Olive"><img src="/olive/images/logo.jpg" alt="Olive"
	width="115" height="49"></div>
<br />
<br />
<div class="links"><a href="#">Help</a></div>
<div class="links"><b>My Projects</b></div>
<br />

<hr>


<div class="vacation">
<h3>My Vacation</h3>
<div class="button">
<button type="button" onclick="alert('Upload New')">Upload New</button>
<button type="button" onclick="alert('Edit')">Edit</button>
<button type="button" onclick="alert('Delete')">Delete</button>
<button type="button" onclick="alert('Select All')">Select All</button>
</div>
<hr>


<table>
	<tr>
		<td class="images"><img src="/olive/images/olive.png"
			alt="Olive1" height="80" width="80" /></td>
		<td><img src="/olive/images/olive.png" alt="Olive2" height="80"
			width="80" /></td>

	</tr>

	<tr>
		<td><img src="/olive/images/olive.png" alt="Olive3" height="80"
			width="80" /></td>
		<td><img src="/olive/images/olive.png" alt="Olive4" height="80"
			width="80" /></td>
	</tr>
</table>

</div>

<div class="video"><video id="vid1" width="780" height="467"
	poster="http://cdn.kaltura.org/apis/html5lib/kplayer-examples/media/bbb480.jpg"
	controls> <source
	src="http://cdn.kaltura.org/apis/html5lib/kplayer-examples/media/bbb_trailer_iphone.m4v">

<source
	src="http://cdn.kaltura.org/apis/html5lib/kplayer-examples/media/bbb400p.ogv" />

</video></div>
<br />
<br />
<div class="timeline">
<h1>timeline </h1>



</div>

<div class="button2">
<button type="button" onclick="JavaScript:alert('Publish Video')">Publish
Video</button>

</div>

<div id="footer">

<div class="about"><a href="index.html">About Us</a></div>
<div class="copyright">&copy; ReadyTalk</div>

</div>

</body>

</html>