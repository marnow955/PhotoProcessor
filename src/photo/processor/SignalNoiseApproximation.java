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

    private boolean isRandom = false;

    private double randomNoisePercent = 0.1;
    private int aRate = 52;
    private int alfaRate = 128;
    private int betaRate = 128;

    @Override
    public BufferedImage getTransformedImage(BufferedImage image, Color[] paletteOfColors) {
        init(image, paletteOfColors);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int resultColorIndex = findNearestPaletteColor(new Color(image.getRGB(x, y)), x, y);
                resultImg.setRGB(x, y, palette[resultColorIndex].getRGB());
            }
        }

        return resultImg;
    }

    private void init(BufferedImage image, Color[] paletteOfColors) {
        this.palette = paletteOfColors;
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        resultImg = new BufferedImage(width, height, image.getType());
    }

    private int findNearestPaletteColor(Color color, int x, int y) {
        int nearestColorIndex = 0;

        for (int i = 1; i < palette.length; i++) {
            if (diff(palette[i], color, x, y) < diff(palette[nearestColorIndex], color, x, y))
                nearestColorIndex = i;
        }

        return nearestColorIndex;
    }

    private int diff(Color paletteColor, Color color, int x, int y) {
        if (isRandom) {
            double r = Math.random();
            return (int) (
                    Math.pow(paletteColor.getRed() - (int) ((color.getRed() - randomNoisePercent * color.getRed()) + r * (color.getRed() + randomNoisePercent * color.getRed())), 2) +
                    Math.pow(paletteColor.getGreen() - (int) ((color.getGreen() - randomNoisePercent * color.getGreen()) + r * (color.getGreen() + randomNoisePercent * color.getGreen())), 2) +
                    Math.pow(paletteColor.getBlue() - (int) ((color.getBlue() - randomNoisePercent * color.getBlue()) + r * (color.getBlue() + randomNoisePercent * color.getBlue())), 2)
            );
        } else {
            double noiseRate = aRate * Math.sin(alfaRate * x) * Math.sin(betaRate * y);
            return (int) (
                    Math.pow(paletteColor.getRed() - color.getRed() + noiseRate, 2) +
                    Math.pow(paletteColor.getGreen() - color.getGreen() + noiseRate, 2) +
                    Math.pow(paletteColor.getBlue() - color.getBlue() + noiseRate, 2)
            );
        }
    }


    public boolean isRandom() {
        return isRandom;
    }

    public void setRandom(boolean random) {
        isRandom = random;
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

    public double getRandomNoisePercent() {
        return randomNoisePercent;
    }

    public void setRandomNoisePercent(double randomNoisePercent) {
        if (randomNoisePercent >= 0 && randomNoisePercent <= 1)
            this.randomNoisePercent = randomNoisePercent;
    }
}
