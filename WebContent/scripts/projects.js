/*
 * This is Olive's JavaScript file for projects.jsp only.
 * Dependencies: "/olive/scripts/master.js"
 */

var deleteProjectDialogContext;	// TODO Remove this global variable.

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	attachDeleteProjectHandlers();
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

// Perform a deleteProject request
function deleteProject() {
	var data = '{'
			+    '"command" : "deleteProject",'
			+    '"arguments" : {'
			+        '"project" : "' + $(this).attr('id') + '"'
			+      '}'
			+  '}';
	makeAjaxPostRequest(data, true);	// Defined in "/olive/scripts/master.js".
}

function openNewProjectForm() {
	window.open("new-project-form.jsp", "newProjectForm",
			"menubar=no,width=320,height=200,toolbar=no");
}