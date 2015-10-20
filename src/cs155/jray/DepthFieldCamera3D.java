package cs155.jray;

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

	private Point3D origin = new Point3D(0d, 0d, 0d);

	private double depth; // Depth into the scene for the focal point. Uses the normalized coordinate system
	private double aperture;

	public DepthFieldCamera3D(Canvas3D f, double depth, double aperture) {
		this(f, Transform3D.IDENTITY, depth, aperture);
	}

	public DepthFieldCamera3D(Canvas3D f, Transform3D tr, double depth, double aperture) {
		super(f);
		this.film = f;
		this.transform = tr;
		this.depth = depth;
		this.aperture = aperture;
	}

	/**
	 * Convert screen coordinates into a ray. Given doubles u,v which are both
	 * between -1 and 1, this method then shoots a ray from a radius around the camera
	 * out through u,v on the plane in the scene at the specified depth.
	 **/
	public Ray3D generateRay(int i, int j) {
		/*
		 * the screen coordinates have the y axis pointed down! so we need to
		 * compensate by letting k represent the distance in pixels from the
		 * base!
		 */
		
		// First, we calculate a normalized u,v coordinate as in the traditional lens.
		int k = film.height() - j;
		double xjitter = Math.random()-0.5;
		double yjitter = Math.random()-0.5;
		double u = 2 * (i + xjitter - film.width() / 2d) / film.width();
		double v = 2 * (k + yjitter - film.height() / 2d) / film.height();
		
		Ray3D r = new Ray3D(origin, new Point3D(u, v, SCREEN_DIST));
		
		double xblur = (Math.random() - 0.5)/aperture;
		double yblur = (Math.random() - 0.5)/aperture;
		Point3D focus = r.atTime(depth);
		Point3D eye = origin.translate(yblur, xblur, 0);
		Ray3D bluredRay = new Ray3D(eye, focus.subtract(eye));
		
//		// Distance from the camera out to where this ray intersects the focal plane
//		double distanceToFocalPlane = this.depth/r.d.dot(new Point3D(0, 0, -1));
//		
//		// Translates the ray back to the origin(camera) to rotate it for the blur effect
//		Transform3D backToCamera = new Transform3D().translate(r.d.scale(-distanceToFocalPlane));
//		Transform3D fwdFromCamera = new Transform3D().translate(r.d.scale(distanceToFocalPlane));
//		
//		// Stochastically rotate this ray depending on distance to the focal plane
//		double xRotate = Math.toDegrees(Math.atan( (Math.random()-.5)*aperture/depth ));
//		double yRotate = Math.toDegrees(Math.atan( (Math.random()-.5)*aperture/depth ));
//		Transform3D rotate = new Transform3D().rotateX(xRotate).rotateY(yRotate);
//		
//		// Now change the ray's direction to cause the blur
//		Transform3D fullTransform = backToCamera.rotate.fwdFromCamera;

//		r.d = fullTransform.applyTo(r.d);
		
		return bluredRay.applyTransform(this.transform);
	}
}
