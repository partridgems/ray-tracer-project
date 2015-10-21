package cs155.core;

/** A Ray3D consists of a point and a (normalized) direction. **/

public class Ray3D {
	
	public static final Ray3D NOT_IN_SCENE = new Ray3D(null, null);
	private Point3D point;
	private Point3D direction;

	/**
	 * This represents a 3D ray with a specified origin point p and direction d.
	 * The direction of a ray is a normalized vector.
	 **/

	public Ray3D(Point3D p, Point3D d) {
		this.point = p;
		this.direction = d;
		if (this.direction != null) {
			this.direction.setW(0);
			this.direction = d.normalize();
		}
	}

	public Ray3D(Ray3D x) {
		this(x.point, x.direction);
	}

	/**
	 * This returns the point along the ray t units from its origin p
	 **/

	public Point3D atTime(double t) {
		return new Point3D((point.getX() + t * direction.getX()), point.getY() + t * direction.getY(), point.getZ() + t * direction.getZ());
	}

	public String toString() {
		return "ray(" + this.point + "," + this.direction + ")";
	}

	public Ray3D applyTransform(Transform3D tr) {
		Point3D p1 = tr.applyTo(this.point);
		Point3D p2 = tr.applyTo(this.point.add(this.direction));
		return new Ray3D(p1, p2.subtract(p1));
	}

    public Point3D getPoint() {
        return point;
    }

    public void setPoint(Point3D point) {
        this.point = point;
    }

    public Point3D getDirection() {
        return direction;
    }

    public void setDirection(Point3D direction) {
        this.direction = direction;
    }
}
