package cs155.jray;

/**
 * this class represents a 3D cylinder with open ends
 */

public class Cylinder3D extends Object3D {
	public Point3D center;
	public Point3D direction;

	public double radius = 0.0;
	public double height = 0.0;

	public Cylinder3D(Point3D center, Point3D direction, double radius,
			double height) {
		this(center, direction, radius, height, Material.defaultMat);
	}

	public Cylinder3D(Point3D center, Point3D direction, double radius,
			double height, Material m) {
		this.center = center;
		this.direction = direction.normalize();
		this.radius = radius;
		this.height = height;
		this.insideMat = this.outsideMat = m;
	}

	public RayHit rayIntersect(Ray3D r) {
		Point3D P = r.p, D = r.d;
		Point3D PC = P.subtract(center);
		Point3D a = D.subtract(direction.scale(direction.dot(D)));
		Point3D b = PC.subtract(direction.scale(direction.dot(PC)));
		double A = a.dot(a), B = 2 * a.dot(b), C = b.dot(b) - radius * radius;
		double Discr = (B * B - 4 * A * C);
		boolean has_solution = (Discr >= 0);
		if (!(has_solution))
			return RayHit.NO_HIT;
		double SqrtDisc = Math.sqrt(Discr);
		double t1 = (-B - SqrtDisc) / (2 * A), t2 = (-B + SqrtDisc) / (2 * A);
		// note t1<=t2 always

		Point3D p, V2;
		double h;
		if (t1 >= epsilon) {
			p = r.atTime(t1);
			V2 = p.subtract(center);
			h = direction.dot(V2);
			if ((h >= 0) && (h <= height))
				return new RayHit(p, t1, normal(p), this,
						getTextureCoordinate(V2));
		}
		if (t2 >= epsilon) {
			p = r.atTime(t2);
			V2 = p.subtract(center);
			h = direction.dot(V2);

			if ((h >= 0) && (h <= height))
				return new RayHit(p, t2, normal(p), this,
						getTextureCoordinate(V2));
		}

		return RayHit.NO_HIT;
	}

	/**
	 * calculate the texture coordinate for the point p on the cylinder. the y
	 * texture coordinate should be the height above the base the x texture
	 * coordinate should be the radians when you project the x and z coordinates
	 * of p into the y=0 plane. This won't work if the normal is parallel to the
	 * y=0 plane!!
	 * 
	 * @param p
	 * @return
	 */
	private TextureCoordinate getTextureCoordinate(Point3D p) {
		double y = p.dot(direction);
		double x = Math.atan2(p.z, p.x);
		TextureCoordinate tc = new TextureCoordinate(x, y);
		return tc;
	}

	private Point3D normal(Point3D p) {
		Point3D p1 = p.subtract(center);
		Point3D q = p1.subtract(direction.scale(direction.dot(p1))).normalize();
		return q;
	}

}
