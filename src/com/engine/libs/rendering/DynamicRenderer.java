package com.engine.libs.rendering;

import com.engine.Core;
import com.engine.libs.font.Alignment;
import com.engine.libs.rendering.surface.Surface;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DynamicRenderer extends Renderer {
    private int unitW, unitH;

    public DynamicRenderer(Core Core, int columns, int rows) {
        super(Core);
        unitW = Core.getWidth()/columns;
        unitH = Core.getHeight()/rows;
    }

    public DynamicRenderer(Surface surf, int columns, int rows) {
        super(surf);
        unitW = surf.getW()/columns;
        unitH = surf.getH()/rows;
    }

    public DynamicRenderer(Graphics g, int w, int h, int columns, int rows) {
        super(g, w, h);
        unitW = w/columns;
        unitH = h/rows;
    }

    public void fillRectangle(int x, int y, int width, int height, Color color) {
        x -= camX;
        y -= camY;
        x *= unitW;
        y *= unitH;
        width *= unitW;
        height *= unitH;
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public void drawRectangle(int x, int y, int width, int height, Color color) {
        x -= camX;
        y -= camY;
        x *= unitW;
        y *= unitH;
        width *= unitW;
        height *= unitH;
        g.setColor(color);
        g.drawRect(x, y, width, height);
    }

    public void setClip(int x, int y, int width, int height) {
        x -= camX;
        y -= camY;
        x *= unitW;
        y *= unitH;
        width *= unitW;
        height *= unitH;
        g.setClip(x, y, width, height);
    }

    public void fillOval(int x, int y, int width, int height, Color color) {
        x -= camX;
        y -= camY;
        x *= unitW;
        y *= unitH;
        width *= unitW;
        height *= unitH;
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }

    @Override
    public void drawOval(int x, int y, int width, int height, Color color) {
        x -= camX;
        y -= camY;
        x *= unitW;
        y *= unitH;
        width *= unitW;
        height *= unitH;
        g.setColor(color);
        g.drawOval(x, y, width, height);
    }

    @Override
    public void fillCircle(int x, int y, int radius, Color color) {
        x -= camX;
        y -= camY;
        x *= unitW;
        y *= unitH;
        radius *= unitW;
        g.setColor(color);
        g.fillOval(x-radius/2, y-radius/2, radius, radius);
    }

    @Override
    public void drawCircle(int x, int y, int radius, Color color) {
        x -= camX;
        y -= camY;
        x *= unitW;
        y *= unitH;
        radius *= unitW;
        g.setColor(color);
        g.drawOval(x-radius/2, y-radius/2, radius, radius);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        x1 -= camX;
        y1 -= camY;
        x2 -= camX;
        y2 -= camY;
        x1 *= unitW;
        y1 *= unitH;
        x2 *= unitW;
        y2 *= unitH;
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawLineWidth(int x1, int y1, int x2, int y2, int width, Color color) {
        x1 -= camX;
        y1 -= camY;
        x2 -= camX;
        y2 -= camY;
        x1 *= unitW;
        y1 *= unitH;
        x2 *= unitW;
        y2 *= unitH;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(width));
        g2.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
        x1 -= camX;
        y1 -= camY;
        x2 -= camX;
        y2 -= camY;
        x1 *= unitW;
        y1 *= unitH;
        x2 *= unitW;
        y2 *= unitH;
        x3 *= unitW;
        y3 *= unitH;
        g.setColor(color);
        g.fillPolygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
    }

    @Override
    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
        x1 -= camX;
        y1 -= camY;
        x2 -= camX;
        y2 -= camY;
        x1 *= unitW;
        y1 *= unitH;
        x2 *= unitW;
        y2 *= unitH;
        x3 *= unitW;
        y3 *= unitH;
        g.setColor(color);
        g.drawPolygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, Color color) {
        int points = Math.min(xPoints.length, yPoints.length);
        for(int i = 0; i < points; i++) {
            xPoints[i] -= camX;
            yPoints[i] -= camY;
            xPoints[i] *= unitW;
            yPoints[i] *= unitH;
        }
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, points);
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, Color color) {
        int points = Math.min(xPoints.length, yPoints.length);
        for(int i = 0; i < points; i++) {
            xPoints[i] -= camX;
            yPoints[i] -= camY;
            xPoints[i] *= unitW;
            yPoints[i] *= unitH;
        }
        g.setColor(color);
        g.drawPolygon(xPoints, yPoints, points);
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int points, Color color) {
        for(int i = 0; i < points; i++) {
            xPoints[i] -= camX;
            yPoints[i] -= camY;
            xPoints[i] *= unitW;
            yPoints[i] *= unitH;
        }
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, points);
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int points, Color color) {
        for(int i = 0; i < points; i++) {
            xPoints[i] -= camX;
            yPoints[i] -= camY;
            xPoints[i] *= unitW;
            yPoints[i] *= unitH;
        }
        g.setColor(color);
        g.drawPolygon(xPoints, yPoints, points);
    }

    @Override
    public void fillPolygon(Polygon polygon, Color color) {
        int points = polygon.npoints;
        for(int i = 0; i < points; i++) {
            polygon.xpoints[i] -= camX;
            polygon.ypoints[i] -= camY;
            polygon.xpoints[i] *= unitW;
            polygon.ypoints[i] *= unitH;
        }
        g.setColor(color);
        g.fillPolygon(polygon);
    }

    @Override
    public void drawPolygon(Polygon polygon, Color color) {
        int points = polygon.npoints;
        for(int i = 0; i < points; i++) {
            polygon.xpoints[i] -= camX;
            polygon.ypoints[i] -= camY;
            polygon.xpoints[i] *= unitW;
            polygon.ypoints[i] *= unitH;
        }
        g.setColor(color);
        g.drawPolygon(polygon);
    }

    @Override
    public void fillPolygon(Color color, int... points) {
        int[] pointsListX = new int[points.length/2];
        int[] pointsListY = new int[points.length/2];
        for(int i = 0; i < points.length-1; i+=2) {
            pointsListX[i/2] = points[i];
            pointsListY[i/2] = points[i+1];
            pointsListX[i/2]-=camX;
            pointsListY[i/2]-=camY;
            pointsListX[i/2]*=unitW;
            pointsListY[i/2]*=unitH;
        }
        g.setColor(color);
        g.fillPolygon(pointsListX, pointsListY, points.length/2);
    }

    @Override
    public void drawPolygon(Color color, int... points) {
        int[] pointsListX = new int[points.length/2];
        int[] pointsListY = new int[points.length/2];
        for(int i = 0; i < points.length-1; i+=2) {
            pointsListX[i/2] = points[i];
            pointsListY[i/2] = points[i+1];
            pointsListX[i/2]-=camX;
            pointsListY[i/2]-=camY;
            pointsListX[i/2]*=unitW;
            pointsListY[i/2]*=unitH;
        }
        g.setColor(color);
        g.drawPolygon(pointsListX, pointsListY, points.length/2);
    }

    @Override
    public void drawImage(int x, int y, BufferedImage image) {
        x -= camX;
        y -= camY;
        x *= unitW;
        y *= unitH;
        g.drawImage(image, x, y, null);
    }

    @Override
    public void drawImage(int x, int y, int width, int height, BufferedImage image) {
        x -= camX;
        y -= camY;
        x *= unitW;
        y *= unitH;
        g.drawImage(image, x, y, width, height, null);
    }

    @Override
    public void drawPixel(int x, int y, Color color) {
        x -= camX;
        y -= camY;
        x *= unitW;
        y *= unitH;
        fillRectangle(x, y, 1, 1, color);
    }

    @Override
    public void drawText(String text, int offX, int offY, int size, Color color) {
        offX -= camX;
        offY -= camY;
        offX *= unitW;
        offY *= unitH;
        g.setColor(color);
        g.setFont(new java.awt.Font(font.getName(), font.getStyle(), size));
        g.drawString(text, offX+size/2, offY+size);
    }

    @Override
    public void drawText(String text, int offX, int offY, int size, Alignment alignment, Color color) {
        offX -= camX;
        offY -= camY;
        offX *= unitW;
        offY *= unitH;
        int width = (int)g.getFontMetrics(new java.awt.Font(font.getName(), font.getStyle(), size)).getStringBounds(text, g).getWidth();
        int height = (int)g.getFontMetrics(new java.awt.Font(font.getName(), font.getStyle(), size)).getStringBounds(text, g).getHeight();
        int xx=offX, yy=offY;

        if(alignment.getAlignmenthor() == Alignment.HOR_LEFT) {
            xx = offX-width;
        } else if(alignment.getAlignmenthor() == Alignment.HOR_CENTER) {
            xx = offX - width/2;
        }

        if (alignment.getAlignmentver() == Alignment.VER_BOTTOM) {
            yy = offY + height/2;
        } else if (alignment.getAlignmentver() == Alignment.VER_MIDDLE) {
            yy = offY + height/4;
        }

        g.setColor(color);
        g.setFont(new java.awt.Font(font.getName(), font.getStyle(), size));
        g.drawString(text, xx, yy);//+size);
    }

}
