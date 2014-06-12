package net.julienlecomte.gridimagesearch;

import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.os.Bundle;

public class ImageDisplayActivity extends Activity {
	SmartImageView sivImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);

		sivImage = (SmartImageView) findViewById(R.id.sivResult);
		ImageResult imageResult = (ImageResult) getIntent().getSerializableExtra("image");
		String url = imageResult.getFullUrl();
		sivImage.setImageUrl(url);
	}
}
