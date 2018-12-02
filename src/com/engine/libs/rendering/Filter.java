package com.engine.libs.rendering;

import java.awt.*;

public interface Filter {
    Color filter(Color newColor, Color previousColor);
}
