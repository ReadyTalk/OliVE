/*
 * This is Olive's JavaScript file for editor.jsp only.
 */

var video; // Global

// Called once the DOM is ready but before the images, etc. load.
// Failsafe jQuery code modified from: http://api.jquery.com/jQuery/#jQuery3
jQuery(function($) {
	video = document.getElementById('player-video');

	$('#videos-playpause').click(function() {
		if (video.paused) {
			video.play();
		} else {
			video.pause();
		}
	});

	$('#videos-volume-up').click(function() {
		if (video.volume <= 0.9) { // Any higher will cause an error.
			video.volume += 0.1;
		}
	});

	$('#videos-volume-down').click(function() {
		if (video.volume >= 0.1) { // Any lower will cause an error.
			video.volume -= 0.1;
		}
	});

	// Modified from: http://jqueryui.com/demos/draggable/
	$('.video-icon-container').draggable( {
		appendTo : 'body',
		scroll : false,
		connectToSortable : '#timeline-sortable',
		helper : 'clone',
		revert : 'invalid',
		snap : '#timeline'
	});

	$('#timeline-sortable').sortable( {
		revert : true
	});

	$('#timeline-sortable').disableSelection();

	$('#timeline').droppable( {
		drop : function() {
			console.log('Dropped');
		}
	});

	/*
	 * $('#olive1').bind('click', function() {
	 * $('#olive1').clone().insertAfter('#olive4').slidedown(); });
	 * 
	 * $('#olive2').bind('click', function() {
	 * $('#olive2').clone().insertAfter('#olive4'); });
	 * 
	 * $('#olive3').bind('click', function() {
	 * $('#olive3').clone().insertAfter('#olive4'); });
	 * 
	 * $('#olive4').bind('click', function() {
	 * $('#olive4').clone().insertAfter('#olive4'); });
	 */
});