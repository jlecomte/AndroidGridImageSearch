package net.julienlecomte.gridimagesearch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
		super(context, R.layout.item_image_result, images);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult image = this.getItem(position);

		SmartImageView sivImage;

		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			sivImage = (SmartImageView) inflator.inflate(R.layout.item_image_result, parent, false);
		} else {
			sivImage = (SmartImageView) convertView;
			sivImage.setImageResource(android.R.color.transparent);
		}

		sivImage.setImageUrl(image.getThumbUrl());

		return sivImage;
	}
}
