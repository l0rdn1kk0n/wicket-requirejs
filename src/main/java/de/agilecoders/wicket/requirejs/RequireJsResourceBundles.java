package de.agilecoders.wicket.requirejs;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.ResourceBundles;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
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

		this.bundles = new HashMap<>();
	}

	@Override
	public JavaScriptReferenceHeaderItem addJavaScriptBundle(final Class<?> scope, final String name, final JavaScriptResourceReference... references) {
		final JavaScriptReferenceHeaderItem item = super.addJavaScriptBundle(scope, name, references);

		for (ResourceReference reference : references) {
			bundles.put(new ResourceReference.Key(reference.getScope().getName(), reference.getName(), null, null, null), item);
		}

		return item;
	}

	public ResourceReference findBundle(ResourceReference reference) {
		ResourceReference bundleReference = null;

		for (Map.Entry<ResourceReference.Key, JavaScriptReferenceHeaderItem> bundle : bundles.entrySet()) {
			if (reference.getKey().equals(bundle.getKey())) {
				bundleReference = bundle.getValue().getReference();
				break;
			}
		}

		return bundleReference;
	}
}
