package de.agilecoders.wicket.requirejs;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.NoHeaderItem;

/**
 * The header item that contributes require.js, the config and the special
 * plugin that loads AMD modules contributed with
 * {@link AmdModuleHeaderItem}
 */
public class RequireJsHeaderItem extends NoHeaderItem {

    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
        List<HeaderItem> dependencies = new ArrayList<>();

        dependencies.add(JavaScriptHeaderItem.forReference(new RequireJsResourceReference()));
        dependencies.add(new RequireJsConfigHeaderItem());
        dependencies.add(new RequireJsWicketPluginHeaderItem());

        return dependencies;
    }
}
