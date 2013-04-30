package de.agilecoders.wicket.requirejs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.Application;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * A JavaScript resource reference that depends on RequireJs
 */
public class AmdJsReference extends JavaScriptResourceReference
{
	public AmdJsReference(Class<?> scope, String name, Locale locale, String style, String variation)
	{
		super(scope, name, locale, style, variation);
	}

	public AmdJsReference(Class<?> scope, String name)
	{
		super(scope, name);
	}

	public AmdJsReference(Key key)
	{
		super(key);
	}

	@Override
	public Iterable<? extends HeaderItem> getDependencies()
	{
		List<JavaScriptHeaderItem> dependencies = new ArrayList<>();

		ResourceReference requireJsReference;
		if (Application.exists()) {
			IRequireJsSettings settings = RequireJs.settings();
			requireJsReference = settings.getResourceReference();
		} else {
			requireJsReference = new WebjarsJavaScriptResourceReference("requirejs/current/require.js");
		}
		dependencies.add(JavaScriptHeaderItem.forReference(requireJsReference));
		return dependencies;
	}
}
