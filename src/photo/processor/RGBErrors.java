package photo.processor;

import java.awt.*;

/**
 * Created by marek on 2017-01-06.
 */
public class RGBErrors {
    int r;
    int g;
    int b;

    public RGBErrors(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public RGBErrors(Color color) {
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
    }

    public int getRedError() {
        return r;
    }

    public int getGreenError() {
        return g;
    }

    public int getBlueError() {
        return b;
    }
}
