package cs155.jray;

/**
 * The super class of all textures. It defines a map from texture coordinates to
 * Color3D objects Textures can be scaled and translated.
 **/
public class Texture {
	public final static Texture WHITE = new Texture();
	public double uScale = 1.0, vScale = 1.0, uOffset = 0.0, vOffset = 0.0;;

	public Texture() {
		;
	}

	/**
	 * This returns a Color corresponding to the specified TextureCoordinates
	 **/
	public Color3D getColor(TextureCoordinate tc) {
		return Color3D.WHITE;
	}

	public void scale(double x, double y) {
		uScale *= x;
		vScale *= y;
	}

	public void translate(double x, double y) {
		uOffset += x;
		vOffset += y;
	}

}
