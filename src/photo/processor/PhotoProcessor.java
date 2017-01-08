package photo.processor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by marek on 2017-01-06.
 */
public interface PhotoProcessor {

    public static final Color[] TWO_COLORS_PALETTE = {
            Color.BLACK, // 0 0 0
            Color.WHITE // 255 255 255
    };

    public static final Color[] EIGHT_COLORS_PALETTE = {
            Color.BLACK, // 0 0 0
            Color.BLUE, // 0 0 255
            Color.GREEN, // 0 255 0
            Color.CYAN, // 0 255 255
            Color.RED, // 255 0 0
            Color.MAGENTA, // 255 0 255
            Color.YELLOW, // 255 255 0
            Color.WHITE // 255 255 255
    };

    public static final Color[] TWENTY_SEVEN_COLORS_PALETTE = {
            new Color(0,0,0),
            new Color(0,0,127),
            new Color(0,0,255),
            new Color(0,127,0),
            new Color(0,127,127),
            new Color(0,127,255),
            new Color(0,255,0),
            new Color(0,255,127),
            new Color(0,255,255),
            new Color(127,0,0),
            new Color(127,0,127),
            new Color(127,0,255),
            new Color(127,127,0),
            new Color(127,127,127),
            new Color(127,127,255),
            new Color(127,255,0),
            new Color(127,255,127),
            new Color(127,255,255),
            new Color(255,0,0),
            new Color(255,0,127),
            new Color(255,0,255),
            new Color(255,127,0),
            new Color(255,127,127),
            new Color(255,127,255),
            new Color(255,255,0),
            new Color(255,255,127),
            new Color(255,255,255),
    };

    public BufferedImage getTransformedImage(BufferedImage image, Color[] paletteOfColors);
}
