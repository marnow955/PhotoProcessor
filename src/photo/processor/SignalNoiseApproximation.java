package photo.processor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by marek on 2017-01-08.
 */
public class SignalNoiseApproximation implements PhotoProcessor {

    private Color[] palette;

    private BufferedImage image;
    private BufferedImage resultImg;

    private int width;
    private int height;

    private int aRate = 52;
    private int alfaRate = 128;
    private int betaRate = 128;

    @Override
    public BufferedImage getTransformedImage(BufferedImage image, Color[] paletteOfColors) {
        init(image,paletteOfColors);

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int resultColorIndex = findNearestPaletteColor(new Color(image.getRGB(x,y)),x,y);
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

    private int findNearestPaletteColor(Color color, int x,int y) {
        int nearestColorIndex = 0;

        for (int i=1; i<palette.length; i++) {
            if (diff(palette[i],color,x,y)<diff(palette[nearestColorIndex],color,x,y))
                nearestColorIndex = i;
        }

        return nearestColorIndex;
    }

    private int diff(Color paletteColor, Color color,int x, int y) {
        double r = Math.random();
        return (int) (
                Math.pow(paletteColor.getRed()-color.getRed()+aRate*Math.sin(alfaRate*x)*Math.sin(betaRate*y),2) +
                Math.pow(paletteColor.getGreen()-color.getGreen()+aRate*Math.sin(alfaRate*x)*Math.sin(betaRate*y),2) +
                Math.pow(paletteColor.getBlue()-color.getBlue()+aRate*Math.sin(alfaRate*x)*Math.sin(betaRate*y),2)
                );
//        return  (int)(
//                Math.pow(paletteColor.getRed()-(int) ((color.getRed()-0.3*color.getRed()) + r*(color.getRed()+0.3*color.getRed())),2) +
//                Math.pow(paletteColor.getGreen()-(int) ((color.getGreen()-0.3*color.getGreen()) + r*(color.getGreen()+0.3*color.getGreen())),2) +
//                Math.pow(paletteColor.getBlue()-(int) ((color.getBlue()-0.3*color.getBlue()) + r*(color.getBlue()+0.3*color.getBlue())),2)
//        );
    }

    public int getaRate() {
        return aRate;
    }

    public void setaRate(int aRate) {
        this.aRate = aRate;
    }

    public int getAlfaRate() {
        return alfaRate;
    }

    public void setAlfaRate(int alfaRate) {
        this.alfaRate = alfaRate;
    }

    public int getBetaRate() {
        return betaRate;
    }

    public void setBetaRate(int betaRate) {
        this.betaRate = betaRate;
    }
}
