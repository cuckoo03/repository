/**
 *
 */
window.onload = function() {
	var dataElmt/* of Input */ = document.getElementById("entryName");
	if (dataElmt.addEventListener) {
		dataElmt.addEventListener("keyup", Show.okClick, false);
	} else {
		dataElmt.attachEvent("onkeyup", Show.okClick);
	}
};
var Show = {
	okClick : function(event) {
		var entryData = document.getElementById("entryName").value;
		var showOne = document.getElementById("show1");
		if (showOne.hasChildNodes() && showOne.childNodes[0].nodeType == 3) {
			showOne.childNodes[0].nodeValue = entryData;
		} else {
			var textNode = document.createTextNode(entryData);
			alert(textNode);
			showOne.appendChild(textNode);
		}
	}
}; 