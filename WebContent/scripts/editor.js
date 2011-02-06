/*
 * This is Olive's JavaScript file for editor.jsp only.
 */

var video; // Global

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	video = document.getElementById('player-video');

	$('#videos-playpause').click(function() {
		if (video.paused) {
			video.play();
		} else {
			video.pause();
		}
	});

	$('#videos-volume-up').click(function() {
		if (video.volume < 0.85) {	// Account for rounding errors
			video.volume += 0.1;
		} else {
			video.volume = 1.0;	// Don't allow rounding errors
			$('#videos-volume-up').attr('disabled', 'disabled');	// Disable
		}
		$('#videos-volume-down').removeAttr('disabled');			// Enable
	});

	$('#videos-volume-down').click(function() {
		if (video.volume > 0.15) {	// Account for rounding errors
			video.volume -= 0.1;
		} else {
			video.volume = 0.0;	// Don't allow rounding errors
			$('#videos-volume-down').attr('disabled', 'disabled');	// Disable
		}
		$('#videos-volume-up').removeAttr('disabled');				// Enable
	});

	// Modified from: http://jqueryui.com/demos/draggable/
	$('.video-container').draggable( {
		appendTo : 'body',
		scroll : false,
		connectToSortable : '#timeline',
		helper : 'clone',
		revert : 'invalid',
		snap : '#timeline'
	});

	$('#timeline').sortable( {
		revert : true
	});

	$('.video-container').contextMenu('videoMenu', {
		menuStyle : {
			border : '1px solid #000'
		},
		itemStyle : {
			fontFamily : 'verdana',
			backgroundColor : '#fff',
			color : 'black',
			border : 'none',
			padding : '1px'
		},
		itemHoverStyle : {
			color : '#fff',
			backgroundColor : '#00c',
			border : 'none'
		},
		bindings : {
			'split' : function(t) {
				alert('Split Video');
				console.log('Split');
			}
		}
	});

});
function openNewVideoForm() {
	window.open("new-video-form.jsp", "videoUploadForm",
			"menubar=no,width=320,height=200,toolbar=no");
}