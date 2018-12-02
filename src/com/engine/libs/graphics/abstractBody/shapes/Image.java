package com.engine.libs.graphics.abstractBody.shapes;

import com.engine.libs.graphics.abstractBody.AbstractPiece;
import com.engine.libs.rendering.RenderUtils;
import com.engine.libs.rendering.Renderer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Image extends AbstractPiece {
    private BufferedImage image, resizedImage;

    public Image(double x, double y, double w, double h, BufferedImage image) {
        super(x, y, w, h, null);
        this.image = image;
    }

    @Override
    public void render(Renderer r, int x, int y, int width, int height) {
        if(resizedImage == null || width != resizedImage.getWidth() || height != resizedImage.getHeight()) {
            resizedImage = RenderUtils.resize(image, (int)(w*width), (int)(h*height));
        }

        r.drawImage(x+(this.x*width), y+(this.y*height), resizedImage);
    }
}
