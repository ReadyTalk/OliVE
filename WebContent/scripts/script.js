/*
 * olive's custom JavaScript file.
 */

// Called once the DOM is ready but before the images, etc. load.
$(document).ready(function() {
	$('img').draggable();

	$('.timeline').droppable( {
		drop : function() {
			alert('dropped');
		}
	});

	$('#olive1').bind('click', function() {
		$('#olive1').clone().insertAfter('#olive4').slidedown();
	});

	$('#olive2').bind('click', function() {
		$('#olive2').clone().insertAfter('#olive4');
	});

	$('#olive3').bind('click', function() {
		$('#olive3').clone().insertAfter('#olive4');
	});

	$('#olive4').bind('click', function() {
		$('#olive4').clone().insertAfter('#olive4');
	});
});