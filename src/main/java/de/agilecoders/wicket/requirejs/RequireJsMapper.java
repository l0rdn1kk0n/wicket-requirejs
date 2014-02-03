package de.agilecoders.wicket.requirejs;

import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.mount.MountMapper;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

import javax.servlet.http.HttpServletResponse;

/**
 * A special {@link org.apache.wicket.request.IRequestMapper} that is used
 * to resolve AMD modules requested with with RequireJs 'wicket' plugin.
 * E.g.:
 * <pre><code>
 * require( ['wicket!demo', "wicket!pageB", "wicket!pageC"], function(demo, pageB, pageC) {
 * </code></pre>
 * will make requests to this mapper for modules 'demo', 'pageB' and 'pageC'.
 * RequireJsMapper will use the {@link IRequireJsSettings#getModulesRegistry() registry}
 * to find the {@link org.apache.wicket.request.resource.JavaScriptResourceReference}
 * for each module.
 *
 * The modules are registered by {@link de.agilecoders.wicket.requirejs.AmdJavaScriptHeaderItem}
 *
 * @see de.agilecoders.wicket.requirejs.AmdJavaScriptHeaderItem
 */
public class RequireJsMapper extends MountMapper {

    public RequireJsMapper(IRequireJsSettings settings) {
        super(settings.getMountPath(), new RequireJsRequestHandler(settings));
    }

    private static class RequireJsRequestHandler implements IRequestHandler {

        private final IRequireJsSettings settings;
        private final int mountPathSegmentsNumber;

        private RequireJsRequestHandler(IRequireJsSettings settings) {
            this.settings = settings;
            this.mountPathSegmentsNumber = Url.parse(settings.getMountPath()).getSegments().size();
        }

        @Override
        public void respond(IRequestCycle requestCycle) {
            Url url = requestCycle.getRequest().getUrl();

            if (url.getSegments().size() > 1) {
                Url resourceUrl = new Url(url);
                resourceUrl.removeLeadingSegments(mountPathSegmentsNumber);


                String moduleName = resourceUrl.toString();

                AmdModulesRegistry modulesRegistry = settings.getModulesRegistry();
                ResourceReference reference = modulesRegistry.getReference(moduleName);

                if (reference != null) {
                    IResource resource = reference.getResource();
                    IResource.Attributes attributes = new IResource.Attributes(requestCycle.getRequest(), requestCycle.getResponse());
                    resource.respond(attributes);
                }
                else {
                    WebResponse response = (WebResponse) requestCycle.getResponse();
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, String.format("RequireJs module with name '%s' cannot be found", moduleName));
                }
            }
        }

        @Override
        public void detach(IRequestCycle requestCycle) {
        }
    }
}
