package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * The {@link RequireJsPanel} adds the base require.js tag and all its configuration
 * to the current page. All configuration specific documentation can be found
 * here: {@link RequireJsConfig}.
 * <p/>
 * Usage:
 * <code lang="java">
 * add(new RequireJsPanel("require-js"));
 * </code>
 * <code lang="html">
 * <div wicket:id="require-js"></div>
 * </code>
 */
public class RequireJsPanel extends Panel {

    /**
     * Construct.
     *
     * @param id component id
     */
    public RequireJsPanel(String id) {
        super(id);

        add(newRequireJsConfig("config"));
        add(newRequireJsScript("require.js"));
    }

    /**
     * @return a new {@link RequireJsConfig} instance
     */
    protected RequireJsConfig newRequireJsConfig(final String id) {
        return new RequireJsConfig(id);
    }

    /**
     * creates a new script tag that refers to the require.js javascript file
     *
     * @param id the component id
     * @return new script tag component
     */
    protected Component newRequireJsScript(final String id) {
        return new WebComponent(id) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);

                tag.put("src", newRequireJsUrl());
            }
        };
    }

    /**
     * @return a new {@link IModel} that contains the url to the require.js resource
     */
    protected CharSequence newRequireJsUrl() {
        final ResourceReference requireJsReference;

        // TODO: do we need this check here?
        if (Application.exists()) {
            IRequireJsSettings settings = RequireJs.settings();
            requireJsReference = settings.getResourceReference();
        } else {
            requireJsReference = new WebjarsJavaScriptResourceReference("requirejs/current/require.js");
        }

        return urlFor(requireJsReference, null);
    }
}
