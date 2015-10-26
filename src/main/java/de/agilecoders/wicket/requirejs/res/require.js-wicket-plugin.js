define("wicket", function() {
    return {
        load: function(name, req, onload, config) {
            var mappings = config.mappings || {};

            var _name = mappings[name];
            if(_name === undefined) {
            	_name = config.mountPath + '/' + name;
            }
            
            req([_name], function(value) {
                onload(value);
            });
        }
    };
});
