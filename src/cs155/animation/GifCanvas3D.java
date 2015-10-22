package cs155.animation;

import cs155.core.Canvas3D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.stream.*;


public class GifCanvas3D implements Canvas3D {

    private GifSequenceWriter writer;
    private final int FRAME_RATE = 24; // Frame rate
    private BufferedImage currentImage; // Current image to write


    /**
     * Creates a canvas for drawing to a png file
     * Without film, this canvas expects user to draw directly on the canvas via
     * the drawPixel() from the Canvas3D interface. To use with film, call other constructor.
     *
     * @param width  Width in pixels of the image
     * @param height Height in pixels of the image
     */
    public GifCanvas3D(String name, int width, int height) {
        this.currentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        File file = new File(name + ".gif");
        // grab the output image type from the first image in the sequence


        // create a new BufferedOutputStream with the last argument

        // create a gif sequence with the type of the first image, 1 second
        // between frames, which loops continuously
        try {
            ImageOutputStream output = new FileImageOutputStream(file);
            writer = new GifSequenceWriter(output, this.currentImage.getType(), 1000/FRAME_RATE, true);
        } catch (IOException e) {
            System.err.println("Failed to write Gif");
        }

    }

    @Override
    public int height() {
        return currentImage.getHeight();
    }

    @Override
    public int width() {
        return currentImage.getWidth();
    }

    @Override
    public void drawPixel(int i, int j, Color c) {
        currentImage.setRGB(i, j, c.getRGB());
    }

    @Override
    public void refresh(String name) {
        try {
            writer.writeToSequence(currentImage);

        } catch (IOException n) {
            System.err.println("A writing error had occured " + n.toString());
        }


    }

    /*
    * Closes resource
    */
    @Override
    public void done() {
        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to close resource");
        }
    }


}
