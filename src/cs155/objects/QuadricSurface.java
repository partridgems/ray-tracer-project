package cs155.objects;

import cs155.aesthetic.Material;
import cs155.aesthetic.TextureCoordinate;
import cs155.core.*;

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
        Material m = new Material(Color3D.BLACK, Color3D.BLACK, Color3D.WHITE.scale(0.5), Color3D.WHITE, 25);
        this.setInsideMat(m);
        this.setOutsideMat(m);
        this.minBounds = this.maxBounds = Optional.empty();
    }


    /**
     * Returns the RayHit describing a collision with the given ray and this quadric
     * surface.
     *
     * @param ray
     * @return
     */
    @Override
    public RayHit rayIntersectObj(Ray3D ray) {
        // Next check if ray hits this quadric
        Point3D hitPoint = findHitPoint(ray);
        if (hitPoint == null || !isWithinBounds(hitPoint)) {
            return RayHit.NO_HIT;
        }
        double distance = hitPoint.subtract(ray.getPoint()).length();
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
        double x = hitPoint.getX();
        double y = hitPoint.getY();
        double z = hitPoint.getZ();
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
        Point3D P = ray.getPoint(); // Ray starting point
        Point3D D = ray.getDirection(); // Ray direction
        // First coefficient of quadratic equation of t
        double A = a * sq(D.getX()) + b * sq(D.getY()) + c * sq(D.getZ())
                 + d * D.getY() * D.getZ() + e * D.getX() * D.getZ() + f * D.getX() * D.getY();
        // Second coefficient of quadratic equation of t
        double B = 2 * (a * P.getX() * D.getX() + b * P.getY() * D.getY() + c * P.getZ() * D.getZ())
                 + d * (P.getY() * D.getZ() + P.getZ() * D.getY())
                 + e * (P.getX() * D.getZ() + P.getZ() * D.getX())
                 + f * (P.getX() * D.getY() + P.getY() * D.getX())
                 + g * D.getX() + h * D.getY() + i * D.getZ();
        // Third coefficient of quadratic equation of t
        double C = a * sq(P.getX()) + b * sq(P.getY()) + c * sq(P.getZ()) + d * P.getY() * P.getZ()
                 + e * P.getX() * P.getZ() + f * P.getX() * P.getY() + g * P.getX() + h * P.getY() + i * P.getZ() + j;

        double discriminant = sq(B) - 4 * A * C;

        // Find intersection Point
        Point3D intersection = null;
        if (discriminant >= 0) {
            double t1 = (-B - Math.sqrt(discriminant)) / (2 * A);
            double t2 = (-B + Math.sqrt(discriminant)) / (2 * A);
            Point3D p1 = ray.atTime(t1);
            Point3D p2 = ray.atTime(t2);
            if (t1 > 0 && t2 > 0 && isWithinBounds(p1) && isWithinBounds(p2)) {
                intersection = t1 <= t2 ? p1 : p2;
            } else if (t1 > 0 && isWithinBounds(p1)) {
                intersection = p1;
            } else if (t2 > 0 && isWithinBounds(p2)) {
                intersection = p2;
            }
        }
        return intersection;
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
            if (point.getX() < min.getX() || point.getY() < min.getY() || point.getZ() < min.getZ()) {
                return false;
            }
        }
        // Check if point is below max bounds
        if (maxBounds.isPresent()) {
            Point3D max = maxBounds.get();
            if (point.getX() > max.getX() || point.getY() > max.getY() || point.getZ() > max.getZ()) {
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
