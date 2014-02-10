package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.WicketApplicationTest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.junit.Test;

/**
 * Tests for WicketRequireJSLoader
 */
public class WicketRequireJSLoaderTest extends WicketApplicationTest {

    @Test
    public void ok() {

        IRequireJsSettings settings = RequireJs.settings(tester().getApplication());
        AmdModulesRegistry modulesRegistry = settings.getModulesRegistry();
        JavaScriptResourceReference jsReference = new JavaScriptResourceReference(WicketRequireJSLoaderTest.class, "test.js");
        String moduleName = "amd";
        modulesRegistry.register(moduleName, jsReference);

        WicketRequireJSLoader loader = new WicketRequireJSLoader(settings);
        PageParameters parameters = new PageParameters();
        parameters.set(0, moduleName);
        tester().startResourceReference(loader, parameters);

        assertEquals(200, tester().getLastResponse().getStatus());
        tester().assertContains("var a = 'a';");
    }

    @Test
    public void notFound() {

        IRequireJsSettings settings = RequireJs.settings(tester().getApplication());
        AmdModulesRegistry modulesRegistry = settings.getModulesRegistry();
        JavaScriptResourceReference jsReference = new JavaScriptResourceReference(WicketRequireJSLoaderTest.class, "test.js");
        String moduleName = "amd";
        modulesRegistry.register(moduleName, jsReference);

        WicketRequireJSLoader loader = new WicketRequireJSLoader(settings);
        PageParameters parameters = new PageParameters();
        parameters.set(0, moduleName + "_non-existing");
        tester().startResourceReference(loader, parameters);

        assertEquals(404, tester().getLastResponse().getStatus());
    }


    @Test
    public void badRequest() {

        IRequireJsSettings settings = RequireJs.settings(tester().getApplication());
        AmdModulesRegistry modulesRegistry = settings.getModulesRegistry();
        JavaScriptResourceReference jsReference = new JavaScriptResourceReference(WicketRequireJSLoaderTest.class, "test.js");
        String moduleName = "amd";
        modulesRegistry.register(moduleName, jsReference);

        WicketRequireJSLoader loader = new WicketRequireJSLoader(settings);
        tester().startResourceReference(loader);

        assertEquals(400, tester().getLastResponse().getStatus());
    }
}
