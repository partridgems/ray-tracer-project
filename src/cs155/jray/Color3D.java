package cs155.jray;

import java.awt.Color;

/**
 * This represents color objects, where the red, green, and blue components are
 * non-negative doubles. When converting to a Color with multiply by 255 and
 * coerce to integers in the range [0,255]. This class also has methods to add,
 * multiply, and scale colors.
 **/

public class Color3D {
	/**
	 * here we define some constant values
	 */
	public static final Color3D WHITE = new Color3D(1d, 1d, 1d),
			BLACK = new Color3D(0d, 0d, 0d);

	public double red = 1.0, green = 1.0, blue = 1.0;

	/**
	 * create a color from its red green and blue components (assumed to be
	 * between 0 and 1)
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public Color3D(double r, double g, double b) {
		red = r;
		green = g;
		blue = b;
	}

	public Color3D(Color c) {
		red = c.getRed() / 255d;
		green = c.getGreen() / 255d;
		blue = c.getBlue() / 255d;
	}

	/**
	 * toColor will convert the red, green, and blue values of a Color3D object
	 * into integers between 0 and 255 (clamping the values if necessary)
	 * 
	 * @return
	 */
	public Color toColor() {
		int r = (int) (red * 255), g = (int) (green * 255), b = (int) (blue * 255);
		return new Color(clamp(r), clamp(g), clamp(b));
	}

	private int clamp(int n) {
		if (n < 0)
			return 0;
		if (n > 255)
			return 255;
		return n;
	}

	/**
	 * add a color to this color and return the new color
	 * 
	 * @param c
	 * @return
	 */
	public Color3D add(Color3D c) {
		return new Color3D(red + c.red, green + c.green, blue + c.blue);
	}

	/**
	 * multiply a color, c, by this color and return the resulting color
	 * 
	 * @param c
	 * @return
	 */
	public Color3D times(Color3D c) {
		return new Color3D(red * c.red, green * c.green, blue * c.blue);
	}

	/**
	 * scale this color by d and return the newly created color
	 * 
	 * @param d
	 * @return
	 */
	public Color3D scale(double d) {
		return new Color3D(d * red, d * green, d * blue);
	}

	public static Color3D weightedAverage(Color3D a, Color3D b, double wa,
			double wb) {
		return a.scale(wa).add(b.scale(wb)).scale(1 / (wa + wb));

	}

	public Color3D averageIn(Color3D b, double w) {
		return this.scale(1 - w).add(b.scale(w));
	}

	public String toString() {
		return "c3d(" + red + "," + green + "," + blue + ")";
	}

}
