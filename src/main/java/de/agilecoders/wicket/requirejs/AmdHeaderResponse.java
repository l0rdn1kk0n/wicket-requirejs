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
	private final List<AmdJavaScriptHeaderItem> amdItems = new ArrayList<>();
	/**
	 * Create a header response that simply delegates all methods to the one that is passed in here.
	 *
	 * @param real the actual response that this class delegates to by default
	 */
	public AmdHeaderResponse(IHeaderResponse real)
	{
		super(real);
	}

	@Override
	public void render(HeaderItem item)
	{
		if (item instanceof AmdJavaScriptHeaderItem)
		{
			if (amdItems.isEmpty())
			{
				amdItems.add(AmdJavaScriptHeaderItem.forReference(JQueryResourceReference.get(), "jquery"));
				amdItems.add(AmdJavaScriptHeaderItem.forReference(WicketEventJQueryResourceReference.get(), "wicket-event"));
				amdItems.add(AmdJavaScriptHeaderItem.forReference(WicketAjaxJQueryResourceReference.get(), "Wicket"));
			}

			amdItems.add((AmdJavaScriptHeaderItem) item);
		}
		else
		{
			super.render(item);
		}
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
		JSONObject requireConfig = new JSONObject();
		JSONObject paths = new JSONObject();
		try
		{
			requireConfig.put("paths", paths);

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

		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		for (AmdJavaScriptHeaderItem item : amdItems)
		{
			try
			{
				paths.put(item.getName(), item.getUrl());
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<script>\nvar require = ");
		sb.append(requireConfig.toString()).append(";");
		sb.append("</script>\n");

		return sb;
	}
}
