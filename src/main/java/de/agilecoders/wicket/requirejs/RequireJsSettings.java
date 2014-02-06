package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import java.util.concurrent.TimeUnit;

/**
 * Default implementation of {@link IRequireJsSettings}.
 *
 * @author miha
 */
public class RequireJsSettings implements IRequireJsSettings {

    private static final class Holder {
        private static final ResourceReference DEFAULT_REFERENCE = new WebjarsJavaScriptResourceReference("requirejs/current/require.js");
    }

    private final AmdModulesRegistry registry;
    private final String mountPath;
    private final long waitSeconds;

    /**
     * Construct.
     */
    public RequireJsSettings() {
        this.registry = new AmdModulesRegistry();
        this.mountPath = "wicket/requirejs";
        this.waitSeconds = TimeUnit.MINUTES.toSeconds(10);
    }

    @Override
    public ResourceReference getResourceReference() {
        return Holder.DEFAULT_REFERENCE;
    }

    @Override
    public AmdModulesRegistry getModulesRegistry() {
        return registry;
    }

    @Override
    public String getMountPath() {
        return mountPath;
    }

    @Override
    public long getWaitSeconds() {
        return waitSeconds;
    }

}
