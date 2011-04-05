/*
 * This is Olive's master JavaScript file for every JSP page.
 */

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3

// The following should be exact copies of the constants in Security.java.
var MIN_USERNAME_LENGTH = 3;
var MAX_USERNAME_LENGTH = 16;
var SAFE_USERNAME_REGEX = /^[a-zA-z]([0-9a-zA-Z_])+$/;
var SAFE_USERNAME_MESSAGE = 'Username may consist of a-z, 0-9, underscores; and must begin with a letter.';

var MIN_EMAIL_LENGTH = 6;
var MAX_EMAIL_LENGTH = 64;
var SAFE_EMAIL_REGEX = /^((([a-zA-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-zA-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-zA-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-zA-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-zA-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-zA-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-zA-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-zA-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-zA-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-zA-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/;
var SAFE_EMAIL_MESSAGE = 'Email should be in the form: example@example.com';

var MIN_PASSWORD_LENGTH = 5;
var MAX_PASSWORD_LENGTH = 128;
var SAFE_PASSWORD_REGEX = /^[a-zA-Z0-9_]$|^[a-zA-Z0-9_]+[a-zA-Z0-9_ ]*[a-zA-Z0-9_]+$/;
var SAFE_PASSWORD_MESSAGE_SUFFIX = 'may consist of letters, numbers, underscores, and spaces.';
var SAFE_PASSWORD_MESSAGE = 'Password ' + SAFE_PASSWORD_MESSAGE_SUFFIX;

var MIN_PROJECT_NAME_LENGTH = 1;
var MAX_PROJECT_NAME_LENGTH = 32;
var SAFE_PROJECT_NAME_REGEX = SAFE_PASSWORD_REGEX;
var SAFE_PROJECT_NAME_MESSAGE = 'Project name ' + SAFE_PASSWORD_MESSAGE_SUFFIX;

var MIN_VIDEO_NAME_LENGTH = 1;
var MAX_VIDEO_NAME_LENGTH = 32;
var SAFE_VIDEO_NAME_REGEX = SAFE_PASSWORD_REGEX;
var SAFE_VIDEO_NAME_MESSAGE = 'Video name ' + SAFE_PASSWORD_MESSAGE_SUFFIX;

jQuery(function($) {
	includeHeader(); // TODO Fix for index.jsp (which is different)
	includeFooter();
});

function includeHeader() {
	$.get('/olive/header.jsp', function(data) {
		$('#header').append($(data));
		attachDialogToLink('help');
	});
}

function includeFooter() {
	$.get('/olive/footer.jsp', function(data) {
		$('#footer').append($(data));
		attachDialogToLink('about');
	});
}

function attachDialogToLink(linkName) {
	$('#' + linkName + '-dialog').dialog( {
		autoOpen : false,
		buttons: {
			OK: function() {
				$(this).dialog('close');
			}
		}
	});
	
	$('#' + linkName + '-dialog-opener').click(function() {
		$('#' + linkName + '-dialog').dialog('open');
		return false;
	});
}

function refresh() {
	window.location.reload();
}

function logout() {
	// See: http://stackoverflow.com/questions/503093/how-can-i-make-a-redirect-page-in-jquery
	window.location.replace('logout.jsp');
}

function makeAjaxPostRequest(requestData, onSuccess, onError) {
	// Domain: http://stackoverflow.com/questions/2300771/jquery-domain-get-url
	// E.g. 'http:' + '//' + 'olive.readytalk.com' + '/olive/OliveServlet'
	var postUrl = location.protocol + '//' + location.host + '/olive/OliveServlet';
	
	// Encoding: http://stackoverflow.com/questions/26620/how-to-set-encoding-in-getjson-jquery
	$.ajax({
		type: 'POST',
		url: postUrl,
		async: true,
		contentType: 'application/json; charset=utf-8',
		data: requestData,
		success: function (responseData) {
			if (onSuccess) {
				onSuccess(responseData);
			}
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			if (onError) {
				onError(XMLHttpRequest, textStatus, errorThrown);
			}
		}
	});
}

/*
 * The following functions are used for input validation
 */

function updateTips(t) {
	var tips = $('.validateTips');
	
	tips.text(t).addClass('ui-state-highlight');
	setTimeout(function() {
		tips.removeClass('ui-state-highlight', 1500);
	}, 500);
}

function checkLength(o, n, min, max) {
	if (o.val().length > max || o.val().length < min) {
		o.addClass('ui-state-error');
		updateTips('Length of ' + n + ' must be between ' + min + ' and '
				+ max + '.');
		return false;
	} else {
		return true;
	}
}

function checkRegexp(o, regexp, n) {
	if (!(regexp.test(o.val()))) {
		o.addClass('ui-state-error');
		updateTips(n);
		return false;
	} else {
		return true;
	}
}

function checkPasswordsEqual(o,p,n){
	if(!(o.val() == p.val())){
		o.addClass('ui-state-error');
		updateTips(n);
		return false;
	} else {
		return true;
	}
}