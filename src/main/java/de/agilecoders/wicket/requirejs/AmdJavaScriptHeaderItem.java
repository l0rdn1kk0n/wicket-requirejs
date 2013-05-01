package de.agilecoders.wicket.requirejs;

import org.apache.wicket.Application;
import org.apache.wicket.ResourceBundles;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import java.util.Collections;

/**
 * A {@link HeaderItem} for AMD references that are used by require.js.
 *
 * @author martin-g
 */
public class AmdJavaScriptHeaderItem extends FilteredHeaderItem {

    /**
     * Creates a {@link AmdJavaScriptHeaderItem} for the given reference.
     *
     * @param reference resource reference pointing to the javascript resource
     * @return A newly created {@link AmdJavaScriptHeaderItem} for the given reference.
     */
    public static AmdJavaScriptHeaderItem forReference(JavaScriptResourceReference reference) {
        return forReference(reference, reference.getName());
    }

    /**
     * Creates a {@link AmdJavaScriptHeaderItem} for the given reference.
     *
     * @param reference resource reference pointing to the javascript resource
     * @param name      id that will be used as AMD identifier
     * @return A newly created {@link AmdJavaScriptHeaderItem} for the given reference.
     */
    public static AmdJavaScriptHeaderItem forReference(JavaScriptResourceReference reference, String name) {
        return new AmdJavaScriptHeaderItem(reference, name);
    }

    /**
     * Creates a new {@code AmdJavaScriptHeaderItem}.
     *
     * @param reference resource reference pointing to the javascript resource
     * @param name      name that will be used as AMD identifier
     */
    public AmdJavaScriptHeaderItem(JavaScriptResourceReference reference, String name) {
        super(new InnerItem(reference, name), RequireJsConfig.filterName());
    }

    /**
     * A simple {@link HeaderItem} that holds an AMD {@link ResourceReference} and its name.
     */
    private static class InnerItem extends HeaderItem {
        /**
         * The name to use as an AMD identifier
         */
        private final String name;

        /**
         * The resource reference that contributes the AMD module
         */
        private final JavaScriptResourceReference reference;

        /**
         * Construct.
         *
         * @param reference the resource reference
         * @param name      the name that is used as AMD identifier
         */
        private InnerItem(JavaScriptResourceReference reference, String name) {
            this.name = name;
            this.reference = reference;
        }

        @Override
        public Iterable<?> getRenderTokens() {
            // TODO: why do we need this? It's never used...
            //String url = Strings.stripJSessionId(getUrl());

            return Collections.singletonList("amd-javascript-" + name);
        }

        @Override
        public void render(Response response) {
            StringBuilder sb = new StringBuilder();
            sb.append(getName()).append('=').append(getUrl()).append('\n');
            response.write(sb);
        }

        /**
         * @return the url to this {@link HeaderItem}
         */
        public String getUrl() {
            IRequestHandler handler = null;

            if (Application.exists()) {
                ResourceBundles resourceBundles = Application.get().getResourceBundles();
                HeaderItem bundleItem = resourceBundles.findBundle(JavaScriptHeaderItem.forReference(reference, name));

                if (bundleItem instanceof JavaScriptReferenceHeaderItem) {
                    JavaScriptReferenceHeaderItem jsBundleItem = (JavaScriptReferenceHeaderItem) bundleItem;
                    ResourceReference bundleReference = jsBundleItem.getReference();
                    handler = new ResourceReferenceRequestHandler(bundleReference, null);
                }
            }

            if (handler == null) {
                handler = new ResourceReferenceRequestHandler(reference, null);
            }

            return RequestCycle.get().urlFor(handler).toString();
        }

        /**
         * @return the AMD identifier
         */
        public String getName() {
            return name;
        }
    }
}
