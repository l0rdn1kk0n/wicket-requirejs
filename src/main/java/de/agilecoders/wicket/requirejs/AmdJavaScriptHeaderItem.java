package de.agilecoders.wicket.requirejs;

import java.util.Collections;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.string.Strings;

/**
 *
 */
public class AmdJavaScriptHeaderItem extends FilteredHeaderItem
{
	public static AmdJavaScriptHeaderItem forReference(JavaScriptResourceReference reference)
	{
		return forReference(reference, reference.getName());
	}

	public static AmdJavaScriptHeaderItem forReference(JavaScriptResourceReference reference, String name)
	{
		return new AmdJavaScriptHeaderItem(reference, name);
	}

	public AmdJavaScriptHeaderItem(JavaScriptResourceReference reference, String name)
	{
		super(new InnerItem(reference, name), RequireJsConfig.FILTER_NAME);
	}

	private static class InnerItem extends HeaderItem {
		/**
		 * The name to use as an AMD identifier
		 */
		private final String name;

		/**
		 * The resource reference that contributes the AMD module
		 */
		private final JavaScriptResourceReference reference;

		private InnerItem(JavaScriptResourceReference reference, String name)
		{
			this.name = name;
			this.reference = reference;
		}

		@Override
		public Iterable<?> getRenderTokens()
		{
			String url = Strings.stripJSessionId(getUrl());
			return Collections.singletonList("amd-javascript-" + url);
		}

		@Override
		public void render(Response response)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(getName()).append('=').append(getUrl()).append('\n');
			response.write(sb);
		}

		public String getUrl()
		{
			IRequestHandler handler = null;
			if (Application.exists()) {
				RequireJsResourceBundles resourceBundles = (RequireJsResourceBundles) Application.get().getResourceBundles();
				ResourceReference bundleReference = resourceBundles.findBundle(reference);
				if (bundleReference != null) {
					handler = new ResourceReferenceRequestHandler(bundleReference, null);
				}
			}
			if (handler == null) {
				handler = new ResourceReferenceRequestHandler(reference, null);
			}
			return RequestCycle.get().urlFor(handler).toString();
		}

		public String getName()
		{
			return name;
		}
	}
}
