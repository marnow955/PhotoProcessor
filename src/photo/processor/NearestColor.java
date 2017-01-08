package photo.processor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by marek on 2017-01-07.
 */
public class NearestColor implements PhotoProcessor {

    private Color[] palette;

    private BufferedImage image;
    private BufferedImage resultImg;

    private int width;
    private int height;

    @Override
    public BufferedImage getTransformedImage(BufferedImage image, Color[] paletteOfColors) {
        init(image,paletteOfColors);

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int resultColorIndex = findNearestPaletteColor(new Color(image.getRGB(x,y)));
                resultImg.setRGB(x,y,palette[resultColorIndex].getRGB());
            }
        }

        return resultImg;
    }

    private void init(BufferedImage image, Color[] paletteOfColors) {
        this.palette = paletteOfColors;
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        resultImg = new BufferedImage(width,height,image.getType());
    }

    private int findNearestPaletteColor(Color color) {
        int nearestColorIndex = 0;

        for (int i=1; i<palette.length; i++) {
            if (diff(palette[i],color)<diff(palette[nearestColorIndex],color))
                nearestColorIndex = i;
        }

        return nearestColorIndex;
    }

    private int diff(Color paletteColor, Color color) {
        return  (int)(
                Math.pow(paletteColor.getRed()-color.getRed(),2) +
                Math.pow(paletteColor.getGreen()-color.getGreen(),2) +
                Math.pow(paletteColor.getBlue()-color.getBlue(),2)
        );
    }
}
