/*
 * This is Olive's JavaScript file for editor.jsp only.
 * Dependencies: "/olive/scripts/master.js"
 */

var deleteVideoDialogContext;	// TODO Remove this global variable.
var video; // Global

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	attachDeleteVideoHandlers();
	attachVideoMenuHandlers();
	attachVideoClickHandlers();
	attachPlayerHandlers();
	enableDragAndDrop();
	attachContextMenuHandlers();
	downloadVideosToTemp();
});

function attachDeleteVideoHandlers() {
	$('.delete-video').click(function () {
		$('#confirm-delete-video-dialog').dialog('open');
		deleteVideoDialogContext = this;	// This is a global variable.
	});
	
	$('#confirm-delete-video-dialog').dialog({
		autoOpen: false,
		resizable: false,
		height: 275,
		modal: true,
		buttons: {
			'Delete': function () {
				deleteVideo.call(deleteVideoDialogContext);	// We don't want the context to be the dialog element, but rather the element that triggered it.
				$(this).dialog('close');
			},
			Cancel: function () {
				$(this).dialog('close');
			}
		}
	});
}

function attachVideoMenuHandlers() {
	$('#upload-new-button').click(function () {
		openNewVideoForm();
	});
	
	attachSplitHandlers();
	$('#split-button').click(function() {
		$('#split-video-dialog-form').dialog('open');
	});
}

function attachVideoClickHandlers() {
	$('.video-container').click(function () {
		var id = $(this).attr('id');
		if ($(this).data('isSelected')) {
			$(this).data('isSelected', false);
			$(this).css( {
				'background-color': ''
			});
			removeFromSelected(id);
			swapOutVideoInPlayer();
		} else {
			$(this).data('isSelected', true);
			$(this).css( {
				'background-color': '#edf4e6'	// A lighter version of the Olive color
			});
			addToSelected(id);
			swapOutVideoInPlayer();
		}
	});
}

//Perform an addToSelected request
function addToSelected(id) {
	var videoName = id;	// TODO This works by definition (but the definition should probably change).
	var data = '{'
		+    '"command" : "addToSelected",'
		+    '"arguments" : {'
		+        '"video" : "' + videoName + '"'
		+      '}'
		+  '}';
	makeAjaxPostRequest(data, false);	// Defined in "/olive/scripts/master.js".
}

// Perform a removeFromSelected request
function removeFromSelected(id) {
	var videoName = id;	// TODO This works by definition (but the definition should probably change).
	var data = '{'
		+    '"command" : "removeFromSelected",'
		+    '"arguments" : {'
		+        '"video" : "' + videoName + '"'
		+      '}'
		+  '}';
	makeAjaxPostRequest(data, false);	// Defined in "/olive/scripts/master.js".
}

// Video tag codecs: http://www.webmonkey.com/2010/02/embed_audio_and_video_in_html_5_pages/
// Also: http://stackoverflow.com/questions/2425218/html5-video-tag-in-chrome-wmv
function swapOutVideoInPlayer() {
	if (hasVideoChanged()) {
		$('#player-video').attr('type', getType());
		$('#player-video').attr('poster', getPoster());
		$('#player-video').attr('src', getSrc());
	}
}

function hasVideoChanged() {
	return true;	// TODO Calculate this.
}

function getType() {
	return 'video/mp4';
}

function getPoster() {
	return '/olive/images/bbb480.jpg';
}

function getSrc() {
	return '/olive/videos/bbb_trailer_iphone.m4v';
}

function attachPlayerHandlers() {
	video = document.getElementById('player-video');

	$('#videos-playpause').click(function () {
		if (video.paused) {
			video.play();
		} else {
			video.pause();
		}
	});
	
	$('#videos-volume-up').click(function () {
		if (video.volume < 0.85) { // Account for rounding errors
			video.volume += 0.1;
		} else {
			video.volume = 1.0; // Don't allow rounding errors
			$('#videos-volume-up').attr('disabled', 'disabled'); // Disable
		}
		$('#videos-volume-down').removeAttr('disabled'); // Enable
	});

	$('#videos-volume-down').click(function () {
		if (video.volume > 0.15) { // Account for rounding errors
			video.volume -= 0.1;
		} else {
			video.volume = 0.0; // Don't allow rounding errors
			$('#videos-volume-down').attr('disabled', 'disabled'); // Disable
		}
		$('#videos-volume-up').removeAttr('disabled'); // Enable
	});
}

function enableDragAndDrop() {
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
		revert : true,
		sort: function() {
			if($('#timeline').sortable('items').length > 0){
				$('#export-button').removeAttr('disabled');
			} else {
				$('#export-button').attr('disabled', 'disabled');
			}
		}
	});
}

function attachContextMenuHandlers() {
	$('.video-container').contextMenu('video-context-menu', {
		menuStyle : {
			border : '1px solid #000'
		},
		itemStyle : {
			fontFamily : 'Arial',
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
			'split-video-menu-item' : function (t) {
				$('#split-video-dialog-form').dialog('open');
			}
		}
	});
}

// Perform a deleteVideo request
function deleteVideo() {
	var data = '{'
			+    '"command" : "deleteVideo",'
			+    '"arguments" : {'
			+        '"video" : "' + $(this).attr('id') + '"'
			+      '}'
			+  '}';
	makeAjaxPostRequest(data, true);	// Defined in "/olive/scripts/master.js".
}

//Perform a splitVideo request
function splitVideo(videoName, splitTimeInSeconds) {
	var data = '{'
			+    '"command" : "splitVideo",'
			+    '"arguments" : {'
			+        '"video" : "' + videoName + '",'
			+        '"splitTimeInSeconds" : ' + splitTimeInSeconds + ''
			+      '}'
			+  '}';
	makeAjaxPostRequest(data, true);	// Defined in "/olive/scripts/master.js".
}

function attachSplitHandlers() {
	var videoName = $('#video-name'), splitTimeInSeconds = $('#split-time-in-seconds'), allFields = $(
			[]).add(videoName).add(splitTimeInSeconds), tips = $('.validateTips');

	function updateTips(t) {
		tips.text(t).addClass("ui-state-highlight");
		setTimeout(function() {
			tips.removeClass("ui-state-highlight", 1500);
		}, 500);
	}

	function checkLength(o, n, min, max) {
		if (o.val().length > max || o.val().length < min) {
			o.addClass("ui-state-error");
			updateTips("Length of " + n + " must be between " + min + " and "
					+ max + ".");
			return false;
		} else {
			return true;
		}
	}

	function checkRegexp(o, regexp, n) {
		if (!(regexp.test(o.val()))) {
			o.addClass("ui-state-error");
			updateTips(n);
			return false;
		} else {
			return true;
		}
	}
	
	$('#split-video-dialog-form').dialog({
		autoOpen : false,
		height : 400,
		width : 400,
		modal : true,
		buttons : {
			'Split video' : function() {
				var bValid = true;
				allFields.removeClass('ui-state-error');

				bValid = bValid
						&& checkLength(videoName,
								'video-name', 1, 32);
				bValid = bValid
						&& checkLength(splitTimeInSeconds, 'split-time-in-seconds',
								0, 14400);
				bValid = bValid
						&& checkRegexp(videoName,
								/^[a-z]([0-9a-z_])+$/i,
								'Video name may consist of a-z, 0-9, underscores; and must begin with a letter.');
				bValid = bValid
						&& checkRegexp(splitTimeInSeconds,
								/^([0-9]*[.]?[0-9]+)$/,
								'Split time must be a number');
				if (bValid) {
					splitVideo(videoName.val(), splitTimeInSeconds.val());
					$(this).dialog("close");
				}
			},
			Cancel : function() {
				window.location.reload(true);
				$(this).dialog('close');
			}
		},
		close : function() {
			allFields.val('').removeClass('ui-state-error');
		}
	});
}

function downloadVideosToTemp() {
	var data = '{'
		+    '"command" : "downloadVideosToTemp"'
		+  '}';
	makeAjaxPostRequest(data, false);	// Defined in "/olive/scripts/master.js".
}

function openNewVideoForm() {
	window.open("new-video-form.jsp", "videoUploadForm",
			"menubar=no,width=320,height=200,toolbar=no");
}