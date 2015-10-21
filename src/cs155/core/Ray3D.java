package cs155.core;

/** A Ray3D consists of a point and a (normalized) direction. **/

public class Ray3D {
	
	public static final Ray3D NOT_IN_SCENE = new Ray3D(null, null);
	public Point3D p, d;

	/**
	 * This represents a 3D ray with a specified origin point p and direction d.
	 * The direction of a ray is a normalized vector.
	 **/

	public Ray3D(Point3D p, Point3D d) {
		this.p = p;
		this.d = d;
		if (this.d != null) {
			this.d.w = 0;
			this.d = d.normalize();
		}
	}

	public Ray3D(Ray3D x) {
		this(x.p, x.d);
	}

	/**
	 * This returns the point along the ray t units from its origin p
	 **/

	public Point3D atTime(double t) {
		return new Point3D((p.x + t * d.x), p.y + t * d.y, p.z + t * d.z);
	}

	public String toString() {
		return "ray(" + this.p + "," + this.d + ")";
	}

	public Ray3D applyTransform(Transform3D tr) {
		Point3D p1 = tr.applyTo(this.p);
		Point3D p2 = tr.applyTo(this.p.add(this.d));
		return new Ray3D(p1, p2.subtract(p1));
	}

}
