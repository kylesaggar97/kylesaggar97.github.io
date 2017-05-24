$(document).ready(function(){
    $('#email').on('click',function(){
    	console.log("HI Kyle")
    	window.location.href = "mailto:ksaggar@terpmail.umd.edu?"; 
    });
});

// When clicking on Full hide fail/success boxes
$('#name').focus(function() {
    $('#success').html('');
});
