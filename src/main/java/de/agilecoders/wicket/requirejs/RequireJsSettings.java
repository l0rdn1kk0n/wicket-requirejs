package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Default implementation of {@link IRequireJsSettings}.
 *
 * @author miha
 */
public class RequireJsSettings implements IRequireJsSettings {

    private static final ResourceReference DEFAULT_REFERENCE = new WebjarsJavaScriptResourceReference("requirejs/current/require.js");

    private ResourceReference requireJsReference = DEFAULT_REFERENCE;

    private AmdModulesRegistry registry = new AmdModulesRegistry();

    @Override
    public ResourceReference getResourceReference() {
        return requireJsReference;
    }

    @Override
    public IRequireJsSettings setResourceReference(ResourceReference reference) {
        this.requireJsReference = reference;
        return this;
    }

    @Override
    public AmdModulesRegistry getModulesRegistry() {
        return registry;
    }

}
