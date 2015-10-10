package cs155.jray;

/** 
 NewRayCanvas3D.java
 **/
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.BufferedImage;

/**
 * This class creates a panel where one can draw pixels
 * 
 * @author Timothy J. Hickey, tim@cs.brandeis.edu
 *         http://www.cs.brandeis.edu/~tim
 **/

public class NewRayCanvas3D extends Panel implements Canvas3D {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Scene3D scene;
	private BufferedImage buffer;
	private Graphics bufferg;
	private int width, height;
	private Dimension size;

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public NewRayCanvas3D(Scene3D scene, int w, int h) {
		super();
		this.scene = scene;
		width = (w > 0) ? w : 1;
		height = (h > 0) ? h : 1;
		size = new Dimension(w, h);
	}

	public Dimension preferredSize() {
		return size;
	}

	public void update2(Graphics g) {
		paint(g);
	} // don't let Java blank the screen

	/**
	 * this draws a pixel of a specified color on the offscreen buffer
	 **/
	public void drawPixel(int x, int y, Color c) {
		if (c == null)
			return;
		try{
			buffer.setRGB(x, y, c.getRGB());
		}catch (Exception e){
			System.out.println("Exception in drawPixel("+x+","+y+","+c+"):  "+e+" buffer="+buffer);
		}
	}


	/**
	 * This procedure is called when the panel needs to be redrawn .. It causes
	 * the scene to be drawn on the panel ...
	 **/
	public void paint(Graphics g) {
		// handle window resizes....
		if (buffer == null)
			buffer = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
		if (bufferg == null)
			bufferg = buffer.getGraphics();

		if (buffer != null) {
			copyFilmToGraphics(scene.camera.film);
			System.out.println("drawing image on buffer");
			g.drawImage(buffer, 0, 0, this); // copy offscreen buffer to screen
		} // close if

	}

	private void copyFilmToGraphics(Film film) {
		for (int i = 0; i < film.width(); i++) {
			for (int j = 0; j < film.height(); j++) {
				if (film.pixels[i][j] == null) {
					// System.out.println("cftg i= "+i+" j= "+j);
				} else {
					this.drawPixel(i, j, film.pixels[i][j]);
				}
			}
		}
	}

	public void refresh() {
		this.update(this.getGraphics());
	}

}
