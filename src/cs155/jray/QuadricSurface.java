package cs155.jray;

import java.util.Optional;

/**
 * Represents an arbitrary quartic surface. Techniques for finding ray intersection and normal
 * are borrowed from http://euclid.nmu.edu/~bpeterso/CS446-Handouts/Notes/CS446_Note_7.pdf
 *
 * Created by kahliloppenheimer on 10/15/15.
 */
public class QuadricSurface extends Object3D {
    // coefficients of a general Quartic surface given the general form:
    // ax2 + by2 + cz2 + dyz + exz + fxy + gx + hy + iz + j = 0
    private final double a, b, c, d, e, f, g, h, i, j;
    // Optional min/max bounds for surface
    private Optional<Point3D> minBounds;
    private Optional<Point3D> maxBounds;

    /**
     * Used for unit-testing / debugging
     * @param args
     */
    public static void main(String[] args) {
        Scene3D scene = DemoScene8.initScene2();
        for (int i = 0; i < scene.camera.film.width(); i+=1) {
            for (int j = 0; j < scene.camera.film.height(); j++) {
                Ray3D r1 = scene.camera.generateRay(i, j); // generate a ray
                // Expected sphere
                QuadricSurface expected = new QuadricSurface(1, 1, 1, 0, 0, 0, 0, 0, 0, -1);
                // Observed sphere
                Sphere3D actual = new Sphere3D(new Point3D(0, 0, 0), 1.0, Material.defaultMat);
                if (r1 == Ray3D.NOT_IN_SCENE) {  // This pixel is outside of the lens we are using (used in FishEye)

                } else {
                    RayHit rh = actual.rayIntersect(r1);
                    if (rh != RayHit.NO_HIT) {
                        RayHit rh2 = expected.rayIntersect(r1);
                        System.out.println("observed = " + rh);
                        System.out.println("expected = " + expected.rayIntersect(r1));
                        System.out.println();
                    }
                }
            }
        } // End of painting points
    }

    public QuadricSurface(double a, double b, double c, double d, double e, double f, double g, double h, double i, double j) {
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
        this.insideMat = this.outsideMat = new Material(Color3D.BLACK, Color3D.BLACK, 
        		Color3D.WHITE.scale(0.5), Color3D.WHITE, 25);
        this.minBounds = this.maxBounds = Optional.empty();
    }


    @Override
    public RayHit rayIntersect(Ray3D ray) {
        // Next check if ray hits this quadric
        Point3D hitPoint = findHitPoint(ray);
        if (hitPoint == null || !isWithinBounds(hitPoint)) {
            return RayHit.NO_HIT;
        }
        double distance = hitPoint.subtract(ray.p).length();
        Point3D normal = findNormalAtPoint(hitPoint);
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
            double t1 = (-B - Math.sqrt(discriminant)) / (2 * A);
            double t2 = (-B + Math.sqrt(discriminant)) / (2 * A);
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
     * Sets the min x, y, z bounds of this surface to be each of the values of minBounds and
     * the max x, y, z bounds of this surface to be each of the values of maxBounds
     *
     * @param minBounds
     * @param maxBounds
     */
    public void setBounds(Point3D minBounds, Point3D maxBounds) {
        this.minBounds = Optional.of(minBounds);
        this.maxBounds = Optional.of(maxBounds);
    }

    /**
     * Returns true iff the given point is within the current min/max bounds of this surface
     *
     * @param point
     * @return
     */
    private boolean isWithinBounds(Point3D point) {
        // Check if point is above min bounds
        if (minBounds.isPresent()) {
            Point3D min = minBounds.get();
            if (point.x < min.x || point.y < min.y || point.z < min.z) {
                return false;
            }
        }
        // Check if point is below max bounds
        if (maxBounds.isPresent()) {
            Point3D max = maxBounds.get();
            if (point.x > max.x || point.y > max.y || point.z > max.z) {
                return false;
            }
        }
        return true;
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
