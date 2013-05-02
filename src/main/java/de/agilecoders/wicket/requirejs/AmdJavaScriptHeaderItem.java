package de.agilecoders.wicket.requirejs;

import org.apache.wicket.Application;
import org.apache.wicket.ResourceBundles;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IReferenceHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.string.Strings;

import java.util.Collections;
import java.util.Map;

/**
 * A {@link HeaderItem} for AMD references that are used by require.js.
 *
 * @author martin-g
 */
public class AmdJavaScriptHeaderItem extends HeaderItem implements IReferenceHeaderItem {

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
     * The name to use as an AMD identifier
     */
    private final String name;

    /**
     * The resource reference that contributes the AMD module
     */
    private final JavaScriptResourceReference reference;

    /**
     * Creates a new {@code AmdJavaScriptHeaderItem}.
     *
     * @param reference resource reference pointing to the javascript resource
     * @param name      name that will be used as AMD identifier
     */
    public AmdJavaScriptHeaderItem(JavaScriptResourceReference reference, String name) {
        this.reference = Args.notNull(reference, "reference");
        this.name = Args.notNull(name, "name");
    }

    @Override
    public Iterable<?> getRenderTokens() {
        return Collections.singletonList("amd-javascript-" + getName());
    }

    @Override
    public void render(Response response) {
        Map<String,CharSequence> paths = RequireJsConfig.getPaths();

        boolean isInBundle = false;

        RequestCycle requestCycle = RequestCycle.get();

        ResourceBundles resourceBundles = Application.get().getResourceBundles();
        HeaderItem bundleItem = resourceBundles.findBundle(JavaScriptHeaderItem.forReference(getReference(), getName()));

        if (bundleItem instanceof JavaScriptReferenceHeaderItem) {
            JavaScriptReferenceHeaderItem jsBundleItem = (JavaScriptReferenceHeaderItem) bundleItem;
            ResourceReference bundleReference = jsBundleItem.getReference();
            String bundleName = getBundleName(bundleReference);
            paths.put(bundleName, getUrl(requestCycle, bundleReference));

            Map<String, String> maps = RequireJsConfig.getMaps();
            maps.put(getName(), bundleName);

            isInBundle = true;
        }

        if (!isInBundle) {
            paths.put(getName(), getUrl(requestCycle, getReference()));
        }
    }

    private String getUrl(RequestCycle cycle, ResourceReference ref) {
        IRequestHandler handler = new ResourceReferenceRequestHandler(ref, null);
        String moduleUrl = cycle.urlFor(handler).toString();
        CharSequence bundleReferenceName = moduleUrl.substring(0, moduleUrl.length() - 3);
        return bundleReferenceName.toString();
    }

    private String getBundleName(ResourceReference bundleReference) {
        CharSequence bundleReferenceName = Strings.replaceAll(bundleReference.getName(), ".", "-");
        return bundleReferenceName.toString();
    }

    /**
     * @return the AMD identifier
     */
    public String getName() {
        return name;
    }

    @Override
    public ResourceReference getReference() {
        return reference;
    }
}
