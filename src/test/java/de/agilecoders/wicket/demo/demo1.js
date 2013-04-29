define("demo", ["Wicket"], function(w) {
	return {
		demo: function() {
			"use strict";
			console.log("%cHello from AMD!", "background-color: green;color: white;")
			console.log("Wicket: ", w);
		}
	};
});