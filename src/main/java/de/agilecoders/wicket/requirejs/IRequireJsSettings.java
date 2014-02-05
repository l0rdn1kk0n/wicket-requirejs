package de.agilecoders.wicket.requirejs;

import org.apache.wicket.request.resource.ResourceReference;

/**
 * Require.js configuration.
 *
 * @author miha
 */
public interface IRequireJsSettings {

    /**
     * @return the require.js {@link ResourceReference}
     */
    ResourceReference getResourceReference();

    IRequireJsSettings setResourceReference(ResourceReference reference);

    AmdModulesRegistry getModulesRegistry();

    /**
     * @param mountPath The mount path that {@link de.agilecoders.wicket.requirejs.RequireJsMapper}
     *                  should use.
     * @return {@code this} instance, for chaining
     */
    IRequireJsSettings setMountPath(String mountPath);

    /**
     * @return The mount path that {@link de.agilecoders.wicket.requirejs.RequireJsMapper}
     *                  should use.
     */
    String getMountPath();

    /**
     * @return The number of seconds Require.js should wait for loading a module.
     */
    int getWaitSeconds();

    /**
     * @param waitSeconds The number of seconds Require.js should wait for loading a module.
     * @return {@code this} instance, for chaining
     */
    IRequireJsSettings setWaitSeconds(int waitSeconds);
}
