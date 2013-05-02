package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Default implementation of {@link IRequireJsSettings}.
 *
 * @author miha
 */
public class RequireJsSettings implements IRequireJsSettings {

    private static final ResourceReference resourceReference = new WebjarsJavaScriptResourceReference("requirejs/current/require.js");

    @Override
    public ResourceReference getResourceReference() {
        return resourceReference;
    }

}
