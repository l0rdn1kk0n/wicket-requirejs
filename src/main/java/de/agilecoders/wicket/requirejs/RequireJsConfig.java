package de.agilecoders.wicket.requirejs;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;
import org.apache.wicket.util.string.Strings;

/**
 * The {@link RequireJsConfig} renders the javascript configuration object that is used
 * by require.js to locate resources and to resolve their dependencies.
 *
 * @author martin-g
 * @author miha
 */
class RequireJsConfig extends HeaderResponseContainer {
    private static final String KEY_SHIM = "shim";
    private static final String KEY_DEPS = "deps";
    private static final String KEY_EXPORTS = "exports";
    private static final String KEY_PATHS = "paths";

    private static final String ID_WICKET = "Wicket";
    private static final String ID_WICKET_EVENT = "wicket-event";
    private static final String ID_JQUERY = "jquery";

    /**
     * @return the require.js filter name that is used to filter all resource
     *         references that should be used
     */
    static String filterName() {
        return RequireJs.settings().getFilterName();
    }

    /**
     * Construct.
     *
     * @param id component id
     */
    public RequireJsConfig(final String id) {
        super(id, filterName());
    }

    @Override
    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
        final FilteringHeaderResponse response = FilteringHeaderResponse.get();

        if (!response.isClosed()) {
            throw new RuntimeException(
                    "there was an error processing the header response - "
                    + "you tried to render a bucket of response from FilteringHeaderResponse, "
                    + "but it had not yet run and been closed. This could occur when the header "
                    + "container that is standard in wicket renders, so perhaps you have done "
                    + "something to keep that from rendering?");
        }

        final String configPaths = response.getContent(filterName()).toString();
        final StringBuilder content = new StringBuilder();

        try {
            JSONObject requireConfig = new JSONObject();

            JSONObject shim = new JSONObject();
            requireConfig.put(KEY_SHIM, shim);

            JSONObject shimWicketEvent = new JSONObject();
            shim.put(ID_WICKET_EVENT, shimWicketEvent);
            shimWicketEvent.put(KEY_DEPS, new JSONArray("[" + ID_JQUERY + "]"));
            shimWicketEvent.put(KEY_EXPORTS, ID_WICKET_EVENT);

            JSONObject shimWicket = new JSONObject();
            shim.put(ID_WICKET, shimWicket);
            shimWicket.put(KEY_DEPS, new JSONArray("[" + ID_WICKET_EVENT + "]"));
            shimWicket.put(KEY_EXPORTS, ID_WICKET);

            JSONObject paths = new JSONObject();
            requireConfig.put(KEY_PATHS, paths);

            IJavaScriptLibrarySettings javaScriptLibrarySettings = getApplication().getJavaScriptLibrarySettings();
            paths.put(ID_JQUERY, urlFor(javaScriptLibrarySettings.getJQueryReference(), null));
            paths.put(ID_WICKET_EVENT, urlFor(javaScriptLibrarySettings.getWicketEventReference(), null));
            paths.put(ID_WICKET, urlFor(javaScriptLibrarySettings.getWicketAjaxReference(), null));

            String[] items = Strings.split(configPaths, '\n');
            for (String item : items) {
                if (!Strings.isEmpty(item)) {
                    String[] parts = Strings.split(item, '=');
                    paths.put(parts[0], parts[1]);
                }
            }

            content.append("<script>\nvar require = ");
            content.append(requireConfig.toString(2)).append(';');
            content.append("</script>\n");
        } catch (JSONException e) {
            throw new WicketRuntimeException(e);
        }

        replaceComponentTagBody(markupStream, openTag, content);
    }

}
