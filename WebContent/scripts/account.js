/*
 * This is Olive's JavaScript file for account.js only.
 */

var deleteAccountDialogContext;	// TODO Remove this global variable.

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	attachDeleteAccountHandlers();
	injectAccountData();
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
	makeAsynchronousPostRequest(requestData, logout, null);	// Defined in "/olive/scripts/master.js".
}

function injectAccountData() {
	var requestData = '{'
		+    '"command" : "getAccountInformation"'
		+  '}';
	makeAsynchronousPostRequest(requestData, function (responseData) {
		$('#new-name').val(responseData.name).change();
		$('#new-email').val(responseData.email).change();
		$('#new-password').val(responseData.password).change();
		$('#confirm-new-password').val(responseData.password).change();
		$('#new-security-question').val(responseData.securityQuestion).change();
		$('#new-security-answer').val(responseData.securityAnswer).change();
		
	}, null);	// Defined in "/olive/scripts/master.js".
}