package de.agilecoders.wicket.demo;

import de.agilecoders.wicket.requirejs.AmdJavaScriptHeaderItem;
import de.agilecoders.wicket.requirejs.AmdJsReference;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 */
public class HomePage extends WebPage
{
	public HomePage(PageParameters parameters)
	{
		super(parameters);

		add(new BookmarkablePageLink<Void>("pageB", PageB.class));
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);

		response.render(AmdJavaScriptHeaderItem.forReference(new AmdJsReference(HomePage.class, "demo1.js"), "demo"));
	}
}
