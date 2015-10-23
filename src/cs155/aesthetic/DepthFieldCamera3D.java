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

public class DepthFieldCamera3D extends Camera3D {

	private static final Point3D origin = new Point3D(0d, 0d, 0d);

	private double depth; // Depth into the scene for the focal point. Uses the normalized coordinate system
	private double aperture;

	public DepthFieldCamera3D(Canvas3D f, double depth, double aperture) {
		this(f, Transform3D.IDENTITY, depth, aperture);
	}

	public DepthFieldCamera3D(Canvas3D f, Transform3D tr, double depth, double aperture) {
		super(f);
		this.setFilm(f);
		this.setTransform(tr);
		this.depth = depth;
		this.aperture = aperture;
	}

	/**
	 * Convert screen coordinates into a ray. Given doubles u,v which are both
	 * between -1 and 1, this method then shoots a ray from a radius around the camera
	 * out through u,v on the plane in the scene at the specified depth. The ray goes
	 * through the focal point in the scene--wherever the original ray would have passed
	 * the specified focal distance. By combining many of these rays, a depth of field
	 * blur is created.
	 **/
	public Ray3D generateRay(int i, int j) {
		/*
		 * the screen coordinates have the y axis pointed down! so we need to
		 * compensate by letting k represent the distance in pixels from the
		 * base!
		 */
		
		// First, we calculate a normalized u,v coordinate as in the traditional lens.
		int k = getFilm().height() - j;
		double xjitter = Math.random()-0.5;
		double yjitter = Math.random()-0.5;
		double u = 2 * (i + xjitter - getFilm().width() / 2d) / getFilm().height();
		double v = 2 * (k + yjitter - getFilm().height() / 2d) / getFilm().height();
		
		// Cast a ray to find the focal point
		Ray3D r = new Ray3D(origin, new Point3D(u, v, SCREEN_DIST));
		
		// Choose a random point on the 'aperture'
		double xblur = (Math.random() - 0.5)/aperture;
		double yblur = (Math.random() - 0.5)/aperture;
		
		// Get the focal point, the blurred eye, and cast the new ray
		Point3D focus = r.atTime(depth);
		Point3D eye = origin.translate(yblur, xblur, 0);
		Ray3D bluredRay = new Ray3D(eye, focus.subtract(eye));
		
		return bluredRay.applyTransform(this.getTransform());
	}
}
