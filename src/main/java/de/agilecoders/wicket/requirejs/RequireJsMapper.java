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
 *
 */
public class RequireJsMapper extends MountMapper {

    public RequireJsMapper(String mountPath) {
        super(mountPath, new RequireJsRequestHandler());
    }

    private static class RequireJsRequestHandler implements IRequestHandler {

        @Override
        public void respond(IRequestCycle requestCycle) {
            Url url = requestCycle.getRequest().getUrl();

            if (url.getSegments().size() > 1) {
                Url resourceUrl = new Url(url);
                resourceUrl.getSegments().remove(0);

                String moduleName = resourceUrl.toString();
                if (moduleName.endsWith(".js")) {
                    moduleName = moduleName.substring(0, moduleName.length() - 3);
                }

                IRequireJsSettings settings = RequireJs.settings();
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
