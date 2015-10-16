package cs155.jray;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PngCanvas3D implements Canvas3D {
	
	private BufferedImage bufferedImage;
	private File file;

	
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
		try {
			ImageIO.write(bufferedImage, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
