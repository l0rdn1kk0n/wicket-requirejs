package de.agilecoders.wicket.requirejs;

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
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;

import java.util.HashMap;
import java.util.Map;

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
    public static Map<String, String> getPaths() {
        RequestCycle requestCycle = RequestCycle.get();
        Map<String, String> paths = requestCycle.getMetaData(PATHS_KEY);
        if (paths == null) {
            paths = new HashMap<>();
            requestCycle.setMetaData(PATHS_KEY, paths);
        }
        return paths;
    }

    @Override
    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {

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

            Application application = getApplication();
            IJavaScriptLibrarySettings javaScriptLibrarySettings = application.getJavaScriptLibrarySettings();
            paths.put(ID_JQUERY, urlFor(javaScriptLibrarySettings.getJQueryReference(), null));
            paths.put(ID_WICKET_EVENT, urlFor(javaScriptLibrarySettings.getWicketEventReference(), null));
            paths.put(ID_WICKET, urlFor(javaScriptLibrarySettings.getWicketAjaxReference(), null));

            for (Map.Entry<String, String> p : getPaths().entrySet()) {
                paths.put(p.getKey(), p.getValue());
            }

            content.append(JavaScriptUtils.SCRIPT_OPEN_TAG).append("var require = ");
            if (application.usesDevelopmentConfig()) {
                content.append(requireConfig.toString(2));
            } else {
                content.append(requireConfig.toString());
            }
            content.append(';').append(JavaScriptUtils.SCRIPT_CLOSE_TAG);
        } catch (JSONException e) {
            throw new WicketRuntimeException(e);
        }

        replaceComponentTagBody(markupStream, openTag, content);
    }

}
