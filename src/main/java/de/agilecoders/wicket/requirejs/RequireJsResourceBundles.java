package de.agilecoders.wicket.requirejs;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.ResourceBundles;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.ResourceReferenceRegistry;

/**
 * This class is an extension to {@link ResourceBundles} that adds some
 * functionality to access all registered bundles.
 *
 * @author miha
 */
public class RequireJsResourceBundles extends ResourceBundles {
    private final Map<ResourceReference.Key, JavaScriptReferenceHeaderItem> bundles;

    /**
     * Construct.
     *
     * @param registry the registry that keeps all referenced resources
     */
    public RequireJsResourceBundles(ResourceReferenceRegistry registry) {
        super(registry);

        this.bundles = new HashMap<ResourceReference.Key, JavaScriptReferenceHeaderItem>();
    }

    @Override
    public JavaScriptReferenceHeaderItem addJavaScriptBundle(final Class<?> scope, final String name, final JavaScriptResourceReference... references) {
        final JavaScriptReferenceHeaderItem item = super.addJavaScriptBundle(scope, name, references);

        // TODO: create correct key (class is missing)
        bundles.put(new ResourceReference.Key(scope.getName(), name, null, null, null), item);

        return item;
    }

    /**
     * @param requestCycle current {@link RequestCycle} for this request.
     * @return a map that contains mapping of bundle name to resource url
     */
    public Map<String, CharSequence> javascriptBundles(final RequestCycle requestCycle) {
        final Map<String, CharSequence> bundleUrls = new HashMap<String, CharSequence>(bundles.size());

        for (Map.Entry<ResourceReference.Key, JavaScriptReferenceHeaderItem> entry : bundles.entrySet()) {
            bundleUrls.put(entry.getKey().getName(), requestCycle.urlFor(entry.getValue().getReference(), null));
        }

        return bundleUrls;
    }
}
