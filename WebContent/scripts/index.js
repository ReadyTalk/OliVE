/*
 * This is Olive's JavaScript file for index.jsp only.
 */

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	attachRegistrationHandlers();
});

function attachRegistrationHandlers() {
	var name = $("#register-name"), email = $("#register-email"), password = $("#register-password"), cPassword = $("#confirm-register-password"), allFields = $(
			[]).add(name).add(email).add(password).add(cPassword), tips = $(".validateTips");
	
	$("#dialog-form").dialog({
		autoOpen : false,
		height : 525,
		width : 400,
		modal : true,
		buttons : {
			"Create an account" : function() {
				var bValid = true;
				allFields.removeClass("ui-state-error");

				bValid = bValid
						&& checkLength(name,
								"register-username", 3, 16);
				bValid = bValid
						&& checkLength(email, "register-email",
								6, 64);
				bValid = bValid
						&& checkLength(password,
								"register-password", 5, 128);
				bValid = bValid
						&& checkRegexp(name,
								/^[a-z]([0-9a-z_])+$/i,
								"Username may consist of a-z, 0-9, underscores; and must begin with a letter.");
				// From jquery.validate.js (by joern),
				// contributed by Scott Gonzalez:
				// http://projects.scottsplayground.com/email_address_validation/
				bValid = bValid
						&& checkRegexp(
								email,
								/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i,
								"Email should look like: example@example.com");
				bValid = bValid
						&& checkRegexp(password,
								/^([0-9a-zA-Z])+$/,
								"Password field only allow : a-z 0-9");
				bValid = bValid
						&& checkPasswordsEqual(password,
								cPassword,
								"Passwords are not equal");
				if (bValid) {
					$("#register-form").submit();
					$(this).dialog("close");
				}
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close : function() {
			allFields.val("").removeClass("ui-state-error");
		}
	});

	$("#create-user").click(function() {
		$("#dialog-form").dialog("open");
	});
}