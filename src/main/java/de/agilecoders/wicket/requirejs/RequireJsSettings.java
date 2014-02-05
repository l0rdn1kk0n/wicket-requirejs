package de.agilecoders.wicket.requirejs;

import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

/**
 * Default implementation of {@link IRequireJsSettings}.
 *
 * @author miha
 */
public class RequireJsSettings implements IRequireJsSettings {

    private static final ResourceReference DEFAULT_REFERENCE = new WebjarsJavaScriptResourceReference("requirejs/current/require.js");

    private ResourceReference requireJsReference = DEFAULT_REFERENCE;

    private AmdModulesRegistry registry = new AmdModulesRegistry();

    private String mountPath = "wicket/requirejs";

    private int waitSeconds = 600; // 10 minutes

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

    @Override
    public IRequireJsSettings setMountPath(String mountPath) {
        this.mountPath = Args.notEmpty(mountPath, "mountPath");
        if ("/".equals(mountPath)) {
            throw new IllegalArgumentException("Cannot mount RequireJsMapper at the root!");
        }
        return this;
    }

    @Override
    public String getMountPath() {
        return mountPath;
    }

    @Override
    public int getWaitSeconds() {
        return waitSeconds;
    }

    @Override
    public IRequireJsSettings setWaitSeconds(int waitSeconds) {
        this.waitSeconds = waitSeconds;
        return this;
    }

}
