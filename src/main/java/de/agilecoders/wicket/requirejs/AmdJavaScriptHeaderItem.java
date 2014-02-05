package de.agilecoders.wicket.requirejs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IReferenceHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.lang.Args;

/**
 * A {@link HeaderItem} for AMD JavaScript resource references that are
 * loaded asynchronously by require.js.
 *
 * @author martin-g
 */
public class AmdJavaScriptHeaderItem extends JavaScriptHeaderItem implements IReferenceHeaderItem {

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
        super(null);
        this.reference = Args.notNull(reference, "reference");
        this.name = Args.notNull(name, "name");
    }

    @Override
    public Iterable<?> getRenderTokens() {
        return Collections.singletonList("amd-javascript-" + getName());
    }

    @Override
    public void render(Response response) {
        IRequireJsSettings settings = RequireJs.settings();
        AmdModulesRegistry modulesRegistry = settings.getModulesRegistry();
        modulesRegistry.register(getName(), getReference());
    }

    /**
     * @return the AMD identifier
     */
    public String getName() {
        return name;
    }

    @Override
    public JavaScriptResourceReference getReference() {
        return reference;
    }

    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
        List<HeaderItem> dependencies = new ArrayList<>();
        dependencies.add(new RequireJsHeaderItem());
        return dependencies;
    }
}
