package cs155.jray;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import javax.imageio.ImageIO;

/**
 * Use an image (specified by a filename) to generate a texture
 **/
public class ImageTexture extends Texture {
	public BufferedImage buf;
	private int w, h;

	public ImageTexture(String filename) {
		super();
		FileInputStream f;
		buf = null;
		w = h = 1;
		try {
			f = new FileInputStream(filename);
			buf = ImageIO.read(f);
			w = buf.getWidth();
			h = buf.getHeight();
		} catch (Exception e) {
			System.out.println("Can't load texture " + e);
		}
	}

	/**
	 * This returns a Color corresponding to the pixel whose location in the
	 * image is (x,y) after the image is normalized to have size 1.0 x 1.0
	 **/
	public Color3D getColor(TextureCoordinate tc) {
		if ((buf == null) || (w < 1) || (h < 1))
			return Color3D.WHITE;

		int i = (int) Math.floor((tc.x + uOffset) * uScale);
		int j = (int) Math.floor((tc.y + vOffset) * vScale);
		i = i % w;
		j = j % h;
		if (i < 0)
			i += w;
		if (j < 0)
			j += h;

		return new Color3D(new Color(buf.getRGB(i, j)));
	}

}
