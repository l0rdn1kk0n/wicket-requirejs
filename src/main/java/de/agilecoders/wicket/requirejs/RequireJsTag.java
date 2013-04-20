package de.agilecoders.wicket.requirejs;

import org.apache.wicket.ResourceBundles;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import java.util.Map;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public class RequireJsTag extends Label {

    private final IModel<String> mainScriptModel;
    private final ScriptModel scriptModel;

    /**
     * Construct.
     *
     * @param id              component id
     * @param mainScriptModel resource url of main javascript file (could be something like
     *                        "bundles!main.js" if you're using ResourceBundles)
     */
    public RequireJsTag(final String id, final IModel<String> mainScriptModel) {
        super(id, new Model<String>());

        this.scriptModel = new ScriptModel();
        this.mainScriptModel = mainScriptModel;

        setEscapeModelStrings(false);
    }

    @Override
    protected void onComponentTag(final ComponentTag tag) {
        super.onComponentTag(tag);

        checkComponentTag(tag, "script");

        tag.put("type", "application/javascript");
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        setDefaultModelObject(scriptModel.getObject()
                                      .replace("'${bundles}'", createBundlesMap())
                                      .replace("${requireJsPath}", requireJsUrl())
                                      .replace("${mainJs}", mainScriptModel.getObject())
                                      .replace("${basePath}", requireJsUrl())
        );
    }

    /**
     * @return a json object as {@link String} that contains a bundle name as key and
     *         a url to that bundle as {@link CharSequence}.
     */
    private CharSequence createBundlesMap() {
        ResourceBundles resourceBundles = getApplication().getResourceBundles();

        if (resourceBundles instanceof RequireJsResourceBundles) {
            final RequireJsResourceBundles requireJsResourceBundles = (RequireJsResourceBundles) resourceBundles;

            Map<String, CharSequence> bundles = requireJsResourceBundles.javascriptBundles(getRequestCycle());
            try {
                return JSONObject.valueToString(bundles);
            } catch (JSONException e) {
                return "{}";
            }
        }

        return "{}";
    }

    /**
     * @return the url to the require.js file
     */
    protected CharSequence requireJsUrl() {
        return urlFor(RequireJs.settings().getResourceReference(), null);
    }

    @Override
    public void detachModels() {
        super.detachModels();

        mainScriptModel.detach();
        scriptModel.detach();
    }

    /**
     * wrapper model for bootstrap.js file that returns the content as string.
     */
    private static final class ScriptModel extends LoadableDetachableModel<String> {
        @Override
        protected String load() {
            return new BootstrapJavaScriptResourceReference().asText();
        }
    }
}
