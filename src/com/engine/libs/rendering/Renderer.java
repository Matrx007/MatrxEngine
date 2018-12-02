package com.engine.libs.rendering;

import com.engine.Core;
import com.engine.libs.font.Alignment;
import com.engine.libs.rendering.surface.Surface;

import java.awt.*;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Renderer {

    protected Graphics g;
    protected int w, h;
    protected Core e;

    public Font font = new java.awt.Font("Arial", java.awt.Font.PLAIN, 12);

    //private com.Core.libs.rendering.Font font = com.Core.libs.rendering.Font.STANDARD;
    protected int camX, camY;

    private int staticCamX, staticCamY, shiftX, shiftY;
    private boolean absolute;
    
    private Filter[] filters;

    public Renderer(Core Core) {
        this.g = Core.getWindow().getG();
        w = Core.getWindow().getCanvas().getWidth();
        h = Core.getWindow().getCanvas().getHeight();
        camX=0;
        camY=0;
        staticCamX = 0;
        staticCamY = 0;
        absolute = false;
        this.e = Core;
        init();
    }

    public Renderer(Surface surf) {
        w = surf.getW();
        h = surf.getH();
        this.g = surf.getSurface().getGraphics();
        camX=0;
        camY=0;
        staticCamX = 0;
        staticCamY = 0;
        absolute = false;
        init();
    }

    public Renderer(Graphics g, int w, int h) {
        this.w = w;
        this.h = h;
        this.g = g;
        camX=0;
        camY=0;
        staticCamX = 0;
        staticCamY = 0;
        absolute = false;
        init();
    }
    
    private void init() {
        filters = new Filter[16];
        shiftX = 0;
        shiftY = 0;
    }

    public void absolute() {
        if(!absolute) {
            staticCamX = camX;
            staticCamY = camY;
            camX = 0;
            camY = 0;
            shiftX = 0;
            shiftY = 0;
            absolute = true;
        }
    }

    public void relative() {
        if(absolute) {
            camX = staticCamX;
            camY = staticCamY;
            staticCamX = 0;
            staticCamY = 0;
            shiftX = 0;
            shiftY = 0;
            absolute = false;
        }
    }
    
    public void setFilter(int index, Filter filter) {
        filters[index] = filter;
    }
    
    public Filter getFilter(int index) {
        return filters[index];
    }
    
    public void removeFilter(int index) {
        filters[index] = null;
    }

    public void clearFilters() {
        filters = new Filter[filters.length];
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void fillRectangle(int x, int y, int width, int height, Color color) {
        x -= camX;
        y -= camY;
        setColor(color);
        g.fillRect(x, y, width, height);
    }

    public void fillRectangle(double x, double y, int width, int height, Color color) {
        x -= camX;
        y -= camY;
        setColor(color);
        g.fillRect((int)x, (int)y, width, height);
    }

    public void drawRectangle(int x, int y, int width, int height, Color color) {
        x -= camX;
        y -= camY;
        setColor(color);
        g.drawRect(x, y, width, height);
    }

    public void drawRectangle(double x, double y, int width, int height, Color color) {
        x -= camX;
        y -= camY;
        setColor(color);
        g.drawRect((int)x, (int)y, width, height);
    }

    public void setClip(int x, int y, int width, int height) {
        x -= camX;
        y -= camY;
        g.setClip(x, y, width, height);
    }

    public void setClip(double x, double y, int width, int height) {
        x -= camX;
        y -= camY;
        g.setClip((int)x, (int)y, width, height);
    }

    public void fillOval(double x, double y, int width, int height, Color color) {
        x -= camX;
        y -= camY;
        setColor(color);
        g.fillOval((int)x, (int)y, width, height);
    }

    public void drawOval(int x, int y, int width, int height, Color color) {
        x -= camX;
        y -= camY;
        setColor(color);
        g.drawOval(x, y, width, height);
    }

    public void drawOval(double x, double y, int width, int height, Color color) {
        x -= camX;
        y -= camY;
        setColor(color);
        g.drawOval((int)x, (int)y, width, height);
    }

    public void fillCircle(int x, int y, int radius, Color color) {
        x -= camX;
        y -= camY;
        setColor(color);
        g.fillOval(x-radius/2, y-radius/2, radius, radius);
    }

    public void fillCircle(double x, double y, int radius, Color color) {
        x -= camX;
        y -= camY;
        setColor(color);
        g.fillOval((int)(x-radius/2), (int)(y-radius/2), radius, radius);
    }

    public void drawCircle(int x, int y, int radius, Color color) {
        x -= camX;
        y -= camY;
        setColor(color);
        g.drawOval(x-radius/2, y-radius/2, radius, radius);
    }

    public void drawCircle(double x, double y, int radius, Color color) {
        x -= camX;
        y -= camY;
        setColor(color);
        g.drawOval((int)(x-radius/2), (int)(y-radius/2), radius, radius);
    }

    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        x1 -= camX;
        y1 -= camY;
        x2 -= camX;
        y2 -= camY;
        setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }

    public void drawLine(double x1, double y1, double x2, double y2, Color color) {
        x1 -= camX;
        y1 -= camY;
        x2 -= camX;
        y2 -= camY;
        setColor(color);
        g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
    }

    public void drawLineWidth(int x1, int y1, int x2, int y2, int width, Color color) {
        x1 -= camX;
        y1 -= camY;
        x2 -= camX;
        y2 -= camY;
        Graphics2D g2 = (Graphics2D) g;
        setColor(color);
        g2.setStroke(new BasicStroke(width));
        g2.drawLine(x1, y1, x2, y2);
    }

    public void drawLineWidth(double x1, double y1, double x2, double y2, int width, Color color) {
        x1 -= camX;
        y1 -= camY;
        x2 -= camX;
        y2 -= camY;
        Graphics2D g2 = (Graphics2D) g;
        setColor(color);
        g2.setStroke(new BasicStroke(width));
        g2.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
        x1 -= camX;
        y1 -= camY;
        x2 -= camX;
        y2 -= camY;
        setColor(color);
        g.fillPolygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
    }

    public void fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
        x1 -= camX;
        y1 -= camY;
        x2 -= camX;
        y2 -= camY;
        setColor(color);
        g.fillPolygon(new int[]{(int)x1, (int)x2, (int)x3}, new int[]{(int)y1, (int)y2, (int)y3}, 3);
    }

    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
        x1 -= camX;
        y1 -= camY;
        x2 -= camX;
        y2 -= camY;
        setColor(color);
        g.drawPolygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
    }

    public void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3, Color color) {
        x1 -= camX;
        y1 -= camY;
        x2 -= camX;
        y2 -= camY;
        setColor(color);
        g.drawPolygon(new int[]{(int)x1, (int)x2, (int)x3}, new int[]{(int)y1, (int)y2, (int)y3}, 3);
    }

    public void fillPolygon(Color color, int... points) {
        int[] pointsListX = new int[points.length/2];
        int[] pointsListY = new int[points.length/2];
        for(int i = 0; i < points.length-1; i+=2) {
            pointsListX[i/2] = points[i];
            pointsListY[i/2] = points[i+1];
            pointsListX[i/2]-=camX;
            pointsListY[i/2]-=camY;
        }
        setColor(color);
        g.fillPolygon(pointsListX, pointsListY, points.length/2);
    }

    public void fillPolygon(Color color, double... points) {
        int[] pointsListX = new int[points.length/2];
        int[] pointsListY = new int[points.length/2];
        for(int i = 0; i < points.length-1; i+=2) {
            pointsListX[i/2] = (int)points[i];
            pointsListY[i/2] = (int)points[i+1];
            pointsListX[i/2]-=camX;
            pointsListY[i/2]-=camY;
        }
        setColor(color);
        g.fillPolygon(pointsListX, pointsListY, points.length/2);
    }

    public void drawPolygon(Color color, int... points) {
        int[] pointsListX = new int[points.length/2];
        int[] pointsListY = new int[points.length/2];
        for(int i = 0; i < points.length-1; i+=2) {
            pointsListX[i/2] = points[i];
            pointsListY[i/2] = points[i+1];
            pointsListX[i/2]-=camX;
            pointsListY[i/2]-=camY;
        }
        setColor(color);
        g.drawPolygon(pointsListX, pointsListY, points.length/2);
    }

    public void drawPolygon(Color color, double... points) {
        int[] pointsListX = new int[points.length/2];
        int[] pointsListY = new int[points.length/2];
        for(int i = 0; i < points.length-1; i+=2) {
            pointsListX[i/2] = (int)points[i];
            pointsListY[i/2] = (int)points[i+1];
            pointsListX[i/2]-=camX;
            pointsListY[i/2]-=camY;
        }
        setColor(color);
        g.drawPolygon(pointsListX, pointsListY, points.length/2);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, Color color) {
        int points = Math.min(xPoints.length, yPoints.length);
        for(int i = 0; i < points; i++) {
            xPoints[i] -= camX;
            yPoints[i] -= camY;
        }
        setColor(color);
        g.fillPolygon(xPoints, yPoints, points);
    }

    public void fillPolygon(double[] xPoints, double[] yPoints, Color color) {
        int points = Math.min(xPoints.length, yPoints.length);

        final int[] xPointsNew= new int[points];
        for (int i=0; i<points; ++i)
            xPointsNew[i] = (int) xPoints[i];
        final int[] yPointsNew= new int[points];
        for (int i=0; i<points; ++i)
            yPointsNew[i] = (int) yPoints[i];

        for(int i = 0; i < points; i++) {
            xPointsNew[i] -= camX;
            yPointsNew[i] -= camY;
        }
        setColor(color);
        g.fillPolygon(xPointsNew, yPointsNew, points);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, Color color) {
        int points = Math.min(xPoints.length, yPoints.length);
        for(int i = 0; i < points; i++) {
            xPoints[i] -= camX;
            yPoints[i] -= camY;
        }
        setColor(color);
        g.drawPolygon(xPoints, yPoints, points);
    }

    public void drawPolygon(double[] xPoints, double[] yPoints, Color color) {
        int points = Math.min(xPoints.length, yPoints.length);

        final int[] xPointsNew= new int[points];
        for (int i=0; i<points; ++i)
            xPointsNew[i] = (int) xPoints[i];
        final int[] yPointsNew= new int[points];
        for (int i=0; i<points; ++i)
            yPointsNew[i] = (int) yPoints[i];

        for(int i = 0; i < points; i++) {
            xPointsNew[i] -= camX;
            yPointsNew[i] -= camY;
        }
        setColor(color);
        g.drawPolygon(xPointsNew, yPointsNew, points);
    }

    public void fillPolygon(double[] xPoints, double[] yPoints, int points, Color color) {

        final int[] xPointsNew= new int[points];
        for (int i=0; i<points; ++i)
            xPointsNew[i] = (int) xPoints[i];
        final int[] yPointsNew= new int[points];
        for (int i=0; i<points; ++i)
            yPointsNew[i] = (int) yPoints[i];

        for(int i = 0; i < points; i++) {
            xPointsNew[i] -= camX;
            yPointsNew[i] -= camY;
        }
        setColor(color);
        g.fillPolygon(xPointsNew, yPointsNew, points);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int points, Color color) {
        for(int i = 0; i < points; i++) {
            xPoints[i] -= camX;
            yPoints[i] -= camY;
        }
        setColor(color);
        g.fillPolygon(xPoints, yPoints, points);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int points, Color color) {
        for(int i = 0; i < points; i++) {
            xPoints[i] -= camX;
            yPoints[i] -= camY;
        }
        setColor(color);
        g.drawPolygon(xPoints, yPoints, points);
    }

    public void drawPolygon(double[] xPoints, double[] yPoints, int points, Color color) {

        final int[] xPointsNew= new int[points];
        for (int i=0; i<points; ++i)
            xPointsNew[i] = (int) xPoints[i];
        final int[] yPointsNew= new int[points];
        for (int i=0; i<points; ++i)
            yPointsNew[i] = (int) yPoints[i];

        for(int i = 0; i < points; i++) {
            xPointsNew[i] -= camX;
            yPointsNew[i] -= camY;
        }
        setColor(color);
        g.drawPolygon(xPointsNew, yPointsNew, points);
    }

    public void fillPolygon(Polygon polygon, Color color) {
        int points = polygon.npoints;
        for(int i = 0; i < points; i++) {
            polygon.xpoints[i] -= camX;
            polygon.ypoints[i] -= camY;
        }
        setColor(color);
        g.fillPolygon(polygon);
    }

    public void fillPolygon(Polygon polygon) {
        int points = polygon.npoints;
        for(int i = 0; i < points; i++) {
            polygon.xpoints[i] -= camX;
            polygon.ypoints[i] -= camY;
        }
        g.fillPolygon(polygon);
    }

    public void drawPolygon(Polygon polygon, Color color) {
        int points = polygon.npoints;
        for(int i = 0; i < points; i++) {
            polygon.xpoints[i] -= camX;
            polygon.ypoints[i] -= camY;
        }
        setColor(color);
        g.drawPolygon(polygon);
    }

    public void drawImage(int x, int y, BufferedImage image) {
        x -= camX;
        y -= camY;
        g.drawImage(image, x, y, null);
    }

    public void drawImage(double x, double y, BufferedImage image) {
        x -= camX;
        y -= camY;
        g.drawImage(image, (int)x, (int)y, null);
    }

    public void drawImage(int x, int y, int width, int height, BufferedImage image) {
        x -= camX;
        y -= camY;
        g.drawImage(image, x, y, width, height, null);
    }

    public void drawImage(double x, double y, int width, int height, BufferedImage image) {
        x -= camX;
        y -= camY;
        g.drawImage(image, (int)x, (int)y, width, height, null);
    }

    public void drawPixel(int x, int y, Color color) {
        x -= camX;
        y -= camY;
        fillRectangle(x, y, 1, 1, color);
    }

    public void drawPixel(double x, double y, Color color) {
        x -= camX;
        y -= camY;
        fillRectangle((int)x, (int)y, 1, 1, color);
    }

    public void clear() {
        g.clearRect(0, 0, e.getWindow().getCanvas().getWidth(), e.getWindow().getCanvas().getHeight());
    }

    public void drawText(String text, int offX, int offY, int size, Color color) {
        offX -= camX;
        offY -= camY;
        setColor(color);
        g.setFont(new java.awt.Font(font.getName(), font.getStyle(), size));
        g.drawString(text, offX+size/2, offY+size);
    }

    public void drawText(String text, double offX, double offY, int size, Color color) {
        offX -= camX;
        offY -= camY;
        setColor(color);
        g.setFont(new java.awt.Font(font.getName(), font.getStyle(), size));
        g.drawString(text, (int)(offX+size/2), (int)(offY+size));
    }

    public void drawText(String text, int offX, int offY, int size, Alignment alignment, Color color) {
        offX -= camX;
        offY -= camY;
        int width = (int)g.getFontMetrics(new java.awt.Font(font.getName(), font.getStyle(), size)).getStringBounds(text, g).getWidth();
        int height = (int)g.getFontMetrics(new java.awt.Font(font.getName(), font.getStyle(), size)).getStringBounds(text, g).getHeight();
        int xx=offX, yy=offY;

        if(alignment.getAlignmenthor() == Alignment.HOR_RIGHT) {
            xx = offX;
        } else if(alignment.getAlignmenthor() == Alignment.HOR_LEFT) {
            xx = offX-width;
        } else if(alignment.getAlignmenthor() == Alignment.HOR_CENTER) {
            xx = offX - width/2;
        }

        if(alignment.getAlignmentver() == Alignment.VER_TOP) {
            yy = offY;
        } else if (alignment.getAlignmentver() == Alignment.VER_BOTTOM) {
            yy = offY + height/2;
        } else if (alignment.getAlignmentver() == Alignment.VER_MIDDLE) {
            yy = offY + height/4;
        }

        setColor(color);
        g.setFont(new java.awt.Font(font.getName(), font.getStyle(), size));
        g.drawString(text, xx, yy);//+size);
    }

    public void drawText(String text, double offX, double offY, int size, Alignment alignment, Color color) {
        offX -= camX;
        offY -= camY;
        int width = (int)g.getFontMetrics(new java.awt.Font(font.getName(), font.getStyle(), size)).getStringBounds(text, g).getWidth();
        int height = (int)g.getFontMetrics(new java.awt.Font(font.getName(), font.getStyle(), size)).getStringBounds(text, g).getHeight();
        double xx=offX, yy=offY;

        if(alignment.getAlignmenthor() == Alignment.HOR_RIGHT) {
            xx = (int)offX;
        } else if(alignment.getAlignmenthor() == Alignment.HOR_LEFT) {
            xx = (int)(offX-width);
        } else if(alignment.getAlignmenthor() == Alignment.HOR_CENTER) {
            xx = (int)(offX - width/2);
        }

        if(alignment.getAlignmentver() == Alignment.VER_TOP) {
            yy = (int)offY;
        } else if (alignment.getAlignmentver() == Alignment.VER_BOTTOM) {
            yy = (int)(offY + height/2);
        } else if (alignment.getAlignmentver() == Alignment.VER_MIDDLE) {
            yy = (int)(offY + height/4);
        }

        setColor(color);
        g.setFont(new java.awt.Font(font.getName(), font.getStyle(), size));
        g.drawString(text, (int)xx, (int)yy);//+size);
    }

    public int getCamX() {
        return camX;
    }

    public void setCamX(int camX) {
        this.camX = camX;
    }

    public int getCamY() {
        return camY;
    }

    public void setCamY(int camY) {
        this.camY = camY;
    }

    public Graphics getG() { return g; }
    
    public void setColor(Color color) {
        Color c = g.getColor();
        boolean changed = false;
        for(Filter filter : filters) {
            if(filter != null) {
                c = filter.filter(color, c);
                changed = true;
            }
        }

        if(!changed) g.setColor(color); else g.setColor(c);
    }

    public void shift(int x, int y) {
        shiftX += x;
        shiftY += y;
        camX -= x;
        camY -= y;
    }

    public void unshift() {
        camX += shiftX;
        camY += shiftY;
        shiftX = 0;
        shiftY = 0;
    }

    public void render(com.engine.libs.graphics.Shape shape) {
        if(shape.getR() != 0) {
            if(shape.isFill()) {
                fillRectangle(shape.getX(), shape.getY(), shape.getW(), shape.getH(), shape.getC());
            } else {
                g.setColor(shape.getC());
                g.drawRect(shape.getX(), shape.getY(), shape.getW(), shape.getH());
            }
        } else {
            if(shape.isFill()) {
                fillCircle(shape.getX(), shape.getY(), shape.getR(), shape.getC());
            } else {
                g.setColor(shape.getC());
                g.drawOval(shape.getX(), shape.getY(), shape.getR()*2, shape.getR()*2);
            }
        }
    }
}
