package net.julienlecomte.gridimagesearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
	private static final long serialVersionUID = 1L;
	private String fullUrl;
	private String thumbUrl;

	public ImageResult(JSONObject o) {
		try {
			fullUrl = o.getString("url");
			thumbUrl = o.getString("tbUrl");
		} catch (JSONException e) {
			fullUrl = null;
			thumbUrl = null;
		}
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public String toString() {
		return "<img src=\"" + fullUrl + "\">";
	}

	public static ArrayList<ImageResult> fromJSONArray(JSONArray jsonResults) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int i = 0; i < jsonResults.length(); i++) {
			try {
				JSONObject o = jsonResults.getJSONObject(i);
				results.add(new ImageResult(o));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
}
