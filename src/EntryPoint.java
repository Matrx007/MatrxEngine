import com.engine.Core;
import com.engine.Game;
import com.engine.libs.graphics.abstractBody.AbstractBody;
import com.engine.libs.graphics.abstractBody.AbstractPiece;
import com.engine.libs.graphics.abstractBody.shapes.Circle;
import com.engine.libs.graphics.abstractBody.shapes.Rectangle;

import java.awt.*;

public class EntryPoint extends Game {
    public static void main(String[] args) {
        if(args[1].equalsIgnoreCase("open-gl")) {
            System.setProperty("sun.java2d.opengl","True");
        }

        new EntryPoint();
    }

    AbstractBody button;

    public EntryPoint() {
        e.start();

        // TODO: Init
        AbstractPiece[] pieces = new AbstractPiece[]{

        };
        button = new AbstractBody(96, 96, 320, 128, pieces);

        e.run();
    }

    @Override
    public void update(Core e) {
        button.w = e.getInput().getMouseX()-button.x;
        button.h = e.getInput().getMouseY()-button.y;
    }

    @Override
    public void render(Core e) {
        e.getRenderer().fillRectangle(0, 0, e.width, e.height, Color.gray);
        button.render(e.getRenderer());
    }
}
