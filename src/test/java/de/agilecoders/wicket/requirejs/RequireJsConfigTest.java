package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.WicketApplicationTest;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.junit.Test;

import java.util.regex.Pattern;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public class RequireJsConfigTest extends WicketApplicationTest {

    @Test
    public void correctMapIsRendered() throws Exception {
        tester().startComponentInPage(new RequireJsConfig("id"));

        tester().assertContains(Pattern.quote("var require = {\n"
                                + "  \"paths\": {\n"
                                + "    \"wicket-event\": \"./resource/org.apache.wicket.ajax.AbstractDefaultAjaxBehavior/res/js/wicket-event-jquery.js\",\n"
                                + "    \"Wicket\": \"./resource/org.apache.wicket.ajax.AbstractDefaultAjaxBehavior/res/js/wicket-ajax-jquery.js\",\n"
                                + "    \"jquery\": \"./resource/org.apache.wicket.resource.JQueryResourceReference/jquery/jquery.js\"\n"
                                + "  },\n"
                                + "  \"shim\": {\n"
                                + "    \"wicket-event\": {\n"
                                + "      \"exports\": \"wicket-event\",\n"
                                + "      \"deps\": [\"jquery\"]\n"
                                + "    },\n"
                                + "    \"Wicket\": {\n"
                                + "      \"exports\": \"Wicket\",\n"
                                + "      \"deps\": [\"wicket-event\"]\n"
                                + "    }\n"
                                + "  }\n"
                                + "};"));
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

        tester().assertContains(Pattern.quote("\"test\": \"./resource/de.agilecoders.wicket.requirejs.RequireJsConfig/test.js\""));
    }
}
