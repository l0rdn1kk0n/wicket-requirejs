package de.agilecoders.wicket.requirejs;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.WicketAjaxJQueryResourceReference;
import org.apache.wicket.ajax.WicketEventJQueryResourceReference;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.DecoratingHeaderResponse;
import org.apache.wicket.resource.JQueryResourceReference;

/**
 *
 */
public class AmdHeaderResponse extends DecoratingHeaderResponse
{
	private final List<AmdJavaScriptHeaderItem> amdItems;
	/**
	 * Create a header response that simply delegates all methods to the one that is passed in here.
	 *
	 * @param real the actual response that this class delegates to by default
	 */
	public AmdHeaderResponse(IHeaderResponse real)
	{
		super(real);

		this.amdItems = new ArrayList<>();
	}

	@Override
	public void render(HeaderItem item)
	{
		if (item instanceof AmdJavaScriptHeaderItem)
		{
			if (amdItems.isEmpty())
			{
				// contribute Wicket resources lazily
				amdItems.add(AmdJavaScriptHeaderItem.forReference(JQueryResourceReference.get(), "jquery"));
				amdItems.add(AmdJavaScriptHeaderItem.forReference(WicketEventJQueryResourceReference.get(), "wicket-event"));
				amdItems.add(AmdJavaScriptHeaderItem.forReference(WicketAjaxJQueryResourceReference.get(), "Wicket"));
			}

			amdItems.add((AmdJavaScriptHeaderItem) item);
		}

		super.render(item);
	}

	@Override
	public void close()
	{
		CharSequence headerContent = getContent();
		amdItems.clear();
		getResponse().write(headerContent);

		super.close();
	}

	private CharSequence getContent()
	{
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
			for (AmdJavaScriptHeaderItem item : amdItems)
			{
				paths.put(item.getName(), item.getUrl());
			}

			content.append("<script>\nvar require = ");
			content.append(requireConfig.toString(2)).append(";");
			content.append("</script>\n");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		return content;
	}
}
