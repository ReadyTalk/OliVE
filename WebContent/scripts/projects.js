/*
 * This is Olive's JavaScript file for projects.jsp only.
 * Dependencies: "/olive/scripts/master.js"
 */

var deleteProjectDialogContext;	// TODO Remove this global variable.

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	attachCreateNewProjectHandlers();
	attachDeleteProjectHandlers();
	attachRenameProjectHandlers();
	enableDragAndDrop();
	getProjectInformation();
});

function attachCreateNewProjectHandlers() {
	var newProjectName = $("#new-project-name"),
		allFields = $([]).add(newProjectName);
	
	$("#new-project-dialog-form").dialog({
		autoOpen : false,
		height : 350,
		width : 400,
		modal : true,
		buttons : {
			"Create New Project" : function () {
				var bValid = true;
				allFields.removeClass("ui-state-error");

				bValid = bValid
						&& checkLength(newProjectName,
								"new-project-name", 1, 32);
				bValid = bValid
						&& checkRegexp(newProjectName,
								/^([0-9a-zA-Z])+$/,
								"Project Name may consist of a-z, A-Z, 0-9.");
				if (bValid) {
					$("#new-project-form").submit();
					$(this).dialog("close");
				}
			},
			Cancel : function () {
				$(this).dialog("close");
			}
		},
		close : function () {
			allFields.val("").change().removeClass("ui-state-error");
		}
	});

	$("#create-new-project").click(function() {
		$("#new-project-dialog-form").dialog("open");
	});
}

function attachRenameProjectHandlers() {
	// Downloaded from: http://www.arashkarimzadeh.com/jquery/7-editable-jquery-plugin.html
	$('.project-name').editable({
		type: 'text',
        submit: 'Save',
        cancel: 'Cancel',
        onEdit: function () {
			// Restrict input length
			// Another way: http://www.arashkarimzadeh.com/jquery/9-how-to-extend-jquery-editable.html
			var maxProjectNameLength = 32;
			$(this).children('input').attr('maxlength', maxProjectNameLength);
		},
        onSubmit: function (content) {
			renameProject(content.previous, content.current);
		},
		onCancel: function (content) {
		}
	});
}

//Perform a renameProject request
function renameProject(oldProjectName, newProjectName) {
	var requestData = '{'
		+    '"command" : "renameProject",'
		+    '"arguments" : {'
		+        '"oldProjectName" : "' + oldProjectName + '",'
		+        '"newProjectName" : "' + newProjectName + '"'
		+    '}'
		+  '}';
	makeAjaxPostRequest(requestData, refresh, null);	// Defined in "/olive/scripts/master.js". 
}

function attachDeleteProjectHandlers() {
	$('.delete-project').click(function () {
		$('#confirm-delete-project-dialog').dialog('open');
		deleteProjectDialogContext = this;	// This is a global variable.
	});
	
	$('#confirm-delete-project-dialog').dialog({
		autoOpen: false,
		resizable: false,
		height: 275,
		modal: true,
		buttons: {
			'Delete': function () {
				deleteProject.call(deleteProjectDialogContext);	// We don't want the context to be the dialog element, but rather the element that triggered it.
				$(this).dialog('close');
			},
			Cancel: function () {
				$(this).dialog('close');
			}
		}
	});
}

function enableDragAndDrop() {
	$('#projects').sortable( {
		appendTo: 'body',
		helper: 'clone',
		items: '.project-container',
		revert: true,
		scroll: false,
		tolerance: 'pointer',
		update: function(event, ui) {
			updateProjectsPosition();
		}
	});
}

//Perform an update<command>Position request
function updatePosition(command, collectionItems) {
	var requestData = '{'
		+    '"command" : "' + command + '",'
		+    '"arguments" : {'
		+      '"projects" : [';
		
	if ($(collectionItems).length > 0) {
		$(collectionItems).each(function(index) {
			requestData += '{'
			+          '"project" : "' + $(this).attr('id') + '",'
			+          '"position" : ' + index
			+        '},';	// This will result in an extra comma.
		});
		
		// Strip off the extra comma.
		requestData = requestData.substring(0, requestData.length - 1);
	}	
	
	requestData += ']}}';
	
	makeAjaxPostRequest(requestData, null, null);	// Defined in "/olive/scripts/master.js".
}

//Perform an updateProjectsPosition request
function updateProjectsPosition() {
	updatePosition('updateProjectsPosition', '#projects > div');
}

// Perform a deleteProject request
function deleteProject() {
	var requestData = '{'
			+    '"command" : "deleteProject",'
			+    '"arguments" : {'
			+        '"project" : "' + $(this).attr('id') + '"'
			+      '}'
			+  '}';
	makeAjaxPostRequest(requestData, refresh, null);	// Defined in "/olive/scripts/master.js".
}

function getProjectInformation() {
	$('.project-container').hide();
	
	var requestData = '{'
		+    '"command" : "getProjectInformation"'
		+  '}';
	makeAjaxPostRequest(requestData, function (responseData) {
		var poolPositions = [];
		for (var i = 0; i < responseData.length; ++i) {
			var element = $('#' + responseData[i].name).get(0);	// Strip off jQuery wrapper.
			$(element).data('icon', responseData[i].icon);
			
			// Modified from: http://stackoverflow.com/questions/600700/jquery-javascript-reordering-rows/617349#617349
			if (responseData[i].poolPosition != -1) {
				$(element).data('poolPosition', responseData[i].poolPosition);
				poolPositions[(responseData.length - 1) - responseData[i].poolPosition] = element;	// Sort in reverse order to work with prepending.
			}
		}
		// Append in the sorted order
		for (var poolIndex = 0; poolIndex < poolPositions.length; ++poolIndex) {
			$('#projects').prepend(poolPositions[poolIndex]);	// Prepend to keep unsorted elements (poolPosition == -1) at the end.
		}
		
		$('.project-container').show();
	}, null);	// Defined in "/olive/scripts/master.js".
}