package cs155.jray;

/**
 * Represents an arbitrary quartic surface. Techniques for finding ray intersection and normal
 * are borrowed from http://euclid.nmu.edu/~bpeterso/CS446-Handouts/Notes/CS446_Note_7.pdf
 *
 * Created by kahliloppenheimer on 10/15/15.
 */
public class QuarticSurface extends Object3D {
    // coefficients of a general Quartic surface given the general form:
    // ax2 + by2 + cz2 + dyz + exz + fxy + gx + hy + iz + j = 0
    private final double a, b, c, d, e, f, g, h, i, j;

    public QuarticSurface(double a, double b, double c, double d, double e, double f, double g, double h, double i, double j) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
        this.j = j;
        this.insideMat = this.outsideMat = Material.defaultMat;
    }

    @Override
    public RayHit rayIntersect(Ray3D ray) {
        Point3D hitPoint = findHitPoint(ray);
        if(hitPoint == null) {
            return RayHit.NO_HIT;
        }
        double distance = hitPoint.subtract(ray.p).length();
        Point3D normal = findNormalAtPoint(hitPoint);
        // normal should face away from eye
        normal = ray.d.dot(normal) < 0 ? normal : normal.scale(-1);
        normal = new Point3D(0, 0, 5);
        return new RayHit(hitPoint, distance, normal, this, new TextureCoordinate(0, 0));
    }

    /**
     * Returns the vector normal to the surface at a give point
     *
     * @param hitPoint
     * @return
     */
    private Point3D findNormalAtPoint(Point3D hitPoint) {
        double x = hitPoint.x;
        double y = hitPoint.y;
        double z = hitPoint.z;
        double normalX = 2 * a * x + e * z + f * y + g;
        double normalY = 2 * b * y + d * z + f * x + h;
        double normalZ = 2 * c * z + d * y + e * x + i;
        return new Point3D(normalX, normalY, normalZ).normalize();
    }

    /**
     * Returns the point at which the passed ray intersects this surface
     *
     * @param ray
     * @return
     */
    private Point3D findHitPoint(Ray3D ray) {
        // We plug paramaterization of x, y, z for ray into our general quartic equation
        // to get standard form: At^2 + Bt + C = 0, then use the quadratic equation to solve for t.
        // The coefficients A, B, and C are quite ugly, and the derivation is described in the linked
        // resource
        Point3D P = ray.p; // Ray starting point
        Point3D D = ray.d; // Ray direction
        // First coefficient of quadratic equation of t
        double A = a * sq(D.x) + b * sq(D.y) + c * sq(D.z)
                 + d * D.y * D.z + e * D.x * D.z + f * D.x * D.y;
        // Second coefficient of quadratic equation of t
        double B = 2 * (a * P.x * D.x + b * P.y * D.y + c * P.z * D.z)
                 + d * (P.y * D.z + P.z * D.y)
                 + e * (P.x * D.z + P.z * D.x)
                 + f * (P.x * D.y + P.y * D.x)
                 + g * D.x + h * D.y + i * D.z;
        // Third coefficient of quadratic equation of t
        double C = a * sq(P.x) + b * sq(P.y) + c * sq(P.z) + d * P.y * P.z
                 + e * P.x * P.z + f * P.x * P.y + g * P.x + h * P.y + i * P.z + j;

        double discriminant = sq(B) - 4 * A * C;

        // Find intersection time
        double time = -1;
        if (discriminant >= 0) {
            double t1 = (-b - Math.sqrt(discriminant)) / (2 * a);
            double t2 = (-b + Math.sqrt(discriminant)) / (2 * a);
            if (t1 > 0 && t2 > 0) {
                time = Math.min(t1, t2);
            } else if (t1 > 0) {
                time = t1;
            } else if (t2 > 0) {
                time = t2;
            }
        }

        // Return point if time is positive
        if (time > 0) {
            return new Point3D(P.x + time * D.x, P.y + time * D.y, P.z + time * D.z);
        }
        return null;
    }

    /**
     * Returns x^2. Used to reduce verbosity of expressions
     *
     * @param x
     * @return
     */
    private static double sq (double x) {
        return Math.pow(x, 2);
    }
}
