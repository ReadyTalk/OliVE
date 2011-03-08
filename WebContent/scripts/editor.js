/*
 * This is Olive's JavaScript file for editor.jsp only.
 * Dependencies: "/olive/scripts/master.js"
 */

var deleteVideoDialogContext;	// TODO Remove this global variable.
var videoTimeline;
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
	attachAddToTimelineHandlers();
	getVideoInformation();
	attachPublishButtonHandler();
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

function attachPublishButtonHandler(){
	$('#export-button').click(function(){
		$('confirm-splice').dialog('open');
	});
	$('#confirm-splice').dialog({
		autoOpen: false,
		resizable: false,
		height: 275,
		modal: true,
		buttons: {
			'Yes': function () {
				combineVideos();
				$(this).dialog('close');
			},
			'No': function () {
				$(this).dialog('close');
			}
		}
	});
	
}

function combineVideos(){
	var requestData = '{'
		+    '"command" : "combineVideos",'
		+    '"arguments" : {'
		+    '}'
		+  '}';
	makeAjaxPostRequest(requestData, function (responseData) {location.reload();}, null);	
}

function attachVideoClickHandlers() {
	$('.video-container').click(function () {
		if ($(this).data('isSelected')) {
			unselect(this);
		} else {
			// First, unselect all
			$('.video-container').each(function () {
				unselect(this);	// 'this' is a different 'this' than outside .each()
			});
			// Then, select this
			select(this);
		}
	});
}

function makeSelectionVisible(element) {
	if ($(element).data('isSelected')) {
		$(element).css( {
			'background-color': '#edf4e6'	// A lighter version of the Olive color
		});
		updatePlayerWithNewElement(element);
	} else {
		$(element).css( {
			'background-color': ''
		});
		updatePlayerWithNoElements();
	}
}

function select(element) {
	$(element).data('isSelected', true);
	makeSelectionVisible(element);
	addToSelected($(element).attr('id'));
}

function unselect(element) {
	$(element).data('isSelected', false);
	makeSelectionVisible(element);
	removeFromSelected($(element).attr('id'));
}

//Perform an addToSelected request
function addToSelected(id) {
	var videoName = id;	// TODO This works by definition (but the definition should probably change).
	var requestData = '{'
		+    '"command" : "addToSelected",'
		+    '"arguments" : {'
		+        '"video" : "' + videoName + '"'
		+    '}'
		+  '}';
	makeAjaxPostRequest(requestData, null, null);	// Defined in "/olive/scripts/master.js".
}

// Perform a removeFromSelected request
function removeFromSelected(id) {
	var videoName = id;	// TODO This works by definition (but the definition should probably change).
	var requestData = '{'
		+    '"command" : "removeFromSelected",'
		+    '"arguments" : {'
		+        '"video" : "' + videoName + '"'
		+    '}'
		+  '}';
	makeAjaxPostRequest(requestData, null, null);	// Defined in "/olive/scripts/master.js".
}

// Video tag codecs: http://www.webmonkey.com/2010/02/embed_audio_and_video_in_html_5_pages/
// Also: http://stackoverflow.com/questions/2425218/html5-video-tag-in-chrome-wmv
function updatePlayerWithNewElement(element) {
	$('#player-video').attr('poster', $(element).data('icon'));
	$('#player-video').append(
			'<source src="' + $(element).data('url')
			+ '" type="' + 'video/ogg; codecs=theora,vorbis'	// TODO Get this from the database.
			+ '" />');
}

function updatePlayerWithNoElements() {
	$('#player-video source').remove();
	$('#player-video').removeAttr('poster');
}

// Modified from: http://dev.opera.com/articles/view/everything-you-need-to-know-about-html5-video-and-audio/
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
	/*$('.video-container').draggable( {
		appendTo : '#timeline',
		scroll : false,
		connectToSortable : '#timeline',
		helper : 'clone',
		revert : 'invalid',
		snap : '#timeline'
	});*/
	
	$('#videos').sortable( {
		appendTo: 'body',
		connectWith: '#timeline',
		helper: 'clone',
		items: 'span',
		revert: true,
		scroll: false,
		tolerance: 'pointer',
		update: function(event, ui) {
			updateVideosPosition();
		}
	});
	
	$('#timeline').sortable( {
		appendTo: 'body',
		connectWith: '#videos',
		helper: 'clone',
		items: 'span',
		revert: true,
		scroll: false,
		tolerance: 'pointer',
		update: function(event, ui) {
		updateTimelinePosition();
		},
		sort: function() {
			if($('#timeline').sortable('items').length > 0){
				$('#export-button').removeAttr('disabled');
			} else {
				$('#export-button').attr('disabled', 'disabled');
			}
		}
	});
}

// Perform an update<command>Position request
function updateCollectionPosition(command, collectionItems) {
	var requestData = '{'
		+    '"command" : "' + command + '",'
		+    '"arguments" : {'
		+      '"videos" : [';
		
	if ($(collectionItems).length > 0) {
		$(collectionItems).each(function(index) {
			requestData += '{'
			+          '"video" : "' + $(this).attr('id') + '",'
			+          '"position" : ' + index
			+        '},';	// This will result in an extra comma.
		});
		
		// Strip off the extra comma.
		requestData = requestData.substring(0, requestData.length - 1);
	}	
	
	requestData += ']}}';
	
	makeAjaxPostRequest(requestData, null, null);	// Defined in "/olive/scripts/master.js".
}

// Perform an updateVideosPosition request
function updateVideosPosition() {
	updateCollectionPosition('updateVideosPosition', '#videos span');
}

// Perform an updateTimelinePosition request
function updateTimelinePosition() {
	updateCollectionPosition('updateTimelinePosition', '#timeline span');
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
	var requestData = '{'
			+    '"command" : "deleteVideo",'
			+    '"arguments" : {'
			+        '"video" : "' + $(this).attr('id') + '"'
			+    '}'
			+  '}';
	makeAjaxPostRequest(requestData, function (responseData) {location.reload();}, null);	// Defined in "/olive/scripts/master.js".
}

//Perform a splitVideo request
function splitVideo(videoName, splitTimeInSeconds) {
	var requestData = '{'
			+    '"command" : "splitVideo",'
			+    '"arguments" : {'
			+        '"video" : "' + videoName + '",'
			+        '"splitTimeInSeconds" : ' + splitTimeInSeconds + ''
			+    '}'
			+  '}';
	makeAjaxPostRequest(requestData, function (responseData) {location.reload(); }, null);	// Defined in "/olive/scripts/master.js".
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
	
	function checkCondition(o, condition, n) {
		if (!condition) {
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
								/^([0-9a-zA-Z])+$/i,
								'Video name may consist of a-z, 0-9; and must begin with a letter.');
				bValid = bValid
						&& checkCondition(splitTimeInSeconds,
								!isNaN(splitTimeInSeconds.val()),
								'Split time (in seconds) must be a number.')
						&& checkCondition(splitTimeInSeconds,
								splitTimeInSeconds.val() > 0,
								'Split time (in seconds) must be greater than 0.')
						&& checkCondition(splitTimeInSeconds,
								splitTimeInSeconds.val() < 14400,
								'Split time (in seconds) must be less than 14400.');
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

function attachAddToTimelineHandlers() {
	$('.add-to-timeline').click(function () {
		var clone = $(this).parent().parent().clone();
		var child = clone.find(".link");
		child.html("Remove from timeline");
		$("#timeline").append(clone);
		videoTimeline = this;
	});
	
	$('#confirm-add-to-timeline-dialog').dialog({
		autoOpen: false,
		resizable: false,
		height: 215,
		modal: true,
		buttons: {
			'OK': function () {
				$("#timeline").append($(videoTimeline).parent().parent().clone());
				$(this).dialog('close');
			}
		}
	});
}

function getVideoInformation() {
	var requestData = '{'
		+    '"command" : "getVideoInformation"'
		+  '}';
	makeAjaxPostRequest(requestData, function (responseData) {
		var poolPositions = [];
		var timelinePositions = [];
		for (var i = 0; i < responseData.length; ++i) {
			var element = $('#' + responseData[i].name).get(0);	// Strip off jQuery wrapper.
			$(element).data('url', responseData[i].url);
			$(element).data('icon', responseData[i].icon);
			
			if (responseData[i].poolPosition != -1) {
				$(element).data('poolPosition', responseData[i].poolPosition);
				poolPositions[responseData[i].poolPosition] = element;	// Sort
			}
			if (responseData[i].timelinePosition != -1) {
				$(element).data('timelinePosition', responseData[i].timelinePosition);
				timelinePositions[responseData[i].timelinePosition] = element;	// Sort
			}
			
			$(element).data('isSelected', responseData[i].isSelected);
			makeSelectionVisible(element);
		}
		// Append in the sorted order
		for (var poolIndex = 0; poolIndex < poolPositions.length; ++poolIndex) {
			$('#videos').append(poolPositions[poolIndex]);
		}
		for (var timelineIndex = 0; timelineIndex < timelinePositions.length; ++timelineIndex) {
			$('#timeline').append(timelinePositions[timelineIndex]);
		}
	}, null);	// Defined in "/olive/scripts/master.js".
}

function openNewVideoForm() {
	window.open("new-video-form.jsp", "videoUploadForm",
			"menubar=no,width=320,height=200,toolbar=no");
}
