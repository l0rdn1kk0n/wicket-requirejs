package de.agilecoders.wicket.requirejs;

import java.util.regex.Pattern;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.junit.Test;

import de.agilecoders.wicket.WicketApplicationTest;

/**
 * Tests the {@link RequireJsConfigHeaderItem} component
 *
 * @author miha
 */
public class RequireJsConfigHeaderItemTest extends WicketApplicationTest {

    @Test
    public void correctMapIsRendered() throws Exception {
        tester().startPage(new TestPage());

        tester().assertContains(Pattern.quote("require.config({\n"
                                              + "  \"baseUrl\": \"..//\",\n"
                                              + "  \"paths\": {\n"
                                              + "    \"wicket-event\": \"./resource/org.apache.wicket.ajax.AbstractDefaultAjaxBehavior/res/js/wicket-event-jquery.js\",\n"
                                              + "    \"Wicket\": \"./resource/org.apache.wicket.ajax.AbstractDefaultAjaxBehavior/res/js/wicket-ajax-jquery.js\",\n"
                                              + "    \"jquery\": \"./resource/org.apache.wicket.resource.JQueryResourceReference/jquery/jquery-1.10.2.js\"\n"
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
                                              + "  },\n"
                                              + "  \"mountPath\": \"../wicket/requirejs\"\n"
                                              + "});"));
    }

    @Test
    public void additionalAmdResourceReferenceIsRendered() throws Exception {
        tester().startComponentInPage(new Label("id", "ignored") {
            @Override
            public void renderHead(IHeaderResponse response) {
                super.renderHead(response);

                response.render(AmdJavaScriptHeaderItem.forReference(new JavaScriptResourceReference(RequireJsConfigHeaderItemTest.class, "test.js"), "test"));
            }
        });

        tester().assertContainsNot(Pattern.quote("\"test\": \"./resource/de.agilecoders.wicket.requirejs.RequireJsConfig/test.js\""));
    }

    private static class TestPage extends WebPage implements IMarkupResourceStreamProvider {
        @Override
        public void renderHead(IHeaderResponse response) {
            super.renderHead(response);

            response.render(new RequireJsHeaderItem());
        }

        @Override
        public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
            return new StringResourceStream("<html><head></head></html>");
        }
    }
}
