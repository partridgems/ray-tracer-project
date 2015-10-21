package cs155.jray;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PngCanvas3D implements Canvas3D {
	
	private BufferedImage bufferedImage;

	
	/**
	 * Creates a canvas for drawing to a png file
	 * Without film, this canvas expects user to draw directly on the canvas via
	 * the drawPixel() from the Canvas3D interface. To use with film, call other constructor.
	 * 
	 * @param width Width in pixels of the image
	 * @param height Height in pixels of the image
	 */
	public PngCanvas3D(int width, int height) {
		this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
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
	public void refresh(String name) {
		System.setProperty("java.awt.headless", "true"); // Prevents stupid window from opening when writing to a file
		try {
			File file = new File(name + ".png");
			ImageIO.write(bufferedImage, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @Override
    public void done() {

    }


}
