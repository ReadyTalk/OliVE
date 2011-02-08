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