package me.urielsalis.mazesolver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

/**
 * Sapienchat 2017(c)
 *
 * @author Uriel Salischiker
 * @package me.urielsalis.mazesolver
 */
public class Main {
    static int width;
    static int height;

    public static void main(String[] args) {
        if(args.length == 0) {
            System.err.println("Pass a image!");
            System.exit(1);
        }
        try {
            boolean[][] image = readImage(ImageIO.read(new File(args[0])));
            Maze maze = new Maze(image, width, height);
        } catch (IOException e) {
            System.err.println("Pass a image!");
            System.exit(1);
        }
    }

    private static boolean[][] readImage(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        width = image.getWidth();
        height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        boolean[][] result = new boolean[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int y = 0;
                /*
                Y = 0.2126*R + 0.7152*G + 0.0722*B
                if(y < 128) black else white
                */
                //y += (((int) pixels[pixel] & 0xff) << 24); // alpha
                y += ((int) pixels[pixel + 1] & 0xff)*0.0722; // blue
                y += (((int) pixels[pixel + 2] & 0xff) << 8)*0.7152; // green
                y += (((int) pixels[pixel + 3] & 0xff) << 16)*0.2126; // red
                result[row][col] = y >= 128;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int y = 0;
                //argb += -16777216; // 255 alpha
                y += ((int) pixels[pixel] & 0xff)*0.0722; // blue
                y += (((int) pixels[pixel + 1] & 0xff) << 8)*0.7152; // green
                y += (((int) pixels[pixel + 2] & 0xff) << 16)*0.2126; // red
                result[row][col] = y >= 128;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

}
