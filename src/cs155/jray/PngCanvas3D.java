package cs155.jray;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PngCanvas3D implements Canvas3D {
	
	private Film film;
	private BufferedImage bufferedImage;
	private File file;

	/**
	 * Creates a canvas for drawing to a png file
	 * Pass in film to color film and then copy to file
	 * 
	 * @param film When refresh() is called, copies image data from film to output file
	 * @param filename File to write output
	 */
	public PngCanvas3D(Film film, String filename) {
		this.film = film;
		this.bufferedImage = new BufferedImage(film.width(), film.height(), BufferedImage.TYPE_INT_RGB);
		this.file = new File(filename);
	}
	
	/**
	 * Creates a canvas for drawing to a png file
	 * Without film, this canvas expects user to draw directly on the canvas via
	 * the drawPixel() from the Canvas3D interface. To use with film, call other constructor.
	 * 
	 * @param width Width in pixels of the image
	 * @param height Height in pixels of the image
	 * @param filename File to write output
	 */
	public PngCanvas3D(int width, int height, String filename) {
		this.film = null;
		this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.file = new File(filename);
	}

	@Override
	public int height() {
		return bufferedImage.getHeight();
	}

	@Override
	public int width() {
		return bufferedImage.getWidth();
	}

	@Override
	public void drawPixel(int i, int j, Color c) {
		bufferedImage.setRGB(i, j, c.getRGB());
	}

	@Override
	public void refresh() {
		if (film != null) { // Only copy film data if we expect there to be any
			copyFilmToFile();
		}
		try {
			ImageIO.write(bufferedImage, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void copyFilmToFile() {
		for (int i = 0; i < film.width(); i++) {
			for (int j = 0; j < film.height(); j++) {
				if (film.pixels[i][j] != null) {
					this.drawPixel(i, j, film.pixels[i][j]);
				}
			}
		}
	}

}
