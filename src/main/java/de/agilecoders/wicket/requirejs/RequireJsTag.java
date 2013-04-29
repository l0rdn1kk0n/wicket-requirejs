package de.agilecoders.wicket.requirejs;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.ResourceBundles;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.template.PackageTextTemplate;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public class RequireJsTag extends Label {

    private final IModel<String> mainScriptModel;

    /**
     * Construct.
     *
     * @param id              component id
     * @param mainScriptModel resource url of main javascript file (could be something like
     *                        "bundles!main.js" if you're using ResourceBundles)
     */
    public RequireJsTag(final String id, final IModel<String> mainScriptModel) {
        super(id, new Model<String>());

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

	    PackageTextTemplate requireJsConfig = new PackageTextTemplate(RequireJs.class, "bootstrap.js");
	    Map<String, Object> variables = new HashMap<>();
	    variables.put("bundles'", createBundlesMap());
	    variables.put("requireJsPath", requireJsUrl());
	    variables.put("mainJs", mainScriptModel.getObject());
	    variables.put("basePath", requireJsUrl());
        setDefaultModelObject(requireJsConfig.asString(variables));
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
    }

}
