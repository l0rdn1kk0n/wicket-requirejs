package de.agilecoders.wicket.requirejs;

import org.apache.wicket.markup.head.JavaScriptContentHeaderItem;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;

/**
 *
 */
class RequireJsWicketPluginHeaderItem extends JavaScriptContentHeaderItem {

    private static final TextTemplate PLUGIN = new PackageTextTemplate(
            RequireJsWicketPluginHeaderItem.class, "res/require.js-wicket-plugin.js");

    /**
     * Creates a new {@code JavaScriptContentHeaderItem}.
     */
    public RequireJsWicketPluginHeaderItem() {
        super(null, "require.js-wicket-plugin", null);
    }

    @Override
    public CharSequence getJavaScript() {
        return PLUGIN.asString();
    }
}
