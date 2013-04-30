package de.agilecoders.wicket.requirejs;

import java.util.Collections;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.string.Strings;

/**
 *
 */
public class AmdJavaScriptHeaderItem extends HeaderItem
{
	/**
	 * The name to use as an AMD identifier
	 */
	private final String name;

	/**
	 * The resource reference that contributes the AMD module
	 */
	private final JavaScriptResourceReference reference;

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
		this.reference = Args.notNull(reference, "reference");
		this.name = Args.notNull(name, "name");
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
		// do nothing.
		// require.js will load this lazily by using #getUrl()
	}

	public String getUrl()
	{
		IRequestHandler handler = new ResourceReferenceRequestHandler(reference, null);
		return RequestCycle.get().urlFor(handler).toString();
	}

	public String getName()
	{
		return name;
	}

	@Override
	public Iterable<? extends HeaderItem> getDependencies()
	{
		return reference.getDependencies();
	}
}
