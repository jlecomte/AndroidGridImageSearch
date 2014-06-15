package net.julienlecomte.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class SettingsActivity extends Activity {
	RadioButton rAnySize;
	RadioButton rSmallSize;
	RadioButton rMediumSize;
	RadioButton rLargeSize;
	
	RadioButton rAnyColor;
	RadioButton rBlackAndWhiteOnly;
	RadioButton rColorOnly;

	RadioGroup rgImageSize;
	RadioGroup rgImageColor;
	
	Switch swSafeSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		setupViews();
		initUIState();
	}

	void setupViews() {
		rAnySize = (RadioButton)findViewById(R.id.rAnySize);
		rSmallSize = (RadioButton)findViewById(R.id.rSmallSize);
		rMediumSize = (RadioButton)findViewById(R.id.rMediumSize);
		rLargeSize = (RadioButton)findViewById(R.id.rLargeSize);

		rAnyColor = (RadioButton)findViewById(R.id.rAnyColor);
		rBlackAndWhiteOnly = (RadioButton)findViewById(R.id.rBlackAndWhiteOnly);
		rColorOnly = (RadioButton)findViewById(R.id.rColorOnly);

		rgImageSize = (RadioGroup)findViewById(R.id.rgImageSize);
		rgImageColor = (RadioGroup)findViewById(R.id.rgImageColor);
		
		swSafeSearch = (Switch) findViewById(R.id.swSafeSearch);
	}

	void initUIState() {
		SearchSettings settings = (SearchSettings)getIntent().getSerializableExtra("settings");

		switch (settings.imageSize) {
		case ANY:
			rAnySize.setChecked(true);
			break;
		case SMALL:
			rSmallSize.setChecked(true);
			break;
		case MEDIUM:
			rMediumSize.setChecked(true);
			break;
		case LARGE:
			rLargeSize.setChecked(true);
			break;
		}

		switch (settings.imageColor) {
		case ANY:
			rAnyColor.setChecked(true);
			break;
		case BlACK_AND_WHITE_ONLY:
			rBlackAndWhiteOnly.setChecked(true);
			break;
		case COLOR_ONLY:
			rColorOnly.setChecked(true);
			break;
		}

		swSafeSearch.setChecked(settings.safesearch);
	}

	SearchSettings getUIState() {
		SearchSettings settings = new SearchSettings();
		
		switch (rgImageSize.getCheckedRadioButtonId()) {

		case R.id.rAnySize:
			settings.imageSize = SearchSettings.ImageSize.ANY;
			break;

		case R.id.rSmallSize:
			settings.imageSize = SearchSettings.ImageSize.SMALL;
			break;

		case R.id.rMediumSize:
			settings.imageSize = SearchSettings.ImageSize.MEDIUM;
			break;

		case R.id.rLargeSize:
			settings.imageSize = SearchSettings.ImageSize.LARGE;
			break;
		}

		switch (rgImageColor.getCheckedRadioButtonId()) {

		case R.id.rAnyColor:
			settings.imageColor = SearchSettings.ImageColor.ANY;
			break;

		case R.id.rBlackAndWhiteOnly:
			settings.imageColor = SearchSettings.ImageColor.BlACK_AND_WHITE_ONLY;
			break;

		case R.id.rColorOnly:
			settings.imageColor = SearchSettings.ImageColor.COLOR_ONLY;
			break;
		}

		settings.safesearch = swSafeSearch.isChecked();

		return settings;
	}

	public void onSave(View v) {
		SearchSettings settings = getUIState();
		Intent i = new Intent();
		i.putExtra("settings", settings);
		setResult(RESULT_OK, i);
		finish();
	}
}
