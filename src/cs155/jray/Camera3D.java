package cs155.jray;

import java.awt.Color;

/**
 * This class represents a camera. It is initially at (0,0,0) facing down the
 * z-axis (i.e. in the direction (0,0,-1)) The camera object is responsible for
 * generating rays corresponding to integer-valued screen coordinates. The
 * screen coordinates depend on the size of the screen which is specified by the
 * Film object.
 * 
 * You create a camera by passing in a film parameter which determines how the
 * pixels will be converted to rays based on the width and height of the film
 * object as measured in pixels.
 **/

public class Camera3D {

	private Point3D origin = new Point3D(0d, 0d, 0d);
	public Canvas3D film;
	public Transform3D transform;

	public double screenDist = -1d; // -1d default

	public Camera3D(Canvas3D f) {
		this(f, Transform3D.IDENTITY);
	}

	public Camera3D(Canvas3D f, Transform3D tr) {
		this.film = f;
		this.transform = tr;

	}

	public void apply(Transform3D t) {
		this.transform = Transform3D.compose(this.transform, t);
	}

	/**
	 * Convert screen coordinates into a ray. Given doubles u,v which are both
	 * between -1 and 1, this method returns the corresponding ray which starts
	 * at the location of the camera and then passes through the virtual screen
	 * which is 1 unit if front of the camera and at position (u,v) relative to
	 * the center of that plane.
	 **/
	public Ray3D generateRay(int i, int j) {
		/*
		 * the screen coordinates have the y axis pointed down! so we need to
		 * compensate by letting k represent the distance in pixels from the
		 * base!
		 */
		int k = film.height() - j;
		double xjitter = Math.random()-0.5;
		double yjitter = Math.random()-0.5;
		double u = 2 * (i + xjitter - film.width() / 2d) / film.height();
		double v = 2 * (k + yjitter - film.height() / 2d) / film.height();
		Ray3D r = new Ray3D(origin, new Point3D(u, v, screenDist));
		return r.applyTransform(this.transform);
	}

	public void setPixelColor(int i, int j, Color c) {
		film.drawPixel(i, j, c);
	}

}
