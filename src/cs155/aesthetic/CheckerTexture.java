package cs155.aesthetic;

import cs155.core.Color3D;

import java.awt.Color;

/**
 * This computes a checkerboard texture
 **/
public class CheckerTexture extends Texture {
	private Color3D white;
	private Color3D black;

	public CheckerTexture() {
		this(new Color(1f, 1f, 1f), new Color(0f, 0f, 0f));
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
		int i = (int) Math.floor((tc.getX() + getuOffset()) * getuScale() / 100);
		int j = (int) Math.floor((tc.getY() + getvOffset()) * getvScale() / 100);
		boolean isWhite = ((i + j) % 2) == 0;
		if (isWhite)
			return white;
		else
			return black;
	}

	public Color3D getWhite() {
		return white;
	}

	public void setWhite(Color3D white) {
		this.white = white;
	}

	public Color3D getBlack() {
		return black;
	}

	public void setBlack(Color3D black) {
		this.black = black;
	}
}
