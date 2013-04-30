package de.agilecoders.wicket.demo;

import java.lang.annotation.Annotation;
import java.util.Collections;

import de.agilecoders.wicket.requirejs.IRequireJsSettings;
import de.agilecoders.wicket.requirejs.RequireJs;
import de.agilecoders.wicket.requirejs.RequireJsSettings;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.filter.FilteringHeaderResponse;
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
		mountPage("c", PageC.class);

		Override oo = new Override() {

			@Override
			public Class<? extends Annotation> annotationType()
			{
				return Override.class;
			}
		};

		IRequireJsSettings settings = new RequireJsSettings();
		RequireJs.install(this, settings);

//		setHeaderResponseDecorator(new IHeaderResponseDecorator()
//		{
//			@Override
//			public IHeaderResponse decorate(IHeaderResponse response)
//			{
//				return new AmdHeaderResponse(response);
//			}
//		});

		setHeaderResponseDecorator(new IHeaderResponseDecorator()
		{
			@Override
			public IHeaderResponse decorate(IHeaderResponse response)
			{
				return new FilteringHeaderResponse(response, "headerFilter", Collections.<FilteringHeaderResponse.IHeaderResponseFilter>emptyList());
			}
		});
	}

	@Override
	public RuntimeConfigurationType getConfigurationType()
	{
		return RuntimeConfigurationType.DEVELOPMENT;
	}
}
