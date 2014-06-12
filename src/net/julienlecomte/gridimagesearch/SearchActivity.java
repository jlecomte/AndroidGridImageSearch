package net.julienlecomte.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	protected EditText etSearchBox;
	protected Button btnSearch;
	protected GridView gvResults;
	ArrayList<ImageResult> results;
	ImageResultArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		setupViews();

		results = new ArrayList<ImageResult>();
		adapter = new ImageResultArrayAdapter(this, results);
		gvResults.setAdapter(adapter);

		gvResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ImageResult target = results.get(position);
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				i.putExtra("image", target);
				startActivity(i);
			}
		});
	}

	protected void setupViews() {
		etSearchBox = (EditText) findViewById(R.id.etSearchBox);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		gvResults = (GridView) findViewById(R.id.gvResults);
	}

	public void onSearchButtonClick(View v) {
		String query = etSearchBox.getText().toString();
		Toast.makeText(this, "Searching for " + query,  Toast.LENGTH_SHORT).show();

		AsyncHttpClient http = new AsyncHttpClient();

		http.get("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8" +
					"&start=" + 0 +
					"&q=" + Uri.encode(query),
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject response) {
						JSONArray jsonResults = null;
						try {
							jsonResults = response.getJSONObject("responseData").getJSONArray("results");
							results.clear();
							adapter.addAll(ImageResult.fromJSONArray(jsonResults));
							Log.d("DEBUG", results.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
}
