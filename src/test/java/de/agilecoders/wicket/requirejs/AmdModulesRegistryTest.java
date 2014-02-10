package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.WicketApplicationTest;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.junit.Test;

/**
 * Tests for AmdModulesRegistry
 */
public class AmdModulesRegistryTest extends WicketApplicationTest {

    @Test
    public void headerItemRegistersModuleInTheRegistry() {
        JavaScriptResourceReference jsReference = new JavaScriptResourceReference(AmdModulesRegistryTest.class, "test.js");
        String moduleName = "amd";
        AmdModuleHeaderItem amdHeaderItem = new AmdModuleHeaderItem(jsReference, moduleName);
        amdHeaderItem.render(tester().getRequestCycle().getResponse());

        IRequireJsSettings settings = RequireJs.settings(tester().getApplication());
        AmdModulesRegistry modulesRegistry = settings.getModulesRegistry();
        ResourceReference reference = modulesRegistry.getReference(moduleName);
        assertSame(reference, jsReference);
    }
}
