package cs155.jray;

/**
 * this class represented 3d affine transforms
 * 
 * @author tim
 * 
 */

public class Transform3D {
	public final static Transform3D IDENTITY = new Transform3D();

	/* the transform is represented by this 4x4 matrix */
	private double tr[][] = new double[4][4];
	private double inv[][] = new double[4][4];

	/**
	 * create the default transform, which is the identity transform
	 */
	public Transform3D() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.inv[i][j] = this.tr[i][j] = (i == j) ? 1 : 0;
			}
		}
	}

	/**
	 * create a translation transform
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static Transform3D translation(double x, double y, double z) {
		Transform3D t = new Transform3D();
		t.tr[0][3] = x;
		t.tr[1][3] = y;
		t.tr[2][3] = z;
		t.inv[0][3] = -x;
		t.inv[1][3] = -y;
		t.inv[2][3] = -z;
		
		return t;
	}

	/**
	 * generate the Transform3D corresponding to translation by a point p
	 */
	public static Transform3D translation(Point3D p) {
		return translation(p.x, p.y, p.z);
	}
	
	public static Transform3D scaleTransf(double x, double y, double z) {
		Transform3D t = new Transform3D();
		t.tr[0][0] = x;
		t.tr[1][1] = y;
		t.tr[2][2] = z;
		
		t.inv[0][0] = 1/x;
		t.inv[1][1] = 1/y;
		t.inv[2][2] = 1/z;
		
		return t;
	}

	/**
	 * generate the Transform3D corresponding to rotation theta degrees around
	 * the x-axis
	 * 
	 * @param theta
	 * @return
	 */
	public static Transform3D rotationZ(double theta) {
		Transform3D t = new Transform3D();
		theta = theta / 180d * Math.PI;
		double c = Math.cos(theta);
		double s = Math.sin(theta);
		t.tr[0][0] = c;
		t.tr[0][1] = s;
		t.tr[1][0] = -s;
		t.tr[1][1] = c;
		
		t.inv[0][0] = c;
		t.inv[0][1] = -s;
		t.inv[1][0] = s;
		t.inv[1][1] = c;
		return t;
	}

	/**
	 * generate the Transform3D corresponding to rotation theta degrees around
	 * the y-axis
	 * 
	 * @param theta
	 * @return
	 */
	public static Transform3D rotationY(double theta) {
		Transform3D t = new Transform3D();
		theta = theta / 180d * Math.PI;
		double c = Math.cos(theta);
		double s = Math.sin(theta);
		t.tr[0][0] = c;
		t.tr[0][2] = -s;
		t.tr[2][0] = s;
		t.tr[2][2] = c;
		
		t.inv[0][0] = c;
		t.inv[0][2] = s;
		t.inv[2][0] = -s;
		t.inv[2][2] = c;
		return t;
	}

	/**
	 * generate the Transform3D corresponding to rotation theta degrees around
	 * the z-axis
	 * 
	 * @param theta
	 * @return
	 */
	public static Transform3D rotationX(double theta) {
		Transform3D t = new Transform3D();
		theta = theta / 180d * Math.PI;
		double c = Math.cos(theta);
		double s = Math.sin(theta);
		t.tr[1][1] = c;
		t.tr[1][2] = s;
		t.tr[2][1] = -s;
		t.tr[2][2] = c;
		
		t.inv[1][1] = c;
		t.inv[1][2] = -s;
		t.inv[2][1] = s;
		t.inv[2][2] = c;
		return t;
	}

	/**
	 * Compose the two transformations by multiplying their correspond matrices
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Transform3D compose(Transform3D a, Transform3D b) {
		Transform3D t = new Transform3D();
		t.tr = multiply(a.tr, b.tr);
		t.inv = multiply(b.inv,a.inv);
		return t;
	}
	
	public Transform3D inverse(){
		Transform3D b = new Transform3D();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				b.inv[i][j] = this.tr[i][j];
				b.tr[i][j]  = this.inv[i][j];
			}
		}
		return b;
	}
	
	public Transform3D transpose(){
		Transform3D b = new Transform3D();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				b.inv[i][j] = this.inv[j][i];
				b.tr[i][j]  = this.tr[j][i];
			}
		}
		return b;
	}

	/**
	 * multiply the two 4x4 matrices together and return their product
	 */
	private static double[][] multiply(double[][] a, double[][] b) {
		double[][] c = new double[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				double sum = 0;
				for (int k = 0; k < 4; k++) {
					sum += a[i][k] * b[k][j];
				}
				c[i][j] = sum;
			}
		}
		return c;
	}

	/* ******************************
	 * Below are the instance methods ...
	 */

	/**
	 * apply this transform to a point
	 * 
	 * @param p
	 * @return
	 */
	public Point3D applyTo(Point3D p) {
		double[] vals = new double[4];
		for (int i = 0; i < 4; i++) {
			vals[i] = tr[i][0] * p.x + tr[i][1] * p.y + tr[i][2] * p.z
					+ tr[i][3] * p.w;
		}
		return new Point3D(vals[0], vals[1], vals[2]);
	}
	
	public Point3D applyInvTranspTo(Point3D p) {
		double[] vals = new double[4];
		for (int i = 0; i < 4; i++) {
			vals[i] = inv[0][i] * p.x + inv[1][i] * p.y + inv[2][i] * p.z
					+ inv[3][i] * p.w;
		}
		return new Point3D(vals[0], vals[1], vals[2]);
	}

	/**
	 * return the transform obtained by composing with a translation by a point
	 * p
	 * 
	 * @param p
	 * @return
	 */
	public Transform3D translate(Point3D p) {
		return (compose(this, Transform3D.translation(p)));
	}

	/**
	 * return the transform obtained by composing with a translation by a
	 * (x,y,z)
	 * 
	 * @param p
	 * @return
	 */
	public Transform3D translate(double x, double y, double z) {
		return (compose(this, Transform3D.translation(x, y, z)));
	}
	
	public Transform3D scale(double x, double y, double z) {
		return (compose(this, Transform3D.scaleTransf(x,y,z)));
	}

	/**
	 * return the transform obtained by composing with a x-rotation of d degrees
	 * 
	 * @param d
	 * @return
	 */
	public Transform3D rotateX(double d) {
		return (compose(this, Transform3D.rotationX(d)));
	}

	/**
	 * return the transform obtained by composing with a y-rotation of d degrees
	 * 
	 * @param d
	 * @return
	 */
	public Transform3D rotateY(double d) {
		return (compose(this, Transform3D.rotationY(d)));
	}

	/**
	 * return the transform obtained by composing with a z-rotation of d degrees
	 * 
	 * @param d
	 * @return
	 */
	public Transform3D rotateZ(double d) {
		return (compose(this, Transform3D.rotationZ(d)));
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < 4; i++) {
			s += "(";
			for (int j = 0; j < 4; j++) {
				s += " " + tr[i][j] + " ";
			}
			s += ")\n";
		}
		return s;
	}

	/**
	 * this calls the runUnitTests method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		runUnitTests();
	}

	/**
	 * these should print out the various kinds of matrices so you can see that
	 * they have the right form! Also, test the matrix multiplication...
	 */
	private static void runUnitTests() {
		System.out.println("id = \n" + Transform3D.IDENTITY);
		System.out.println("tr(1,2,3) = \n"
				+ Transform3D.translation(1d, 2d, 3d));
		System.out.println("rotX(45) = \n" + Transform3D.rotationX(45d));
	}

}
