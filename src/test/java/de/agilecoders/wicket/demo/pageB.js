define("pageB", ["jquery", "Wicket"], function($, w) {
	return {
		demo: function() {
			"use strict";

			$(".text").html("<strong>Welcome to Page B</strong>");
			console.log("Wicket: ", w);
		}
	};
});