package cs155.jray;

import java.awt.Color;

public class Film implements Canvas3D {

	public Color[][] pixels;
	private int w, h;

	public Film(int w, int h) {
		this.w = w;
		this.h = h;
		pixels = new Color[w][h];
		initializeFilm();

	}

	/**
	 * fill the Film object with white pixels
	 */
	private void initializeFilm() {
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				pixels[i][j] = Color.white;
			}
		}
	}

	@Override
	public int height() {
		return this.h;
	}

	@Override
	public int width() {
		return this.w;
	}

	@Override
	public void drawPixel(int i, int j, Color c) {
		this.pixels[i][j] = c;

	}

	@Override
	public void refresh() {
		throw new RuntimeException("Film is not a true implementation of the Canvas3D API and does not support actual drawing!");
	}

}
