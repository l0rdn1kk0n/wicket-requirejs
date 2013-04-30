package de.agilecoders.wicket.demo;

import de.agilecoders.wicket.requirejs.AmdJavaScriptHeaderItem;
import de.agilecoders.wicket.requirejs.RequireJsPanel;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 *
 */
public class PageC extends WebPage
{
	public PageC() {

		add(new RequireJsPanel("requireJs"));
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);

		response.render(AmdJavaScriptHeaderItem.forReference(new JavaScriptResourceReference(PageB.class, "pageB.js"), "pageB"));
	}
}
