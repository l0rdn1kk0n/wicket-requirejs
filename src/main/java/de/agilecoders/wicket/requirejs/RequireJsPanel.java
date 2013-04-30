package de.agilecoders.wicket.requirejs;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;

/**
 *
 */
public class RequireJsPanel extends Panel
{
	public RequireJsPanel(String id)
	{
		super(id);

		add(new RequireJsConfig());

		WebComponent requireJsScript = new WebComponent("require.js");
		requireJsScript.add(AttributeModifier.replace("src", getRequireJsUrl()));
		add(requireJsScript);
	}

	private IModel<String> getRequireJsUrl()
	{
		return new AbstractReadOnlyModel<String>()
		{
			@Override
			public String getObject()
			{
				ResourceReference requireJsReference;
				if (Application.exists()) {
					IRequireJsSettings settings = RequireJs.settings();
					requireJsReference = settings.getResourceReference();
				} else {
					requireJsReference = new WebjarsJavaScriptResourceReference("requirejs/current/require.js");
				}

				return urlFor(requireJsReference, null).toString();
			}
		};
	}
}
