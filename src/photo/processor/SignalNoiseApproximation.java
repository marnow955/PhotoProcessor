package photo.processor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by marek on 2017-01-08.
 */
public class SignalNoiseApproximation extends NearestColor {

    @Override
    public BufferedImage getTransformedImage(BufferedImage image, Color[] paletteOfColors) {
        BufferedImage imageWithNoise = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        for (int y=0; y<image.getHeight(); y++) {
            for (int x=0; x<image.getWidth(); x++) {
                imageWithNoise.setRGB(x,y,noiseAdd(image.getRGB(x,y)));
            }
        }
        return super.getTransformedImage(imageWithNoise, paletteOfColors);
    }

    private int noiseAdd(int rgbColor) {
        Color color = new Color(rgbColor);
        double r = Math.random();
        Color result = new Color(
                validate(color.getRed() + (int) ((color.getRed()-0.1*color.getRed()) + r*(color.getRed()+0.1*color.getRed()))),
                validate(color.getGreen() + (int) ((color.getGreen()-0.1*color.getGreen()) + r*(color.getGreen()+0.1*color.getGreen()))),
                validate(color.getBlue() + (int) ((color.getBlue()-0.1*color.getBlue()) + r*(color.getBlue()+0.1*color.getBlue())))
        );
        return result.getRGB();
    }

    private int validate(int rgbValue) {
        if (rgbValue<0)
            return 0;
        else if(rgbValue>255)
            return 255;
        else
            return rgbValue;
    }
}
