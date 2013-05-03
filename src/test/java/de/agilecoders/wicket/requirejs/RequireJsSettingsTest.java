package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the {@link RequireJsSettings} class
 *
 * @author miha
 */
public class RequireJsSettingsTest {

    @Test
    public void checkForValidResourceReference() {
        assertThat(new RequireJsSettings().getResourceReference(), is(instanceOf(WebjarsJavaScriptResourceReference.class)));
    }
}
