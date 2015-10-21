package cs155.objects;

import cs155.aesthetic.Material;
import cs155.aesthetic.TextureCoordinate;
import cs155.core.Object3D;
import cs155.core.Point3D;
import cs155.core.Ray3D;
import cs155.core.RayHit;

/**
 * class represents a 3D sphere
 */

public class Sphere3D extends Object3D {
	private Point3D center;
	private double radius = 0.0;

	public Sphere3D(Point3D center, double radius, Material m) {
		super();
		this.setCenter(center);
		this.setRadius(radius);
		this.setInsideMat(m);
		this.setOutsideMat(m);
	}

	public Sphere3D(Point3D center, double radius) {
		this(center, radius, Material.DEFAULT_MAT);
	}

	public Sphere3D(double centerX, double centerY, double centerZ,
			double radius) {
		this(new Point3D(centerX, centerY, centerZ), radius);
	}

	@Override
	protected RayHit rayIntersectObj(Ray3D r) {
		Point3D P = r.getPoint(), D = r.getDirection();
		Point3D PC = P.subtract(getCenter());
		double t0 = -1, A = D.dot(D), B = 2 * D.dot(PC), C = PC.dot(PC)
				- getRadius() * getRadius();
		double Discr = (B * B - 4 * A * C);
		// if (Math.abs(Discr)<0.00001) Discr=0;
		boolean has_solution = (Discr >= 0);
		// System.out.println("A="+A+" B="+B+" C+"+C+" D="+Discr);
		if (!(has_solution))
			return RayHit.NO_HIT;

		double SqrtDisc = Math.sqrt(Discr);
		double t1 = (-B - SqrtDisc) / (2 * A), t2 = (-B + SqrtDisc) / (2 * A);
		// note t1<=t2 always
		if (t1 >= getEpsilon())
			t0 = t1; // ray hits the outside of the sphere
		else if (t2 >= getEpsilon())
			t0 = t2; // ray starts inside the sphere
		else
			return RayHit.NO_HIT; // sphere is behind the ray!

		Point3D p0 = r.atTime(t0);
		Point3D n0 = p0.subtract(getCenter()).normalize();

		// calculate the texture coordinate!
		TextureCoordinate tc = new TextureCoordinate(n0.getY(), Math.atan2(n0.getZ(),
                n0.getX()));
		return new RayHit(p0, t0, n0, this, tc);
	}

	public String toString() {
		return "sphere(" + this.getCenter() + "," + this.getRadius() + ")";
	}

    public Point3D getCenter() {
        return center;
    }

    public void setCenter(Point3D center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
