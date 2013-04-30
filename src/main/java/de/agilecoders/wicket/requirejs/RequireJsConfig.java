package de.agilecoders.wicket.requirejs;

import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.settings.IJavaScriptLibrarySettings;
import org.apache.wicket.util.string.Strings;

/**
 *
 */
class RequireJsConfig extends HeaderResponseContainer
{
	static final String FILTER_NAME = "require-js-config";

	public RequireJsConfig()
	{
		super("config", FILTER_NAME);
	}

	@Override
	public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
	{
		FilteringHeaderResponse response = FilteringHeaderResponse.get();
		if (!response.isClosed())
		{
			throw new RuntimeException(
					"there was an error processing the header response - you tried to render a bucket of response from FilteringHeaderResponse, but it had not yet run and been closed.  this should occur when the header container that is standard in wicket renders, so perhaps you have done something to keep that from rendering?");
		}

		String configPaths = response.getContent(FILTER_NAME).toString();

		StringBuilder content = new StringBuilder();

		try
		{
			JSONObject requireConfig = new JSONObject();

			JSONObject shim = new JSONObject();
			requireConfig.put("shim", shim);

			JSONObject shimWicketEvent = new JSONObject();
			shim.put("wicket-event", shimWicketEvent);
			shimWicketEvent.put("deps", new JSONArray("[jquery]"));
			shimWicketEvent.put("exports", "wicket-event");

			JSONObject shimWicket = new JSONObject();
			shim.put("Wicket", shimWicket);
			shimWicket.put("deps", new JSONArray("[wicket-event]"));
			shimWicket.put("exports", "Wicket");

			JSONObject paths = new JSONObject();
			requireConfig.put("paths", paths);

			IJavaScriptLibrarySettings javaScriptLibrarySettings = getApplication().getJavaScriptLibrarySettings();
			paths.put("jquery", urlFor(javaScriptLibrarySettings.getJQueryReference(), null));
			paths.put("wicket-event", urlFor(javaScriptLibrarySettings.getWicketEventReference(), null));
			paths.put("Wicket", urlFor(javaScriptLibrarySettings.getWicketAjaxReference(), null));

			String[] items = Strings.split(configPaths, '\n');
			for (String item : items)
			{
				if (Strings.isEmpty(item) == false)
				{
					String[] parts = Strings.split(item, '=');
					paths.put(parts[0], parts[1]);
				}
			}

			content.append("<script>\nvar require = ");
			content.append(requireConfig.toString(2)).append(';');
			content.append("</script>\n");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		replaceComponentTagBody(markupStream, openTag, content);
	}

}
