package cs155.objects;

import cs155.aesthetic.Material;
import cs155.aesthetic.TextureCoordinate;
import cs155.core.Object3D;
import cs155.core.Point3D;
import cs155.core.Ray3D;
import cs155.core.RayHit;

/**
 * class represents a 3D plane
 */

public class Plane3D extends Object3D {
	private Point3D center;
    private Point3D normal;
	private Point3D up = new Point3D(0d, 1d, 0d);
	private Point3D right = new Point3D(1d, 0d, 0d);

	public Plane3D(Point3D center, Point3D normal, Material m) {
		super();
		this.center = center;
		this.normal = normal.normalize();
		this.setInsideMat(m);
		this.setOutsideMat(m);
		Point3D tmp = project(up);
		if (Math.abs(tmp.dot(tmp)) > 0.1) {
			up = up.normalize();
			right = up.cross(normal);
		} else {
			right = project(right).normalize();
			up = right.cross(normal);
		}

	}

	public Plane3D(Point3D center, Point3D normal) {
		this(center, normal, Material.DEFAULT_MAT);
	}

	private Point3D project(Point3D q) {
		return q.subtract(normal.scale(normal.dot(q)));
	}

	public RayHit rayIntersectObj(Ray3D r) {
		Point3D P = r.getPoint(), D = r.getDirection();
		Point3D PC = P.subtract(center);
		double dn = normal.dot(D), cpn = normal.dot(PC) /*, t*/;
		if (dn == 0.0)
			return RayHit.NO_HIT;
		double t0 = -cpn / dn;
		if (t0 < getEpsilon())
			return RayHit.NO_HIT;
		else {
			Point3D q = r.atTime(t0);
			Point3D q0 = q; //project(q);
			double x = q0.dot(this.right);
			double y = q0.dot(this.up);
			// calculate the texture coordinates (x,y) using this.right and
			// this.up as the basis

			// put in some lines of code here for PA05 ..
			//Point3D PQ = P.subtract(q);
			//double u = PQ.dot(up);
			//double v = PQ.dot(right);
			TextureCoordinate tc = new TextureCoordinate(x,y); // change this!!
			return new RayHit(q, t0, normal, this, tc);
		}
	}

    public Point3D getCenter() {
        return center;
    }

    public void setCenter(Point3D center) {
        this.center = center;
    }

    public Point3D getNormal() {
        return normal;
    }

    public void setNormal(Point3D normal) {
        this.normal = normal;
    }
}
