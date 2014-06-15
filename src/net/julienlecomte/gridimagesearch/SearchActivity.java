package net.julienlecomte.gridimagesearch;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
// import android.util.Log;
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
	public static final String SETTINGS_FILE_NAME = "settings";

	EditText etSearchBox;
	Button btnSearch;
	GridView gvResults;
	ArrayList<ImageResult> results;
	ImageResultArrayAdapter adapter;
	String query = "";
	SearchSettings settings;

	private final int SETTINGS_ACTIVITY_REQUEST_CODE = 50; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		setupViews();

		try {
			loadSettings();
		} catch (Exception e) {
			settings = new SearchSettings();
		}

		results = new ArrayList<ImageResult>();
		adapter = new ImageResultArrayAdapter(this, results);
		gvResults.setAdapter(adapter);

		gvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				search(totalItemsCount);
			}
		});

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

	void loadSettings() throws Exception {
		FileInputStream fis = this.openFileInput(SETTINGS_FILE_NAME);
		ObjectInputStream is = new ObjectInputStream(fis);
		settings = (SearchSettings) is.readObject();
		is.close();
	}

	void storeSettings() throws Exception {
		FileOutputStream fos = this.openFileOutput(SETTINGS_FILE_NAME, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(settings);
		os.close();
	}

	public void onSearchButtonClick(View v) {
		String s = etSearchBox.getText().toString().trim();

		if (s.equals(query)) {
			return;
		}

		query = s;
		adapter.clear();

		if (query.length() > 0) {
			Toast.makeText(this, "Searching for " + query,  Toast.LENGTH_SHORT).show();
			search(0);
		}
	}

	public void search(int offset) {
		AsyncHttpClient http = new AsyncHttpClient();

		String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8" +
				"&start=" + offset +
				"&q=" + Uri.encode(query);

		switch (settings.imageSize) {
		case LARGE:
			url += "&imgsz=large";
			break;
		case MEDIUM:
			url += "&imgsz=medium";
			break;
		case SMALL:
			url += "&imgsz=small";
			break;
		default:
			break;
		}

		switch (settings.imageColor) {
		case BlACK_AND_WHITE_ONLY:
			url += "&imgc=gray";
			break;
		case COLOR_ONLY:
			url += "&imgc=color";
			break;
		default:
			break;
		}

		if (settings.safesearch) {
			url += "&safe=active";
		}

		http.get(url,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						JSONArray jsonResults = null;
						try {
							jsonResults = response.getJSONObject("responseData").getJSONArray("results");
							adapter.addAll(ImageResult.fromJSONArray(jsonResults));
							// Log.d("DEBUG", results.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

	public void onSettings(MenuItem mi) {
		Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, SettingsActivity.class);
		i.putExtra("settings", settings);
		startActivityForResult(i, SETTINGS_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SETTINGS_ACTIVITY_REQUEST_CODE) {
			settings = (SearchSettings)data.getSerializableExtra("settings");
			if (query.length() > 0) {
				// TODO: refresh...
			}
		}
	}

	public void onClear(View v) {
		query = "";
		etSearchBox.setText(query);
		adapter.clear();
	}
}
