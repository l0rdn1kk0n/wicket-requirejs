define("demo", ["wicket!Wicket"], function(w) {
	return {
		unnamed: function() {
			"use strict";
			console.log("%cHello from AMD!", "background-color: green;color: white;")
			console.log("Wicket: ", w);
		}
	};
});
