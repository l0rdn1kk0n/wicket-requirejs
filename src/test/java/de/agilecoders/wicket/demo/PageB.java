package de.agilecoders.wicket.demo;

import de.agilecoders.wicket.requirejs.AmdJavaScriptHeaderItem;
import de.agilecoders.wicket.requirejs.AmdJsReference;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;

/**
 *
 */
public class PageB extends WebPage
{
	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);

		response.render(AmdJavaScriptHeaderItem.forReference(new AmdJsReference(PageB.class, "pageB.js"), "pageB"));
	}
}
