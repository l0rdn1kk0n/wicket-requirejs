package de.agilecoders.wicket.requirejs;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.UrlRenderer;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;

/**
 * The {@link RequireJsConfig} renders the javascript configuration object that is used
 * by require.js to locate resources and to resolve their dependencies.
 *
 * @author martin-g
 * @author miha
 */
class RequireJsConfig extends Label implements IFeedback {

    /**
     * A key used to store the paths for all AMD modules in a request cycle
     */
    private static final MetaDataKey<Map<String, String>> PATHS_KEY = new MetaDataKey<Map<String, String>>() {
    };

    private static final String KEY_SHIM = "shim";
    private static final String KEY_DEPS = "deps";
    private static final String KEY_EXPORTS = "exports";
    private static final String KEY_PATHS = "paths";

    private static final String ID_WICKET = "Wicket";
    private static final String ID_WICKET_EVENT = "wicket-event";
    private static final String ID_JQUERY = "jquery";

    /**
     * Construct.
     *
     * @param id component id
     */
    public RequireJsConfig(final String id) {
        super(id);
    }

    /**
     * @return a map that stores all amdModuleName -> amdModuleUrl per request cycle
     */
    public static Map<String, String> getMappings() {
        Application application = Application.get();
        Map<String, String> mappings = application.getMetaData(PATHS_KEY);
        if (mappings == null) {
            mappings = new HashMap<>();
            application.setMetaData(PATHS_KEY, mappings);
        }
        return mappings;
    }

    @Override
    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
        final StringBuilder content = new StringBuilder();

        try {
            JSONObject requireConfig = new JSONObject();

            IRequireJsSettings requireJsSettings = RequireJs.settings(getApplication());
            UrlRenderer urlRenderer = getRequestCycle().getUrlRenderer();

            String mountPath = requireJsSettings.getMountPath();
            String relativeMountPathUrl = urlRenderer.renderRelativeUrl(Url.parse(mountPath));
            requireConfig.put("mountPath", relativeMountPathUrl);

            String baseUrl = urlRenderer.renderRelativeUrl(Url.parse("/"));
            requireConfig.put("baseUrl", baseUrl);

            configureMappings(requireConfig);

            configureShims(requireConfig);

            configurePaths(requireConfig);

            content.append(JavaScriptUtils.SCRIPT_OPEN_TAG).append("require.config(");
            if (getApplication().usesDevelopmentConfig()) {
                content.append(requireConfig.toString(2));
            } else {
                content.append(requireConfig.toString());
            }
            content.append(')').append(';').append(JavaScriptUtils.SCRIPT_CLOSE_TAG);
        } catch (JSONException e) {
            throw new WicketRuntimeException(e);
        }

        replaceComponentTagBody(markupStream, openTag, content);
    }

    protected void configureMappings(JSONObject requireJsConfig) throws JSONException {
        Map<String, String> mappings = getMappings();
        if (!mappings.isEmpty()) {
            JSONObject mappingsJson = new JSONObject();
            requireJsConfig.put("mappings", mappingsJson);

            for (Map.Entry<String, String> mapping : mappings.entrySet()) {
                mappingsJson.put(mapping.getKey(), mapping.getValue());
            }
        }
    }

    /**
     * Adds 'shim' to require.js config that loads 'Wicket' as a global variable
     * and configures a dependency to 'wicket-event'.
     *
     * This is needed because wicket-***.js files are not AMD modules.
     *
     * @throws JSONException
     */
    protected void configureShims(JSONObject requireJsConfig) throws JSONException {
        JSONObject shim = new JSONObject();

        JSONObject shimWicketEvent = new JSONObject();
        shim.put(ID_WICKET_EVENT, shimWicketEvent);
        shimWicketEvent.put(KEY_DEPS, new JSONArray("[" + ID_JQUERY + "]"));
        shimWicketEvent.put(KEY_EXPORTS, ID_WICKET_EVENT);

        JSONObject shimWicket = new JSONObject();
        shim.put(ID_WICKET, shimWicket);
        shimWicket.put(KEY_DEPS, new JSONArray("[" + ID_WICKET_EVENT + "]"));
        shimWicket.put(KEY_EXPORTS, ID_WICKET);

        requireJsConfig.put(KEY_SHIM, shim);
    }

    protected void configurePaths(JSONObject requireJsConfig) throws JSONException {
        JSONObject paths = new JSONObject();

        IJavaScriptLibrarySettings javaScriptLibrarySettings = getApplication().getJavaScriptLibrarySettings();

        paths.put(ID_JQUERY, urlFor(javaScriptLibrarySettings.getJQueryReference(), null));
        paths.put(ID_WICKET_EVENT, urlFor(javaScriptLibrarySettings.getWicketEventReference(), null));
        paths.put(ID_WICKET, urlFor(javaScriptLibrarySettings.getWicketAjaxReference(), null));

        requireJsConfig.put(KEY_PATHS, paths);
    }
}
