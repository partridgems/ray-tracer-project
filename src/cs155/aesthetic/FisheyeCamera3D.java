package cs155.aesthetic;

import cs155.core.Camera3D;
import cs155.core.Point3D;
import cs155.core.Ray3D;
import cs155.core.Transform3D;
import cs155.core.Canvas3D;

/**
 * This class represents a fisheye camera. It is initially at (0,0,0) facing down the
 * z-axis (i.e. in the direction (0,0,-1)) The camera object is responsible for
 * generating rays corresponding to integer-valued screen coordinates. The
 * screen coordinates depend on the size of the screen which is specified by the
 * Film object. In addition, the rays are projected through a curved lens to produce 
 * the fisheye effect. In essence, the rays are more spread out than normal.
 * 
 * You create a camera by passing in a film parameter which determines how the
 * pixels will be converted to rays based on the width and height of the film
 * object as measured in pixels.
 **/

public class FisheyeCamera3D extends Camera3D {

	private static final Point3D origin = new Point3D(0d, 0d, 0d);

	private double fishEyeAngle = Math.PI; // Angle of view for the lens in radians (default, pi)

	public FisheyeCamera3D(Canvas3D f) {
		this(f, Transform3D.IDENTITY);
	}

	public FisheyeCamera3D(Canvas3D f, Transform3D tr) {
		super(f);
		this.setFilm(f);
		this.setTransform(tr);
	}

	/**
	 * Convert screen coordinates into a ray. Given doubles u,v which are both
	 * between -1 and 1, this method returns then projects the corresponding ray 
	 * through a hemisphere by finding spherical coordinates for the ray, 
	 * which starts at the location of the camera and then passes through the 
	 * virtual screen on the hemisphere of radius 1, at depth 1 unit if front of the camera 
	 * and at position (u,v) relative to the center of that hemisphere.
	 **/
	public Ray3D generateRay(int i, int j) {
		/*
		 * the screen coordinates have the y axis pointed down! so we need to
		 * compensate by letting k represent the distance in pixels from the
		 * base!
		 */
		
		// First, we calculate a normalized u,v coordinate as in the traditional lens.
		int k = getFilm().height() - j;
//		 double xjitter = Math.random()-0.5; // Jitter is useful when performing oversampling,
//		 double yjitter = Math.random()-0.5; // but oversampling is not currently implemented
		double u = 2 * (i - getFilm().width() / 2d) / getFilm().height();
		double v = 2 * (k - getFilm().height() / 2d) / getFilm().height();
		
		// Next, find polar coordinates for u,v
		double r = Math.sqrt(u*u + v*v);
		if (r > 1) { // Reject points outside of the image
			return Ray3D.NOT_IN_SCENE;
		}
		double theta = Math.atan2(v, u); // Theta is the angle from the x axis in the xz plane
		
		// Project polar coordinates down to spherical using the fisheye angle
		double rho = r * this.fishEyeAngle / 2; // Rho is the angle from the y axis in the vertical plane rotated from xy by theta
		
		// Finally convert those spherical coordinates into cartesian for creating the ray
		// Note that the radius of our sphere is 1.0, not r as calculated above
		Point3D direction = new Point3D(Math.cos(theta)*Math.sin(rho), Math.sin(theta)*Math.sin(rho), -Math.cos(rho));
		
		Ray3D ray = new Ray3D(origin, direction);
		return ray.applyTransform(this.getTransform());
	}

}
