package cs155.aesthetic;

import cs155.core.Color3D;

/**
 * The super class of all textures. It defines a map from texture coordinates to
 * Color3D objects Textures can be scaled and translated.
 **/
public class Texture {
	public static final Texture WHITE = new Texture();
	private double uScale;
	private double vScale;
	private double uOffset;
	private double vOffset;

	public Texture() {
		this.uScale = 1.0;
		this.vScale = 1.0;
		this.uOffset = 0.0;
		this.vOffset = 0.0;
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

	public double getuScale() {
		return uScale;
	}

	public void setuScale(double uScale) {
		this.uScale = uScale;
	}

	public double getvScale() {
		return vScale;
	}

	public void setvScale(double vScale) {
		this.vScale = vScale;
	}

	public double getuOffset() {
		return uOffset;
	}

	public void setuOffset(double uOffset) {
		this.uOffset = uOffset;
	}

	public double getvOffset() {
		return vOffset;
	}

	public void setvOffset(double vOffset) {
		this.vOffset = vOffset;
	}
}
