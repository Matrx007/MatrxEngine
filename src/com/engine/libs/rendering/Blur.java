package com.engine.libs.rendering;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

public class Blur {

    public static BufferedImage blur(BufferedImage image, int[] filter, int filterSize) {
        if (filter.length % filterSize != 0) {
            throw new IllegalArgumentException("filter contains a incomplete row");
        }

        final int width = image.getWidth();
        final int height = image.getHeight();
        final int sum = IntStream.of(filter).sum();

        int[] input = image.getRGB(0, 0, width, height, null, 0, width);

        int[] output = new int[input.length];

        final int pixelIndexOffset = width - filterSize;
        final int centerOffsetX = filterSize / 2;
        final int centerOffsetY = filter.length / filterSize / 2;

        // apply filter
        for (int h = height - filter.length / filterSize + 1, w = width - filterSize + 1, y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int r = 0;
                int g = 0;
                int b = 0;
                for (int filterIndex = 0, pixelIndex = y * width + x;
                     filterIndex < filter.length;
                     pixelIndex += pixelIndexOffset) {
                    for (int fx = 0; fx < filterSize; fx++, pixelIndex++, filterIndex++) {
                        int col = input[pixelIndex];
                        int factor = filter[filterIndex];

                        // sum up color channels separately
                        r += ((col >>> 16) & 0xFF) * factor;
                        g += ((col >>> 8) & 0xFF) * factor;
                        b += (col & 0xFF) * factor;
                    }
                }
                r /= sum;
                g /= sum;
                b /= sum;
                // combine channels with full opacity
                output[x + centerOffsetX + (y + centerOffsetY) * width] = (r << 16) | (g << 8) | b | 0xFF000000;
            }
        }

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        result.setRGB(0, 0, width, height, output, 0, width);
        Graphics g = result.getGraphics();

        Area area = new Area(new Rectangle(0, 0, result.getWidth(), result.getHeight()));
        area.subtract(new Area(new Rectangle(filterSize, filterSize, result.getWidth()-filterSize*2,
                        result.getHeight()-filterSize*2)));

        g.setClip(area);
        g.drawImage(image, 0, 0, null);

        return result;
    }

    public static int[] createKernel(int size) {
        int[] kernel = new int[size*size];
        /*for(int xx = 0; xx < size; xx++) {
            for(int yy = size-1; yy > -1; yy++) {
                kernel[yy*size+xx] =
            }
        }*/

        Point2D center = new Point2D.Float(size/2, size/2);
        float radius = size/2;
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {Color.white, Color.black};
        RadialGradientPaint p =
                new RadialGradientPaint(center, radius, dist, colors);
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        g2.setPaint(p);
        g2.fillOval(0, 0, size, size);

        int[] matrix = new int[img.getWidth()*img.getHeight()];

        for(int xx = 0; xx < img.getWidth(); xx++) {
            for(int yy = 0; yy < img.getHeight(); yy++) {
                matrix[yy*img.getWidth()+xx] = (int)((double)(new Color(img.getRGB(xx, yy)).getBlue())/2);
            }
        }

        return matrix;
    }
}
