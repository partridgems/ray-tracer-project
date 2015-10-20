package cs155.jray;

/**
 * class represents a 3D sphere
 */

public class Sphere3D extends Object3D {
	public Point3D center;
	public double radius = 0.0;

	public Sphere3D(Point3D center, double radius, Material m) {
		super();
		this.center = center;
		this.radius = radius;
		this.insideMat = this.outsideMat = m;
	}

	public Sphere3D(Point3D center, double radius) {
		this(center, radius, Material.defaultMat);
	}

	public Sphere3D(double centerX, double centerY, double centerZ,
			double radius) {
		this(new Point3D(centerX, centerY, centerZ), radius);
	}

	@Override
	protected RayHit rayIntersectObj(Ray3D r) {
		Point3D P = r.p, D = r.d;
		Point3D PC = P.subtract(center);
		double t0 = -1, A = D.dot(D), B = 2 * D.dot(PC), C = PC.dot(PC)
				- radius * radius;
		double Discr = (B * B - 4 * A * C);
		// if (Math.abs(Discr)<0.00001) Discr=0;
		boolean has_solution = (Discr >= 0);
		// System.out.println("A="+A+" B="+B+" C+"+C+" D="+Discr);
		if (!(has_solution))
			return RayHit.NO_HIT;

		double SqrtDisc = Math.sqrt(Discr);
		double t1 = (-B - SqrtDisc) / (2 * A), t2 = (-B + SqrtDisc) / (2 * A);
		// note t1<=t2 always
		if (t1 >= epsilon)
			t0 = t1; // ray hits the outside of the sphere
		else if (t2 >= epsilon)
			t0 = t2; // ray starts inside the sphere
		else
			return RayHit.NO_HIT; // sphere is behind the ray!

		Point3D p0 = r.atTime(t0);
		Point3D n0 = p0.subtract(center).normalize();

		// calculate the texture coordinate!
		TextureCoordinate tc = new TextureCoordinate(n0.y, Math.atan2(n0.z,
				n0.x));
		return new RayHit(p0, t0, n0, this, tc);
	}

	public String toString() {
		return "sphere(" + this.center + "," + this.radius + ")";
	}

}
