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
	attachPlayerHandlers();
	enableDragAndDrop();
	attachContextMenuHandlers();
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
	
	$('#select-all-button').click(function () {
		console.log('Select All');
	});
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
				splitVideo();
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
	makeAjaxPostRequest(data);	// Defined in "/olive/scripts/master.js".
}

//Perform a splitVideo request
function splitVideo() {
	var data = '{'
			+    '"command" : "splitVideo",'
			+    '"arguments" : {'
			+        '"video" : "' + 'valid' + '",'
			+        '"splitTimeInSeconds" : ' + 1.5 + ''
			+      '}'
			+  '}';
	makeAjaxPostRequest(data);	// Defined in "/olive/scripts/master.js".
}

function openNewVideoForm() {
	window.open("new-video-form.jsp", "videoUploadForm",
			"menubar=no,width=320,height=200,toolbar=no");
}