package de.agilecoders.wicket;

import de.agilecoders.wicket.requirejs.RequireJs;
import org.apache.wicket.core.util.resource.ClassPathResourceFinder;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;

/**
 * base test case class
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
     * @return creates a new web application
     */
    protected WebApplication newWebApplication() {
        return new MockApplication() {

            @Override
            protected void init() {
                if (WicketApplicationTest.this.initApplication(this)) {
                    RequireJs.install(this);

                    getResourceSettings().getResourceFinders().add(new ClassPathResourceFinder(""));
                }
            }
        };
    }

    protected boolean initApplication(final MockApplication application) {
        return application != null;
    }
}
