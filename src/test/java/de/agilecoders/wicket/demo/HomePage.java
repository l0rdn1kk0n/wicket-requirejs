package de.agilecoders.wicket.demo;

import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.requirejs.AmdModuleHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

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

//		response.render(AmdModuleHeaderItem.forReference(new DemoRef(), "demo"));
        response.render(AmdModuleHeaderItem.forReference(new JavaScriptResourceReference(HomePage.class, "demo1.js"), "demo"));
		response.render(AmdModuleHeaderItem.forReference(new JavaScriptResourceReference(PageB.class, "pageB.js"), "pageB"));
        response.render(AmdModuleHeaderItem.forReference(new JavaScriptResourceReference(PageC.class, "pageC.js"), "pageC"));
	}

    private static final class DemoRef extends JavaScriptResourceReference {

        public DemoRef() {
            super(HomePage.class, "demo1.js");
        }

        @Override
        public Iterable<? extends HeaderItem> getDependencies() {
            List<HeaderItem> deps = new ArrayList<>();
            deps.add(AmdModuleHeaderItem.forReference(new JavaScriptResourceReference(PageB.class, "pageB.js"), "pageB"));
            return deps;
        }
    }
}
