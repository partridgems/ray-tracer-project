package cs155.core;

/**
 * This represents 3D lights with the full OpenGL color model. So there are
 * several parameters including Color3D values for the ambient, diffuse, and
 * specular illumination as well as the attenuation coefficients: attC, attL,
 * attQ - representing the Constant, Linear, and Quadratic components Finally we
 * include an intensity to make it easy to increase the brightness of a all
 * components of a light.
 **/

public class Light3D {
	private static final boolean blinnPhong = true; // Set for true for BlinnPhong specular,
													 // false for Phong
	
	/** location of the light **/
	private Point3D location = new Point3D(0d, 0d, 0d);
	private static Color3D dimGray = new Color3D(0.2, 0.2, 0.2);

	/** intensity of the light **/
	public double intensity = 1.0;

	/** ambient, diffuse, and specular components of the light **/
	public Color3D ambient = dimGray, diffuse = Color3D.WHITE,
			specular = Color3D.WHITE;
	
	public Transform3D transform = Transform3D.IDENTITY;

	/** attenuation coefficients: constant, linear, and quadratic **/
	public double attC = 1.0, attL = 0.001, attQ = 0.00001;

	/** create a default white light at the origin **/
	public Light3D() {
	}

	/** create a default white light at a specified location **/
	public Light3D(Point3D new_location) {
		location = new Point3D(new_location);
	}

	public Light3D(Point3D new_location, double intensity) {
		location = new Point3D(new_location);
		this.intensity = intensity;
	}

	public Light3D(Point3D new_location, double intensity, Color3D amb,
			Color3D dif, Color3D sp) {
		location = new Point3D(new_location);
		this.intensity = intensity;
		this.ambient = amb;
		this.diffuse = dif;
		this.specular = sp;
	}
	
	public Point3D getLocation() {
		return transform.applyTo(location);
	}

	/** this returns the spotlight intensity which is 1.0 for all non-spotlights **/
	public double spot(Point3D lookAt) {
		return 1.0;
	}

	/**
	 * calculate the attentuation of the light based on its distance from the
	 * illuminated point
	 **/
	public double attenuation(double length) {
		return 1.0 / (attC + attL * length + attQ * length * length);
	}
	
	public void setTransform(Transform3D t){
		this.transform = t;
	}
	
	public void applyTrans(Transform3D t){
		transform = Transform3D.compose(transform, t);
	}

	/**
	 * calculate the specular intensity of the light
	 **/
	public static double specular(Point3D lightVec, Point3D normal,
			Point3D eyeVec, int hardness) {
		if (blinnPhong) {
			return getBlinnPhong(lightVec, normal, eyeVec, hardness);
		} else {
			return getPhong(lightVec, normal, eyeVec, hardness);
		}
	}
	
	private static double getPhong(Point3D lightVec, Point3D normal, Point3D eyeVec, int hardness) {
		// Reflect the light ray in the surface
		Point3D lightReflect = normal.scale( 2*lightVec.dot(normal) ).subtract(lightVec);
		
		// Phong illumination is (angle between reflected light and eye)^hardness
		// Return positive value or 0.0
		return lightReflect.dot(eyeVec) > 0 ? Math.pow(lightReflect.dot(eyeVec), hardness) : 0.0;
	}
	
	private static double getBlinnPhong(Point3D lightVec, Point3D normal, Point3D eyeVec, int hardness) {
		Point3D u = lightVec;
		Point3D e = eyeVec;
		Point3D w = u.add(e).normalize();
		if (u.dot(normal) > 0)
			return Math.pow(w.dot(normal), hardness);
		else
			return 0.0;
	}

	/**
	 * calculate the diffuse intensity
	 **/
	public static double diffuse(Point3D lightVec, Point3D normal) {
		double cosA = normal.dot(lightVec);
		if (cosA < 0)
			return 0.0;
		else
			return (cosA);
	}

}
