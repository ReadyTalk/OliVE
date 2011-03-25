/*
 * This is Olive's JavaScript file for index.jsp only.
 */

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	attachRegistrationHandlers();
});

function attachRegistrationHandlers() {
	var name = $('#register-name'), email = $('#register-email'), password = $('#register-password'), cPassword = $('#confirm-register-password'), allFields = $(
			[]).add(name).add(email).add(password).add(cPassword);
	
	$('#dialog-form').dialog({
		autoOpen : false,
		height : 550,
		width : 450,
		modal : true,
		buttons : {
			'Create an account' : function() {
				var bValid = true;
				allFields.removeClass('ui-state-error');

				bValid = bValid
						&& checkLength(name,
								'register-username',
								MIN_USERNAME_LENGTH,
								MAX_USERNAME_LENGTH);
				bValid = bValid
						&& checkLength(email,
								'register-email',
								MIN_EMAIL_LENGTH,
								MAX_EMAIL_LENGTH);
				bValid = bValid
						&& checkLength(password,
								'register-password',
								MIN_PASSWORD_LENGTH,
								MAX_PASSWORD_LENGTH);
				bValid = bValid
						&& checkRegexp(name,
								SAFE_USERNAME_REGEX,
								'Username may consist of a-z, 0-9, underscores; and must begin with a letter.');
				bValid = bValid
						&& checkRegexp(email,
								SAFE_EMAIL_REGEX,
								'Email should be in the form: example@example.com');
				bValid = bValid
						&& checkRegexp(password,
								SAFE_PASSWORD_REGEX,
								'Password may consist of letters, numbers, underscores, and spaces.');
				bValid = bValid
						&& checkPasswordsEqual(password,
								cPassword,
								'Passwords do not match.');
				if (bValid) {
					$('#register-form').submit();
					$(this).dialog('close');
				}
			},
			Cancel : function() {
				$(this).dialog('close');
			}
		},
		close : function() {
			allFields.val('').change().removeClass('ui-state-error');
		}
	});

	$('#create-user').click(function() {
		$('#dialog-form').dialog('open');
	});
}