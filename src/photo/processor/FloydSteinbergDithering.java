package photo.processor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by marek on 2017-01-06.
 */
public class FloydSteinbergDithering implements PhotoProcessor {

    private Color[] palette;

    private BufferedImage image;
    private BufferedImage resultImg;

    private int width;
    private int height;

    protected RGBErrors[][] errorTab;
    private RGBErrors currentError;

    @Override
    public BufferedImage getTransformedImage(BufferedImage image, Color[] paletteOfColors) {
        init(image, paletteOfColors);

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int resultColorIndex = findNearestPaletteColor(calculateColor(image.getRGB(x,y),errorTab[x][y]));
                resultImg.setRGB(x,y,palette[resultColorIndex].getRGB());
                currentError = subColors(calculateColor(image.getRGB(x,y),errorTab[x][y]),palette[resultColorIndex]);

                propagation(x,y,currentError);
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

        errorTab = new RGBErrors[width][height];
        for (int y=0; y<height; y++)
            for (int x=0; x<width; x++)
                errorTab[x][y] = new RGBErrors(Color.BLACK);
    }

    protected void propagation(int x, int y, RGBErrors currentError) {
        if (x<width-1)
            errorTab[x+1][y] = new RGBErrors(
                    errorTab[x+1][y].getRedError()+(int)((7./16)*currentError.getRedError()),
                    errorTab[x+1][y].getGreenError()+(int)((7./16)*currentError.getGreenError()),
                    errorTab[x+1][y].getBlueError()+(int)((7./16)*currentError.getBlueError())
            );
        if (x>0 && y<height-1)
            errorTab[x-1][y+1] = new RGBErrors(
                    errorTab[x-1][y+1].getRedError()+(int)((3./16)*currentError.getRedError()),
                    errorTab[x-1][y+1].getGreenError()+(int)((3./16)*currentError.getGreenError()),
                    errorTab[x-1][y+1].getBlueError()+(int)((3./16)*currentError.getBlueError())
            );

        if (y<height-1)
            errorTab[x][y+1] = new RGBErrors(
                    errorTab[x][y+1].getRedError()+(int)((5./16)*currentError.getRedError()),
                    errorTab[x][y+1].getGreenError()+(int)((5./16)*currentError.getGreenError()),
                    errorTab[x][y+1].getBlueError()+(int)((5./16)*currentError.getBlueError())
            );

        if (x<width-1 && y<height-1)
            errorTab[x+1][y+1] = new RGBErrors(
                    errorTab[x+1][y+1].getRedError()+(int)((1./16)*currentError.getRedError()),
                    errorTab[x+1][y+1].getGreenError()+(int)((1./16)*currentError.getGreenError()),
                    errorTab[x+1][y+1].getBlueError()+(int)((1./16)*currentError.getBlueError())
            );
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

    private Color calculateColor(int colorRGB, RGBErrors currentError) {
        Color color = new Color(colorRGB);
        int r = validate(color.getRed() + currentError.getRedError());
        int g = validate(color.getGreen() + currentError.getGreenError());
        int b = validate(color.getBlue() + currentError.getBlueError());

        return new Color(r,g,b);
    }

    private int validate(int rgbValue) {
        if (rgbValue<0)
            return 0;
        else if(rgbValue>255)
            return 255;
        else
            return rgbValue;
    }

    private RGBErrors subColors(Color minuend, Color subtrahend) {
        return new RGBErrors(
                minuend.getRed() - subtrahend.getRed(),
                minuend.getGreen() - subtrahend.getGreen(),
                minuend.getBlue() - subtrahend.getBlue()
        );
    }

    protected int getWidth() {
        return width;
    }

    protected int getHeight() {
        return height;
    }
}
