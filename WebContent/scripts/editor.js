/*
 * This is Olive's JavaScript file for editor.jsp only.
 * Dependencies: "/olive/scripts/master.js"
 */

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	populateVideos();
});

function attachHandlers() {
	attachUploadNewVideoHandlers();
	attachDeleteVideoHandlers();
	attachSplitVideoHandlers();
	attachVideoClickHandlers();
	attachRenameVideoHandlers();
	//attachPublishButtonHandlers();
	enableDragAndDrop();
}

function createNewVideoContainer(videoName, videoNum, videoIcon) {
	var videoContainer = '<div id="video-container-'
		+ videoNum
		+ '" class="video-container"><img id="video-image-'
		+ videoNum
		+ '" class="video-image" src="'
		+ videoIcon
		+ '" alt="video-image-'
		+ videoNum
		+ '" /><div class="video-name">'
		+ videoName
		+ '</div><div class="video-controls"><small><a id="split-video-link-'
		+ videoNum
		+ '" class="split-video-link link hidden">Split</a>'
		+ '<span class="video-controls-divider hidden"> | </span>'
		+ '<a id="delete-video-link-'
		+ videoNum
		+ '" class="delete-video-link warning">Delete</a></small></div></div>';
	
	$('#videos').append(videoContainer);
	
	// Return the object that was just appended (with the jQuery wrapper stripped off).
	return $('#video-container-' + videoNum).get(0);
}

function createVideoSpinner() {
	
}

function populateVideos() {
	$('.video-container').hide();
	
	var requestData = '{'
		+    '"command" : "getVideoInformation"'
		+  '}';
	makeAjaxPostRequest(requestData, function (responseData) {
		var poolPositions = [];
		var timelinePositions = [];
		for (var i = 0; i < responseData.length; ++i) {
			var element = createNewVideoContainer(responseData[i].name, i, responseData[i].icon);
			$(element).data('name', responseData[i].name);
			$(element).data('url', responseData[i].url);
			$(element).data('icon', responseData[i].icon);
			
			// Modified from: http://stackoverflow.com/questions/600700/jquery-javascript-reordering-rows/617349#617349
			if (responseData[i].poolPosition != -1) {
				$(element).data('poolPosition', responseData[i].poolPosition);
				poolPositions[(responseData.length - 1) - responseData[i].poolPosition] = element;	// Sort in reverse order to work with prepending.
			}
			if (responseData[i].timelinePosition != -1) {
				$(element).data('timelinePosition', responseData[i].timelinePosition);
				timelinePositions[(responseData.length - 1) - responseData[i].timelinePosition] = element;	// Sort in reverse order to work with prepending.
			}
			
			$(element).data('isSelected', responseData[i].isSelected);
			makeSelectionVisible(element);
		}
		// Append in the sorted order
		for (var poolIndex = 0; poolIndex < poolPositions.length; ++poolIndex) {
			$('#videos').prepend(poolPositions[poolIndex]);	// Prepend to keep unsorted elements (poolPosition == -1) at the end.
		}
		for (var timelineIndex = 0; timelineIndex < timelinePositions.length; ++timelineIndex) {
			$('#timeline').prepend(timelinePositions[timelineIndex]);	// Prepend to keep unsorted elements (timelinePosition == -1) at the end.
		}
		
		$('.video-container').show();
		
		enableOrDisablePublishButton();
		
		attachHandlers();	// This must go inside the ajax callback or it will be called too early.
	}, null);	// Defined in "/olive/scripts/master.js".
}

function attachUploadNewVideoHandlers() {
	var newVideoName = $('#new-video-name'),
		allFields = $([]).add(newVideoName);
	
	$('#new-video-dialog-form').dialog({
		autoOpen : false,
		height : 400,
		width : 400,
		modal : true,
		buttons : {
			'Upload New Video' : function () {
				var bValid = true;
				allFields.removeClass('ui-state-error');

				bValid = bValid
						&& checkLength(newVideoName,
								'new-video-name', 1, 32);
				bValid = bValid
						&& checkRegexp(newVideoName,
								/^([0-9a-zA-Z])+$/,
								'Video Name may consist of a-z, A-Z, 0-9.');
				if (bValid) {
					createVideoSpinner();
					$('#new-video-form').submit();
					$(this).dialog('close');
				}
			},
			Cancel : function () {
				$(this).dialog('close');
			}
		},
		close : function () {
			allFields.val('').change().removeClass('ui-state-error');
		}
	});

	$('#upload-new-video-button').click(function() {
		$('#new-video-dialog-form').dialog('open');
	});
}

function attachDeleteVideoHandlers() {
	var videoToDelete;
	
	$('.delete-link').click(function () {
		doNotSelectThisTime();
		$('#confirm-delete-video-dialog').dialog('open');
		videoToDelete = $(this).parent().parent().parent();
	});
	
	$('#confirm-delete-video-dialog').dialog({
		autoOpen: false,
		resizable: false,
		height: 275,
		modal: true,
		buttons: {
			'Delete': function () {
				deleteVideo($(videoToDelete).data('name'));
				$(this).dialog('close');
			},
			Cancel: function () {
				$(this).dialog('close');
			}
		}
	});
}

//Perform a deleteVideo request
function deleteVideo(videoName) {
	var requestData = '{'
			+    '"command" : "deleteVideo",'
			+    '"arguments" : {'
			+        '"video" : "' + videoName + '"'
			+    '}'
			+  '}';
	makeAjaxPostRequest(requestData, refresh, null);	// Defined in "/olive/scripts/master.js".
}

function attachSplitVideoHandlers() {
	$('.split-link').click(function () {
		var video = $('video').get(0);
		if (video.currentTime === 0 || video.ended) {
			$('#invalid-split-dialog').dialog('open');
		} else {
			var videoToSplit = $(this).parent().parent().parent();
			var maximumZencoderDecimalPlaces = 2;
			splitVideo($(videoToSplit).data('name'),
					video.currentTime.toFixed(maximumZencoderDecimalPlaces));
		}
		doNotSelectThisTime();
	});
	
	$('#invalid-split-dialog').dialog( {
		autoOpen : false,
		buttons: {
			OK: function() {
				$(this).dialog('close');
			}
		}
	});
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
	makeAjaxPostRequest(requestData, refresh, null);	// Defined in "/olive/scripts/master.js".
}

function attachVideoClickHandlers() {
	$('.video-container').click(function (eventObject) {
		// Don't let the parent div's .click() event fire.
		// Modified from: http://stackoverflow.com/questions/1369035/how-do-i-prevent-a-parents-onclick-event-from-firing-when-a-child-anchor-is-clic
		eventObject.stopPropagation();
		
		if ($(this).data('isSelected')) {
			unselect(this);
		} else {
			unselectAll();
			select(this);
		}
	});
	
	$('#videos').click(function () {
		unselectAll();
	});
	
	$('#timeline').click(function () {
		unselectAll();
	});
}

function attachRenameVideoHandlers() {
	// Downloaded from: http://www.arashkarimzadeh.com/jquery/7-editable-jquery-plugin.html	
	$('.video-name').editable({
		type: 'text',
        submit: 'Save',
        cancel: 'Cancel',
        onEdit: function () {
			doNotSelectThisTime();	
			
			// Restrict input length
			// Another way: http://www.arashkarimzadeh.com/jquery/9-how-to-extend-jquery-editable.html
			var maxVideoNameLength = 32;
			$(this).children('input').attr('maxlength', maxVideoNameLength);
		},
        onSubmit: function (content) {
			renameVideo(content.previous, content.current);
		},
		onCancel: function (content) {
		}
	});
	
	// These don't work.
	$('.video-container input').live('click', function (){
		doNotSelectThisTime();
	});
	$('.video-container button').live('click', function (){
		doNotSelectThisTime();
	});
}

function doNotSelectThisTime() {
	event.stopPropagation();	// Prevent selecting from happening.
}

function attachPublishButtonHandlers(){
	$('#export-button').click(function(){
		$(this).text("Please wait...");
		combineVideos();
	});
}

// Perform a combineVideos request
function combineVideos(){
	var requestData = '{'
		+    '"command" : "combineVideos",'
		+    '"arguments" : {'
		+    '}'
		+  '}';
	makeAjaxPostRequest(requestData, null, null);
}

//Perform a renameVideo request
function renameVideo(oldVideoName, newVideoName) {
	var requestData = '{'
		+    '"command" : "renameVideo",'
		+    '"arguments" : {'
		+        '"oldVideoName" : "' + oldVideoName + '",'
		+        '"newVideoName" : "' + newVideoName + '"'
		+    '}'
		+  '}';
	makeAjaxPostRequest(requestData, refresh, null);	// Defined in "/olive/scripts/master.js". 
}

function makeSelectionVisible(element) {
	if ($(element).data('isSelected')) {
		$(element).css( {
			'background-color': '#edf4e6'	// A lighter version of the Olive color
		});
		$(element).find('.split-link').removeClass('hidden');
		$(element).find('.video-controls-divider').removeClass('hidden');
		updatePlayerWithNewElement(element);
	} else {
		$(element).css( {
			'background-color': ''
		});
		$(element).find('.split-link').addClass('hidden');
		$(element).find('.video-controls-divider').addClass('hidden');
		updatePlayerWithNoElements();
	}
}

function select(element) {
	$(element).data('isSelected', true);
	makeSelectionVisible(element);
	addToSelected($(element).data('name'));
}

function unselect(element) {
	$(element).data('isSelected', false);
	makeSelectionVisible(element);
	removeFromSelected($(element).data('name'));
}

function unselectAll() {
	$('.video-container').each(function () {
		unselect(this);	// 'this' is a different 'this' than outside .each()
	});
}

//Perform an addToSelected request
function addToSelected(videoName) {
	var requestData = '{'
		+    '"command" : "addToSelected",'
		+    '"arguments" : {'
		+        '"video" : "' + videoName + '"'
		+    '}'
		+  '}';
	makeAjaxPostRequest(requestData, null, null);	// Defined in "/olive/scripts/master.js".
}

// Perform a removeFromSelected request
function removeFromSelected(videoName) {
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
	$('#player').attr('poster', $(element).data('icon'));
	$('#player').append(
			'<source src="' + $(element).data('url')
			+ '" type="' + 'video/ogg; codecs=theora,vorbis'	// TODO Get this from the database.
			+ '" />');
}

function updatePlayerWithNoElements() {
	$('#player > source').remove();
	$('#player').removeAttr('poster');
}

function enableDragAndDrop() {
	$('#videos').sortable( {
		appendTo: 'body',
		connectWith: '#timeline',
		helper: 'clone',
		items: '.video-container',
		revert: true,
		scroll: false,
		tolerance: 'pointer',
		update: function (event, ui) {
			updateVideosPosition();
		}
	});
	
	$('#timeline').sortable( {
		appendTo: 'body',
		connectWith: '#videos',
		helper: 'clone',
		items: '.video-container',
		revert: true,
		scroll: false,
		tolerance: 'pointer',
		update: function (event, ui) {
			enableOrDisablePublishButton();
			updateTimelinePosition();
		}
	});
}

// Perform an update<command>Position request
function updatePosition(command, collectionItems) {
	var requestData = '{'
		+    '"command" : "' + command + '",'
		+    '"arguments" : {'
		+      '"videos" : [';
		
	if ($(collectionItems).length > 0) {
		$(collectionItems).each(function(index) {
			requestData += '{'
			+          '"video" : "' + $(this).data('name') + '",'
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
	updatePosition('updateVideosPosition', '#videos > .video-container');
}

// Perform an updateTimelinePosition request
function updateTimelinePosition() {
	updatePosition('updateTimelinePosition', '#timeline > .video-container');
}

function enableOrDisablePublishButton() {
	if ($('#timeline').sortable('toArray').length > 0){
		$('#export-button').removeAttr('disabled');
	} else {
		$('#export-button').attr('disabled', 'disabled');
	}
}