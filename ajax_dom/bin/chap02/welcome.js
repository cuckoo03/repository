/**
 *
 */
/*
window.onload = function(){
    var clickElement = document.getElementById("okClick");
    if (clickElement.addEventListener) {
        clickElement.addEventListener("click", Show.okClick, false);
    }
    else {
        clickElement.attachEvent("onClick", Show.okClick);
    }
};
*/
Event.observe(window, "load", function(){
    $("okClick").observe("click", Show.okClick);
});
var Show = {
	okClick:function(event) {
		alert('');
	}
};
