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
     * @return the resource filter name that is used by {@link FilteringHeaderResponse}
     */
    String getFilterName();

}
