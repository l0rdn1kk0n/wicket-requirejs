define("wicket", function() {
    return {
        load: function(name, req, onload, config) {
            var mappings = config.mappings || {};
            var _name = mappings[name] || name;
            _name = config.mountPath + '/' + _name;
            req([_name], function(value) {
                onload(value);
            });
        }
    };
});
