package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.WicketApplicationTest;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.junit.Ignore;
import org.junit.Test;

import java.util.regex.Pattern;

/**
 * Tests the {@link RequireJsConfig} component
 *
 * @author miha
 */
public class RequireJsConfigTest extends WicketApplicationTest {

    @Test
    public void correctMapIsRendered() throws Exception {
        tester().startComponentInPage(new RequireJsConfig("id"));

        tester().assertContains(Pattern.quote("require.config({\n"
                                              + "  \"baseUrl\": \"../requirejs/\",\n"
                                              + "  \"shim\": {\n"
                                              + "    \"wicket-event\": {\n"
                                              + "      \"exports\": \"wicket-event\",\n"
                                              + "      \"deps\": [\"jquery\"]\n"
                                              + "    },\n"
                                              + "    \"Wicket\": {\n"
                                              + "      \"exports\": \"Wicket\",\n"
                                              + "      \"deps\": [\"wicket-event\"]\n"
                                              + "    }\n"
                                              + "  },\n"
                                              + "  \"mappings\": {}\n"
                                              + "});"));
    }

    @Test
    public void additionalAmdResourceReferenceIsRendered() throws Exception {
        tester().startComponentInPage(new RequireJsConfig("id") {
            @Override
            public void renderHead(IHeaderResponse response) {
                super.renderHead(response);

                response.render(AmdJavaScriptHeaderItem.forReference(new JavaScriptResourceReference(RequireJsConfig.class, "test.js"), "test"));
            }
        });

        tester().assertContainsNot(Pattern.quote("\"test\": \"./resource/de.agilecoders.wicket.requirejs.RequireJsConfig/test.js\""));
    }
}
