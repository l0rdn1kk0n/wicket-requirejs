package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

/**
 * A JavaScript resource reference for require.js
 */
class RequireJsResourceReference extends WebjarsJavaScriptResourceReference {

    public RequireJsResourceReference() {
        super("requirejs/current/require.js");
    }
}
