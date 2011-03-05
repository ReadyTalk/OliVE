/*
 * This is Olive's JavaScript file for account.js only.
 */

var deleteAccountDialogContext;	// TODO Remove this global variable.

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	attachDeleteAccountHandlers();
});

function attachDeleteAccountHandlers() {
	$('#delete-account').click(function () {
		$('#confirm-delete-account-dialog').dialog('open');
		deleteAccountDialogContext = this;	// This is a global variable.
	});
	
	$('#confirm-delete-account-dialog').dialog({
		autoOpen: false,
		resizable: false,
		height: 275,
		modal: true,
		buttons: {
			'Delete': function () {
				deleteAccount.call(deleteAccountDialogContext);	// We don't want the context to be the dialog element, but rather the element that triggered it.
				$(this).dialog('close');
			},
			Cancel: function () {
				$(this).dialog('close');
			}
		}
	});
}

// Perform a deleteAccount request
function deleteAccount() {
	var requestData = '{'
			+    '"command" : "deleteAccount",'
			+    '"arguments" : {'
			+        '"account" : "' + $(this).attr('id') + '"'
			+      '}'
			+  '}';
	makeAjaxPostRequest(requestData, function (responseData) {logout(); }, null);	// Defined in "/olive/scripts/master.js".
}