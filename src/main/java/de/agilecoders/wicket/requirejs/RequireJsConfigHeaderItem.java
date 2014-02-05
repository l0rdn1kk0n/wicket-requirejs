package de.agilecoders.wicket.requirejs;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.UrlRenderer;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;

/**
 * A header item that contributes the require.js configuration.
 * It is contributed as dependency of {@link de.agilecoders.wicket.requirejs.RequireJsHeaderItem}
 */
class RequireJsConfigHeaderItem extends JavaScriptContentHeaderItem {

    /**
     * A key used to store the paths for all AMD modules in a request cycle
     */
    private static final MetaDataKey<Map<String, String>> PATHS_KEY = new MetaDataKey<Map<String, String>>() {
    };

    private static final String KEY_SHIM = "shim";
    private static final String KEY_DEPS = "deps";
    private static final String KEY_EXPORTS = "exports";
    private static final String KEY_PATHS = "paths";
    private static final String KEY_WAIT_SECONDS = "waitSeconds";

    private static final String ID_WICKET = "Wicket";
    private static final String ID_WICKET_EVENT = "wicket-event";
    private static final String ID_JQUERY = "jquery";

    /**
     * Creates a new {@code JavaScriptContentHeaderItem}.
     */
    public RequireJsConfigHeaderItem() {
        super(null, "wicket-require.js-config", null);
    }

    @Override
    public CharSequence getJavaScript() {
        final StringBuilder content = new StringBuilder();
        Application application = Application.get();
        RequestCycle cycle = RequestCycle.get();
        
        try {
            JSONObject requireConfig = new JSONObject();

            UrlRenderer urlRenderer = cycle.getUrlRenderer();

            configureMountPath(requireConfig, application, urlRenderer);

            String baseUrl = urlRenderer.renderRelativeUrl(Url.parse("/"));
            requireConfig.put("baseUrl", baseUrl);

            configureWaitSeconds(requireConfig, application);

            configureMappings(requireConfig);

            configureShims(requireConfig);

            configurePaths(requireConfig, application, cycle);

            content.append("require.config(");
            if (application.usesDevelopmentConfig()) {
                content.append(requireConfig.toString(2));
            } else {
                content.append(requireConfig.toString());
            }
            content.append(')').append(';');
        } catch (JSONException e) {
            throw new WicketRuntimeException(e);
        }
        
        return content;
    }

    private void configureWaitSeconds(JSONObject requireConfig, Application application) throws JSONException {
        if (application.usesDevelopmentConfig()) {
            requireConfig.put(KEY_WAIT_SECONDS, 600); // 10 minutes
        }
    }

    private void configureMountPath(JSONObject requireConfig, Application application, UrlRenderer urlRenderer)
            throws JSONException {
        IRequireJsSettings requireJsSettings = RequireJs.settings(application);
        String mountPath = requireJsSettings.getMountPath();
        String relativeMountPathUrl = urlRenderer.renderContextRelativeUrl(mountPath);
        requireConfig.put("mountPath", relativeMountPathUrl);
    }

    public static Map<String, String> getMappings() {
        Application application = Application.get();
        Map<String, String> mappings = application.getMetaData(PATHS_KEY);
        if (mappings == null) {
            synchronized (RequireJsConfigHeaderItem.class) {
                mappings = application.getMetaData(PATHS_KEY);
                if (mappings == null) {
                    mappings = new HashMap<>();
                    application.setMetaData(PATHS_KEY, mappings);
                }
            }
        }
        return mappings;
    }

    private void configureMappings(JSONObject requireJsConfig) throws JSONException {
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
    private void configureShims(JSONObject requireJsConfig) throws JSONException {
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

    private void configurePaths(JSONObject requireJsConfig, Application application, RequestCycle cycle)
            throws JSONException {
        JSONObject paths = new JSONObject();

        IJavaScriptLibrarySettings javaScriptLibrarySettings = application.getJavaScriptLibrarySettings();

        paths.put(ID_JQUERY, cycle.urlFor(javaScriptLibrarySettings.getJQueryReference(), null));
        paths.put(ID_WICKET_EVENT, cycle.urlFor(javaScriptLibrarySettings.getWicketEventReference(), null));
        paths.put(ID_WICKET, cycle.urlFor(javaScriptLibrarySettings.getWicketAjaxReference(), null));

        requireJsConfig.put(KEY_PATHS, paths);
    }
}
