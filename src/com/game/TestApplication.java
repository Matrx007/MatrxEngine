import com.engine.Core;
import com.engine.Game;

import java.awt.*;

// Your class must extend Game, when you try to create Core class,
// it might not work and you don't have the same functionality.
public class TestApplication extends Game {

    // Your application entry point.
    public static void main(String[] args) {
        // You must call it in order to execute it's construct.
        new TestApplication();
    }

    private TestApplication() {
        // Set the dimensions of the window
        // Must be done before e.start()
        // because e.start() create the window
        // and renderer and all the other
        // stuff.
        e.width = 800;
        e.height = 800;

        // Prepares the engine and
        // initializes classes.
        e.start();

        // I made it into another method but
        // you don't have to. Anyway here's
        // your game startup/creation code.
        initGame();

        // Starts game loop.
        e.run();

        // Just an example so you understand
        // that the e.run() wouldn't stop before
        // you close the game, or exception
        // occurred.
        System.out.println("Game was closed!");
    }

    // Variables that I use for this example.
    private int currentX, currentY, currentW, currentH, endX, endY;

    private void initGame() {
        e.getWindow().getFrame().setTitle("Test Application - Powered by MatrxEngine II");

        currentX = 0;
        currentY = 0;
        currentW = e.getWidth()/2;
        currentH = e.getHeight()/2;
        endX = e.getWidth();
        endY = e.getHeight();
    }

    @Override
    public void update(Core e) {
        // Here's your game update code,
        // default it's 60 tick per second.

        // Simple rendering. SCROLL DOWN, THIS ISN'T THE
        // PLACE TO RENDER. ONLY IF YOU KNOW WHAT YOU
        // ARE DOING.
        e.getRenderer().fillRectangle(currentX, currentY, currentW, currentH, Color.white);
        currentX += currentW;
        currentY += currentH;
        currentW /= 2;
        currentH /= 2;

        // Check if we reached the end
        if((currentX == endX) || (currentY == endY)) {
            // We are done!
            e.stop();
        }
    }

    @Override
    public void render(Core e) {
        // Here's your render code.
        // This get executed as many times as
        // your computer can.

        // Use this line when you want to redraw the
        // scene every time, I currently don't.
        // e.getRenderer().fillRectangle(0, 0, e.width, e.height, Color.black);

        // That code here is empty because I do the rendering in the update method.
        // Don't do that! Or only if you know what you are doing.
        // You might crash the game if you do too a lot of rendering
        // in there.
    }
}
