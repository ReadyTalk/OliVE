/*
 * This is Olive's JavaScript file for projects.jsp only.
 */

// TODO Remove this global variable.
var deleteDialogContext;

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	attachDeleteHandlers();
});

function attachDeleteHandlers() {
	$('.delete-project').click(function () {
		$('#confirm-delete-project-dialog').dialog('open');
		deleteDialogContext = this;	// This is a global variable.
	});
	
	$('#confirm-delete-project-dialog').dialog({
		autoOpen: false,
		resizable: false,
		height: 250,
		modal: true,
		buttons: {
			'Delete': function () {
				deleteProject.call(deleteDialogContext);	// We don't want the context to be the dialog element, but rather the element that triggered it.
				$(this).dialog('close');
			},
			Cancel: function () {
				$(this).dialog('close');
			}
		}
	});
}

// Perform a deleteProject request (to test POST commands)
function deleteProject() {
	// Domain: http://stackoverflow.com/questions/2300771/jquery-domain-get-url
	var postUrl = location.protocol + '//' + location.host + '/olive/OliveServlet';
	var postData = '{'
				+    '"command" : "deleteProject",'
				+    '"arguments" : '
				+      '['
				+        '{'
				+          '"project" : "' + $(this).attr('id') + '"'
				+        '}'
				+      ']'
				+  '}';
	// Encoding: http://stackoverflow.com/questions/26620/how-to-set-encoding-in-getjson-jquery
	$.ajax({
		type: 'POST',
		url: postUrl,
		contentType: 'application/json; charset=utf-8',
		data: postData,
		success: function (data) {
			console.log(data);
			console.log('Project deleted successfully.');
			location.reload();
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			// console.log(XMLHttpRequest.responseText);
			console.log(XMLHttpRequest.responseText);
			console.log('Could not delete project.');
			location.reload();
		}
	});
}

function openNewProjectForm() {
	window.open("new-project-form.jsp", "newProjectForm",
			"menubar=no,width=320,height=200,toolbar=no");
}