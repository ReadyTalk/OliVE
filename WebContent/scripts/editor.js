/*
 * This is Olive's JavaScript file for editor.jsp only.
 * Dependencies: "/olive/scripts/master.js"
 */

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	attachDeleteVideoHandlers();
	attachVideoMenuHandlers();
	attachVideoClickHandlers();
	attachVideoRenameHandlers();
	enableDragAndDrop();
	//attachPublishButtonHandler();
	getVideoInformation();
});

function doNotSelectThisTime() {
	event.stopPropagation();	// Prevent selecting from happening.
}

function attachDeleteVideoHandlers() {
	var videoToDelete;
	
	$('.delete-video').click(function () {
		doNotSelectThisTime();
		$('#confirm-delete-video-dialog').dialog('open');
		videoToDelete = this;
	});
	
	$('#confirm-delete-video-dialog').dialog({
		autoOpen: false,
		resizable: false,
		height: 275,
		modal: true,
		buttons: {
			'Delete': function () {
				deleteVideo($(videoToDelete).attr('id'));	// We don't want the context to be the dialog element, but rather the element that triggered it.
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
	$('.split-link').click(function() {
		doNotSelectThisTime();
		$('#video-name').val($(this).attr('id'))
						.change();	// Prefill in the value in the split dialog.
		$('#split-video-dialog-form').dialog('open');
	});
}

function attachPublishButtonHandler(){
	$('#export-button').click(function(){
		$(this).text("Please wait...")
		combineVideos();
	});
	
}

function combineVideos(){
	var requestData = '{'
		+    '"command" : "combineVideos",'
		+    '"arguments" : {'
		+    '}'
		+  '}';
	makeAjaxPostRequest(requestData, null, null);
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

function attachVideoRenameHandlers() {
	// Downloaded from: http://www.arashkarimzadeh.com/jquery/7-editable-jquery-plugin.html	
	$('.video-name').editable({
		type: 'text',
        submit: 'Save',
        cancel: 'Cancel',
        onEdit: function () {
			doNotSelectThisTime();
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
	$('#player').attr('poster', $(element).data('icon'));
	$('#player').append(
			'<source src="' + $(element).data('url')
			+ '" type="' + 'video/ogg; codecs=theora,vorbis'	// TODO Get this from the database.
			+ '" />');
}

function updatePlayerWithNoElements() {
	$('#player source').remove();
	$('#player').removeAttr('poster');
}

function enableDragAndDrop() {
	$('#videos').sortable( {
		appendTo: 'body',
		connectWith: '#timeline',
		helper: 'clone',
		items: '> div',	// Only immediate divs, not divs within other elements.
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
		items: 'div',
		revert: true,
		scroll: false,
		tolerance: 'pointer',
		update: function (event, ui) {
			enableOrDisableExportButton();
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
	updatePosition('updateVideosPosition', '#videos > div');
}

// Perform an updateTimelinePosition request
function updateTimelinePosition() {
	updatePosition('updateTimelinePosition', '#timeline > div');
}

// Perform a deleteVideo request
function deleteVideo(videoName) {
	var requestData = '{'
			+    '"command" : "deleteVideo",'
			+    '"arguments" : {'
			+        '"video" : "' + videoName + '"'
			+    '}'
			+  '}';
	makeAjaxPostRequest(requestData, refresh, null);	// Defined in "/olive/scripts/master.js".
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

function attachSplitHandlers() {
	var videoName = $('#video-name'), splitTimeInSeconds = $('#split-time-in-seconds'), allFields = $(
			[]).add(videoName).add(splitTimeInSeconds), tips = $('.validateTips');
	
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
		height : 325,
		width : 300,
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
				$(this).dialog('close');
			}
		},
		close : function() {
			allFields.val('').removeClass('ui-state-error');
		}
	});
}

function getVideoInformation() {
	$('.video-container').hide();
	
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
			
			// Modified from: http://stackoverflow.com/questions/600700/jquery-javascript-reordering-rows/617349#617349
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
		
		$('.video-container').show();
		
		enableOrDisableExportButton();
	}, null);	// Defined in "/olive/scripts/master.js".
}

function enableOrDisableExportButton() {
	if ($('#timeline').sortable('toArray').length > 0){
		$('#export-button').removeAttr('disabled');
	} else {
		$('#export-button').attr('disabled', 'disabled');
	}
}

function openNewVideoForm() {
	window.open("new-video-form.jsp", "videoUploadForm",
			"menubar=no,width=320,height=200,toolbar=no");
}
