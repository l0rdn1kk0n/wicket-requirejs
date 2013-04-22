package de.agilecoders.wicket;

import de.agilecoders.wicket.requirejs.BootstrapJavaScriptResourceReference;
import de.agilecoders.wicket.requirejs.RequireJsResourceBundles;
import de.agilecoders.wicket.requirejs.RequireJs;
import de.agilecoders.wicket.requirejs.RequireJsSettings;
import org.apache.wicket.ResourceBundles;
import org.apache.wicket.core.util.resource.ClassPathResourceFinder;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.ResourceReferenceRegistry;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public abstract class WicketApplicationTest {

    private WicketTester tester;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester(newWebApplication());
    }

    @After
    public void tearDown() throws Exception {
        tester.destroy();
    }

    protected final WicketTester tester() {
        return tester;
    }

    /**
     * TODO
     *
     * @return TODO
     */
    protected WebApplication newWebApplication() {
        return new MockApplication() {

            @Override
            protected ResourceBundles newResourceBundles(ResourceReferenceRegistry registry) {
                return new RequireJsResourceBundles(registry);
            }

            @Override
            protected void init() {
                if (WicketApplicationTest.this.initApplication(this)) {
                    RequireJs.install(this, new RequireJsSettings());

                    getResourceSettings().getResourceFinders().add(new ClassPathResourceFinder(""));
                    getResourceBundles().addJavaScriptBundle(BootstrapJavaScriptResourceReference.class, "main.js", new BootstrapJavaScriptResourceReference());
                }
            }
        };
    }

    protected boolean initApplication(final MockApplication application) {
        return application != null;
    }
}
