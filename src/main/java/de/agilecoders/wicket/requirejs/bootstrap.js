/**
 * base requirejs object
 *
 * @type {object}
 */
var require;

/**
 * loads the require.js script and adds a "webjars!" loader
 */
(function() {
    /**
     * the base path of webjars files
     *
     * @type {string}
     */
    var basePath = "${basePath}";

    /**
     * the main javascript file that will be loaded after require.js
     *
     * @type {string}
     */
    var mainJs   = "${mainJs}";

    /**
     * path to require.js file
     *
     * @type {string}
     */
    var requireJsPath   = "${requireJsPath}";

    /**
     * all existing wicket resource bundles as name:uri map
     *
     * @type {object}
     */
    var bundles   = '${bundles}';

    require = {
        /**
         * define webjars and bundles loader
         */
        callback: function() {
            define("webjars", function() {
                return {
                    load: function(name, req, onload, config) {
                        req([basePath + name], function(value) {
                            onload(value);
                        });
                    }
                };
            });

            define("bundles", function() {
                return {
                    load: function(name, req, onload, config) {
                        var path = bundles[name] || bundles[name + ".js"];

                        req([path], function(value) {
                            onload(value);
                        });
                    }
                };
            });
        }
    };

    /**
     * lazy load the require.js file
     *
     * @type {HTMLElement}
     */
    var script = document.createElement("script");
    script.setAttribute("data-main", mainJs);
    script.setAttribute("type", "application/javascript");
    script.setAttribute("src", requireJsPath);
    script.setAttribute("defer", "true");
    document.getElementsByTagName("head")[0].appendChild(script);
}());