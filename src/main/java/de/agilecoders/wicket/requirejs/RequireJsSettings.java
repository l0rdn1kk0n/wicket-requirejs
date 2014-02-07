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

    private final AmdModulesRegistry registry;
    private final String mountPath;
    private final long waitSeconds;
    private final ResourceReference resourceReference;

    /**
     * Construct.
     */
    public RequireJsSettings() {


        this.registry = new AmdModulesRegistry();
        this.mountPath = "wicket/requirejs";
        this.waitSeconds = TimeUnit.MINUTES.toSeconds(10);
        this.resourceReference = new WebjarsJavaScriptResourceReference("requirejs/current/require.js");
    }

    @Override
    public ResourceReference getResourceReference() {
        return resourceReference;
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
