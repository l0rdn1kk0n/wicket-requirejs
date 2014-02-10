package de.agilecoders.wicket.requirejs;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.request.Url;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * A special {@link org.apache.wicket.request.resource.ResourceReference} that is used
 * to load AMD modules requested with with RequireJs 'wicket' plugin.
 * E.g.:
 * <pre><code>
 * require( ['wicket!demo', "wicket!pageB", "wicket!pageC"], function(demo, pageB, pageC) { ... });
 * </code></pre>
 * will make requests to this reference for modules 'demo', 'pageB' and 'pageC'.
 * WicketRequireJSLoader will use the {@link IRequireJsSettings#getModulesRegistry() registry}
 * to find the {@link org.apache.wicket.request.resource.JavaScriptResourceReference}
 * for each module.
 * <p/>
 * The modules are registered by {@link AmdModuleHeaderItem}
 *
 * @see AmdModuleHeaderItem
 */
public class WicketRequireJSLoader extends ResourceReference {

    private final IResource resource;

    /**
     * Constructor.
     *
     * @param settings The settings for RequireJs integration
     */
    public WicketRequireJSLoader(IRequireJsSettings settings) {
        super("wicket-require-js-loader");

        this.resource = new WicketRequireJsLoaderResource(settings);
    }

    @Override
    public IResource getResource() {
        return resource;
    }

    private static class WicketRequireJsLoaderResource implements IResource {

        private final IRequireJsSettings settings;

        public WicketRequireJsLoaderResource(IRequireJsSettings settings) {
            this.settings = settings;
        }

        @Override
        public void respond(Attributes attributes) {
            PageParameters parameters = attributes.getParameters();
            int indexedCount = parameters.getIndexedCount();
            List<String> segments = new ArrayList<>(indexedCount);
            for (int i = 0; i < indexedCount; i++) {
                segments.add(parameters.get(i).toString());
            }

            if (segments.size() > 0) {

                Url url = new Url(segments, new ArrayList<Url.QueryParameter>());
                String moduleName = url.toString();

                AmdModulesRegistry modulesRegistry = settings.getModulesRegistry();
                ResourceReference reference = modulesRegistry.getReference(moduleName);

                if (reference != null) {
                    IResource resource = reference.getResource();
                    resource.respond(attributes);
                } else {
                    WebResponse response = (WebResponse) attributes.getResponse();
                    response.sendError(HttpServletResponse.SC_NOT_FOUND,
                                       String.format("RequireJs module with name '%s' cannot be found", moduleName));
                }
            } else {
                WebResponse response = (WebResponse) attributes.getResponse();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                   "Not enough parameters provided");
            }
        }
    }
}
