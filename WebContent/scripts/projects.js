/*
 * This is Olive's JavaScript file for projects.jsp only.
 * Dependencies: "/olive/scripts/master.js"
 */

var deleteProjectDialogContext;	// TODO Remove this global variable.

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	attachDeleteProjectHandlers();
	enableDragAndDrop();
});

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
	$('#project-clips').sortable( {
		appendTo: 'body',
		helper: 'clone',
		items: 'div',
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
	console.log(requestData);
	makeAjaxPostRequest(requestData, null, null);	// Defined in "/olive/scripts/master.js".
}

//Perform an updateVideosPosition request
function updateProjectsPosition() {
	updatePosition('updateProjectsPosition', '#project-clips div');
}

// Perform a deleteProject request
function deleteProject() {
	var requestData = '{'
			+    '"command" : "deleteProject",'
			+    '"arguments" : {'
			+        '"project" : "' + $(this).attr('id') + '"'
			+      '}'
			+  '}';
	makeAjaxPostRequest(requestData, function (responseData) {location.reload(); }, null);	// Defined in "/olive/scripts/master.js".
}

function openNewProjectForm() {
	window.open("new-project-form.jsp", "newProjectForm",
			"menubar=no,width=320,height=200,toolbar=no");
}