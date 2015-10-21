package cs155.aesthetic;

import cs155.core.Color3D;

import java.awt.Color;

/**
 * This computes a checkerboard texture
 **/
public class CheckerTexture extends Texture {
	public Color3D white = new Color3D(1d, 1d, 1d), black = new Color3D(0d, 0d,
			0d);

	public CheckerTexture() {
		;
	}

	/**
	 * select colors for the "white" and "black" checkerboard spaces
	 **/
	public CheckerTexture(Color white, Color black) {
		super();
		this.white = new Color3D(white);
		this.black = new Color3D(black);
	}

	/**
	 * This returns a Color corresponding to the pixel whose location in the
	 * image is (x,y) where the checkerboard squares have size 100x100
	 **/
	public Color3D getColor(TextureCoordinate tc) {
		int i = (int) Math.floor((tc.x + uOffset) * uScale / 100);
		int j = (int) Math.floor((tc.y + vOffset) * vScale / 100);
		boolean isWhite = ((i + j) % 2) == 0;
		if (isWhite)
			return white;
		else
			return black;
	}

}
