package net.julienlecomte.gridimagesearch;

import java.io.Serializable;

public class SearchSettings implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum ImageSize {
		ANY,
		SMALL,
		MEDIUM,
		LARGE
	};

	public enum ImageColor {
		ANY,
		BlACK_AND_WHITE_ONLY,
		COLOR_ONLY
	};

	public ImageSize imageSize;
	public ImageColor imageColor;
	public boolean safesearch;

	public SearchSettings() {
		this.imageSize = ImageSize.ANY;
		this.imageColor = ImageColor.ANY;
		this.safesearch = false;
	}
}
