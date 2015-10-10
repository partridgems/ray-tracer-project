package cs155.jray;

public class Triangle3D extends Object3D {
	private static TextureCoordinate tc00 = new TextureCoordinate(0, 0),
			tc01 = new TextureCoordinate(0, 1), tc10 = new TextureCoordinate(1,
					0);

	/**
	 * the three vertices in CCW order viewed from the outside
	 */
	public Point3D p0, p1, p2;
	/*
	 * the two edge vectors from p0->p1 and p0->p2
	 */
	private Point3D e1, e2;
	/**
	 * the normals for the three vertices for smooth shading
	 */
	public Point3D n0, n1, n2; // normals for the three vertices in smooth
								// shading
	/*
	 * the normal for the face for flat shading e1.cross(e2)
	 */
	private Point3D normal; // normal for the face in flat shading
	/**
	 * the texture coordinates for p0,p1,p2
	 */
	public TextureCoordinate tc0, tc1, tc2; // tc for the three vertices

	/**
	 * create a triangle given three vertices and provide default values for the
	 * normals and texture coordinates of the three vertices
	 * 
	 * @param p0
	 * @param p1
	 * @param p2
	 */
	public Triangle3D(Point3D p0, Point3D p1, Point3D p2) {
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		e1 = p1.subtract(p0);
		e2 = p2.subtract(p0);
		normal = e1.cross(e2).normalize();
		// System.out.println("e1=" + e1 + " e2=" + e2 + " n=" + normal);
		n0 = n1 = n2 = normal;
		tc0 = tc00;
		tc1 = tc10;
		tc2 = tc01;

	}

	/**
	 * intersect the ray with the triangle and generate the texture coordinates
	 * and normal by interpolation using the Barycentric coordinates
	 */
	@Override
	public RayHit rayIntersect(Ray3D ray) {
		Point3D s = ray.p.subtract(p0);
		Point3D s1 = ray.d.cross(e2);
		Point3D s2 = s.cross(e1);
		double s1e1 = s1.dot(e1);
		double s2e2 = s2.dot(e2);
		double s1s = s1.dot(s);
		double s2d = s2.dot(ray.d);
		if (s1e1 == 0)
			return RayHit.NO_HIT;
		double t = s2e2 / s1e1;
		if (t < 0)
			return RayHit.NO_HIT;
		double b1 = s1s / s1e1;
		double b2 = s2d / s1e1;
		Point3D ns0 = n0.scale(1 - b1 - b2);
		Point3D ns1 = n1.scale(b1);
		Point3D ns2 = n2.scale(b2);
		Point3D n = ((ns0.add(ns1)).add(ns2)).normalize();
		// Point3D nt0 = ns0.add(ns1);
		// Point3D nt1 = nt0.add(ns2);
		// Point3D n = nt1.normalize();
		Point3D hitPoint = ray.p.add(ray.d.scale(t));

		TextureCoordinate tc = TextureCoordinate.interpolate(b1, b2, tc0, tc1,
				tc2);
		// also average the texture coordinates ....

		RayHit rh = new RayHit(hitPoint, t, n, this, tc);

		if ((b1 >= 0) && (b2 >= 0) && (b1 + b2 <= 1))
			return rh;
		else
			return RayHit.NO_HIT;
	}

	public String toString() {
		return "t(" + p0 + "||" + p1 + "||" + p2 + ")";
	}

	public static void main(String[] args) {
		System.out.println("testing");
		Point3D p0 = new Point3D(0, 0, 0);
		Point3D p1 = new Point3D(0, 0, 1);
		Point3D p2 = new Point3D(1, 0, 0);
		Point3D n0 = new Point3D(-1, 0, -1).normalize();
		Point3D n1 = new Point3D(-1, 0, 2).normalize();
		Point3D n2 = new Point3D(2, 0, -1).normalize();
		Point3D p = new Point3D(0.04, 1, 0.95);
		//Point3D y = new Point3D(0, 1, 0);
		Point3D d = new Point3D(0, -1, 0);
		Triangle3D t1 = new Triangle3D(p0, p1, p2);
		t1.n0 = n0;
		t1.n1 = n1;
		t1.n2 = n2;
		Ray3D r = new Ray3D(p, d);
		RayHit rh = t1.rayIntersect(r);
		System.out.println(rh);
		Scene3D s = new Scene3D();
		Light3D l1 = new Light3D(p.add(p2));
		// Plane3D pl1 = new Plane3D(p0, y);
		s.add(t1);
		s.add(l1);
		// s.add(pl1);
		Color3D c = RayTracer3D.computeColor(r, s, 0);
		System.out.println("c=" + c);

	}

}
