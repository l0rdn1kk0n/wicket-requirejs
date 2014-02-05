package de.agilecoders.wicket.demo;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import de.agilecoders.wicket.requirejs.AmdJavaScriptHeaderItem;

/**
 *
 */
public class PageC extends WebPage
{
	public PageC(PageParameters parameters) {
        super(parameters);
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);

		response.render(AmdJavaScriptHeaderItem.forReference(new JavaScriptResourceReference(PageC.class, "pageC.js"), "pageC"));
	}
}
