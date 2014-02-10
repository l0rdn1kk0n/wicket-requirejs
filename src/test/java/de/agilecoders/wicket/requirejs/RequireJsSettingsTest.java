package de.agilecoders.wicket.requirejs;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import de.agilecoders.wicket.WicketApplicationTest;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;

/**
 * Tests the {@link RequireJsSettings} class
 *
 * @author miha
 */
public class RequireJsSettingsTest extends WicketApplicationTest {

    @Test
    public void checkForValidResourceReference() {
        assertThat(new RequireJsSettings().getResourceReference(), is(instanceOf(WebjarsJavaScriptResourceReference.class)));
    }
}
