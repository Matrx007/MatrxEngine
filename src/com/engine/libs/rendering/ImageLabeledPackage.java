package com.engine.libs.rendering;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageLabeledPackage {
    private HashMap<String, Image> pack;

    public ImageLabeledPackage() {
        pack = new HashMap<>();
    }

    public void addImage(Image img, String index) {
        pack.put(index, img);
    }

    public void removeImage(String index) {
        pack.remove(index);
    }

    public Image getImage(String index) {
        return pack.get(index);
    }
}
