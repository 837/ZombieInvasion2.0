package ch.zombieInvasion.util;

public class Vector2D {
    public final double x;
    public final double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * add a Vector to this Vector, returns a new Vector
     */
    public Vector2D add(Vector2D v) {
        return new Vector2D(x + v.x, y + v.y);
    }

    /**
     * sub a Vector from this Vector, returns a new Vector r
     */
    public Vector2D sub(Vector2D v) {
        return new Vector2D(x - v.x, y - v.y);
    }

    /**
     * multiply this Vector with n, returns a new Vector
     */
    public Vector2D mult(double n) {
        return new Vector2D(x * n, y * n);
    }

    /**
     * divide this Vector with n, returns a new Vector
     */
    public Vector2D div(double n) {
        return new Vector2D(x / n, y / n);
    }

    /**
     * returns the magnitude of this Vector
     */
    public double mag() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * returns the magnitudeSquared of this Vector
     */
    public double magSq() {
        return (x * x + y * y);
    }

    /**
     * returns the distance between this Vector and the provided target
     */
    public double dist(Vector2D v) {
        double dx = x - v.x;
        double dy = y - v.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double dist(float[] center) {
        double dx = x - center[0];
        double dy = y - center[1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * normalizes this Vector, returns a new normalized Vector
     */
    public Vector2D normalize() {
        double m = mag();
        if (m != 0) {
            return div(m);
        }
        return new Vector2D();
    }

    /**
     * limits this vector, returns a new Vector
     */
    public Vector2D limit(double maxForce) {
        if (magSq() > maxForce * maxForce) {
            return this.normalize().mult(maxForce);
        }
        return this;
    }

    /**
     * returns the angle between this vector and the provided target vector
     */
    public double getTargetAngle(Vector2D target) {
        double dx = target.x - x;
        double dy = target.y - y;
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    /**
     * returns the dotProduct
     */
    public double dotP(Vector2D v) {
        return x * v.x + y * v.y;
    }

    /**
     * returns a new Vector perpendicular to this vector
     */
    public Vector2D perpCCW() {
        return new Vector2D(-y, x);
    }

    public float heading2D() {
        float angle = (float) Math.atan2(-y, x);
        return -1 * angle;
    }

    /**
     * returns a vector rotated CCW
     * 
     */
    public Vector2D rotate(double angle) {
        return new Vector2D((Math.cos(angle) * this.x) - (Math.sin(angle) * this.y), (Math.sin(angle) * this.x) + (Math.cos(angle) * this.y));
    }

    /**
     * returns a copy of this vector
     */
    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    // STATIC FUNCTIONS
    static public Vector2D add(Vector2D v1, Vector2D v2) {
        Vector2D v3 = new Vector2D(v1.x + v2.x, v1.y + v2.y);
        return v3;
    }

    static public Vector2D sub(Vector2D v1, Vector2D v2) {
        Vector2D v3 = new Vector2D(v1.x - v2.x, v1.y - v2.y);
        return v3;
    }

    static public Vector2D mult(Vector2D v1, Vector2D v2) {
        Vector2D v3 = new Vector2D(v1.x * v2.x, v1.y * v2.y);
        return v3;
    }

    static public Vector2D div(Vector2D v1, Vector2D v2) {
        Vector2D v3 = new Vector2D(v1.x / v2.x, v1.y / v2.y);
        return v3;
    }

    static public double dist(Vector2D v1, Vector2D v2) {
        double dx = v1.x - v2.x;
        double dy = v1.y - v2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * returns the angle between to vectors
     * 
     * A couple things to note here:
     * 
     * If two vectors (A-> and B-> ) are orthogonal (i.e. perpendicular), the dot
     * product (A-> *B-> ) is equal to 0.
     * 
     * If two vectors are unit vectors, then the dot product is simply equal to
     * cosine of the angle between them, i.e. A-> * B-> = cos() if A-> and B-> are
     * of length 1.
     */
    static public double angleBetween(Vector2D v1, Vector2D v2) {
        double dot = v1.dotP(v2);
        double theta = Math.acos(dot / (v1.mag() * v2.mag()));
        return theta;
    }

}
