package com.engine.libs.rendering;

import com.engine.Core;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window {

    private JFrame frame;
    private BufferedImage image;
    private Canvas canvas;
    private BufferStrategy bs;
    private Graphics g;
    private Core e;

    public boolean createFrame=true, ScaleView = false;
    public JComponent target;

    public Window(Core e, boolean startAsFullscreen) {
        this.e = e;
        this.createFrame = !e.disableJFrame;
        this.target = e.target;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        image = gc.createCompatibleImage(e.getWidth(), e.getHeight(), Transparency.OPAQUE);
        image.setAccelerationPriority(1);
        canvas = new Canvas();
        Dimension s = new Dimension((int)(e.getWidth() * e.getScale()), (int)(e.getHeight() * e.getScale()));
        canvas.setPreferredSize(s);
        canvas.setMinimumSize(s);
        canvas.setMinimumSize(s);
        if(createFrame) {
            frame = new JFrame(e.getTitle());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(false);
            frame.setLayout(new BorderLayout());
            if(startAsFullscreen) {
                frame.setUndecorated(true);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            frame.add(canvas, BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);

        } else {
            target.add(canvas);
        }

        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        g = bs.getDrawGraphics();
        Color c = g.getColor();
        g.setColor(new Color(192, 192, 192));
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setColor(c);
        image.createGraphics().clearRect(0, 0, e.getWidth(), e.getHeight());
    }

    public void update() {
        g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        bs.show();
    }

    public Graphics getG() {
        return image.createGraphics();
    }

    public JFrame getFrame() {
        return frame;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public BufferedImage getImage() {
        return image;
    }

    /*public BufferedImage takePicture() {
        //BufferedImage bi =new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        //Graphics2D g2 = bi.createGraphics();
        //canvas.printAll(g2);
        return image;
    }*/
}
