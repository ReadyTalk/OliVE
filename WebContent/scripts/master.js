/*
 * This is Olive's master JavaScript file for every JSP page.
 */

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
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
		autoOpen : false
	});
	
	$('#' + linkName + '-dialog-opener').click(function() {
		$('#' + linkName + '-dialog').dialog('open');
		return false;
	});
}

function openHelpWindow() {
	window.open("help.jsp", "HelpWindow",
			"menubar=no,width=500,height=500,toolbar=no");
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