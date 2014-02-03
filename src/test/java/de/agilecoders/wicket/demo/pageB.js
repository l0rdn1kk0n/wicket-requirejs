define("pageB", ["wicket!jquery", "wicket!Wicket"], function($, w) {
	return {
		b: function() {
			"use strict";

			$(".text").html("<strong>Welcome to Page B</strong>");
			console.log("Wicket: ", w);
		}
	};
});
