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

    /**
     * @return amd module registry
     */
    AmdModulesRegistry getModulesRegistry();

    /**
     * @return The mount path that {@link de.agilecoders.wicket.requirejs.RequireJsMapper}
     *                  should use.
     */
    String getMountPath();

    /**
     * @return The number of seconds Require.js should wait for loading a module.
     */
    long getWaitSeconds();

}
