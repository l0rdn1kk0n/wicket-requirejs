package de.agilecoders.wicket.demo;

import de.agilecoders.wicket.requirejs.AmdJavaScriptHeaderItem;
import de.agilecoders.wicket.requirejs.RequireJs;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 *
 */
public class HomePage extends WebPage
{
	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);

		response.render(AmdJavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HomePage.class, "demo1.js"), "demo"));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(RequireJs.class, "require.js")));
	}
}
