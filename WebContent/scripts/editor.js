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
		connectToSortable : '#timeline',
		helper : 'clone',
		revert : 'invalid',
		snap : '#timeline'
	});

	/*$('#timeline-sortable').sortable( {
		revert : true
	});

	$('#timeline-sortable').disableSelection();*/

	$('#timeline').sortable( {
		revert : true
	});
	
    $('.video-icon-container').contextMenu('videoMenu', {
        menuStyle: {
          border: '1px solid #000'
        },
        itemStyle: {
          fontFamily : 'verdana',
          backgroundColor : '#fff',
          color: 'black',
          border: 'none',
          padding: '1px'
        },
        itemHoverStyle: {
          color: '#fff',
          backgroundColor: '#00c',
          border: 'none'
        },
        bindings: {
          'split': function(t) {
              alert('Split Video');
          }
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
function win1() {
    window.open("videoUpload.jsp","Window1","menubar=no,width=320,height=55,toolbar=no");
}