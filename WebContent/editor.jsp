<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit Project | Olive</title>
<link rel="shortcut icon" href="/olive/images/olive.ico">
<link rel="stylesheet" type="text/css" href="/olive/css/reset.css">
<link rel="stylesheet" type="text/css" href="/olive/css/style.css" />
<script type="text/javascript" src="/olive/scripts/jquery-1.4.4.js"></script>

<!-- Google Analytics code. Leave intact just above closing head tag. -->
<script type="text/javascript" src="/olive/scripts/jquery-1.4.4.js"></script>
<script type="text/javascript" src="/olive/scripts/jquery-ui-1.8.6.custom/js/jquery-ui-1.8.6.custom.min.js"></script>
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
</script>
<script type="text/javascript">
		$("img").draggable();

		$(".timeline").droppable({
		    drop: function() { 
		    	alert('dropped'); 
		    }
		});

		$("#Olive1").bind('click', function(){
			$('#Olive1').clone().insertAfter("#Olive4").slidedown();
		});

		$("#Olive2").bind('click', function(){
			$('#Olive2').clone().insertAfter("#Olive4");
		});

		$("#Olive3").bind('click', function(){
			$('#Olive3').clone().insertAfter("#Olive4");
		});

		$("#Olive4").bind('click', function(){
			$('#Olive4').clone().insertAfter("#Olive4");
		});
				
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
<table id="clips">

<tr>

<td>
<div class="vacation" style="overflow:scroll;">
<h3>My Vacation</h3>
<div class="button">
<button type="button" onclick="alert('Upload New')">Upload New</button>
<button type="button" onclick="alert('Edit')">Edit</button>
<button type="button" onclick="alert('Delete')">Delete</button>
<button type="button" onclick="alert('Select All')">Select All</button>
</span>

<table>
	<tr>
		<td id = "Olive1" class="images"><img src="/olive/images/olive.png"
			alt="Olive1" height="80" width="80" /></td>
		<td id = "Olive2"><img src="/olive/images/olive.png" alt="Olive2" height="80"
			width="80" /></td>

	</tr>

	<tr>
		<td id = "Olive3"><img src="/olive/images/olive.png" alt="Olive3" height="80"
			width="80" /></td>
		<td id = "Olive4"><img src="/olive/images/olive.png" alt="Olive4" height="80"
			width="80" /></td>
	</tr>
</table>

</div>
</td>
<td>
<div class="video"><video id="vid1" width="500%" height="500%"
	poster="http://cdn.kaltura.org/apis/html5lib/kplayer-examples/media/bbb480.jpg"
	controls> <source
	src="http://cdn.kaltura.org/apis/html5lib/kplayer-examples/media/bbb_trailer_iphone.m4v">

<source
	src="http://cdn.kaltura.org/apis/html5lib/kplayer-examples/media/bbb400p.ogv" />

</video></div>

</td>
</tr>
</table>

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
