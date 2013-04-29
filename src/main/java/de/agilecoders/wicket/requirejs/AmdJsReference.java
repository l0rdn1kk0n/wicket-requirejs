package de.agilecoders.wicket.requirejs;

import java.util.Locale;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 *
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
}
