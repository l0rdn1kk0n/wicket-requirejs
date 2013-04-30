package de.agilecoders.wicket.demo;

import de.agilecoders.wicket.requirejs.AmdHeaderResponse;
import de.agilecoders.wicket.requirejs.IRequireJsSettings;
import de.agilecoders.wicket.requirejs.RequireJs;
import de.agilecoders.wicket.requirejs.RequireJsSettings;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderResponseDecorator;
import org.apache.wicket.protocol.http.WebApplication;

/**
 *
 */
public class RequireJsApplication extends WebApplication
{
	@Override
	public Class<? extends Page> getHomePage()
	{
		return HomePage.class;
	}

	@Override
	protected void init()
	{
		super.init();

		mountPage("some/very/deep/path", PageB.class);

		IRequireJsSettings settings = new RequireJsSettings();
		RequireJs.install(this, settings);

		setHeaderResponseDecorator(new IHeaderResponseDecorator()
		{
			@Override
			public IHeaderResponse decorate(IHeaderResponse response)
			{
				return new AmdHeaderResponse(response);
			}
		});
	}

	@Override
	public RuntimeConfigurationType getConfigurationType()
	{
		return RuntimeConfigurationType.DEVELOPMENT;
	}
}
