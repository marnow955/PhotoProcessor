package photo.processor;

/**
 * Created by marek on 2017-01-08.
 */
public class JaJuNiDithering extends FloydSteinbergDithering {

    @Override
    protected void propagation(int x, int y, RGBErrors currentError) {
        if (x<getWidth()-1)
            errorTab[x+1][y] = new RGBErrors(
                    errorTab[x+1][y].getRedError()+(int)((7./48)*currentError.getRedError()),
                    errorTab[x+1][y].getGreenError()+(int)((7./48)*currentError.getGreenError()),
                    errorTab[x+1][y].getBlueError()+(int)((7./48)*currentError.getBlueError())
            );

        if (x<getWidth()-2)
            errorTab[x+2][y] = new RGBErrors(
                    errorTab[x+2][y].getRedError()+(int)((5./48)*currentError.getRedError()),
                    errorTab[x+2][y].getGreenError()+(int)((5./48)*currentError.getGreenError()),
                    errorTab[x+2][y].getBlueError()+(int)((5./48)*currentError.getBlueError())
            );






        if (x>1 && y<getHeight()-1)
            errorTab[x-2][y+1] = new RGBErrors(
                    errorTab[x-2][y+1].getRedError()+(int)((3./48)*currentError.getRedError()),
                    errorTab[x-2][y+1].getGreenError()+(int)((3./48)*currentError.getGreenError()),
                    errorTab[x-2][y+1].getBlueError()+(int)((3./48)*currentError.getBlueError())
            );

        if (x>0 && y<getHeight()-1)
            errorTab[x-1][y+1] = new RGBErrors(
                    errorTab[x-1][y+1].getRedError()+(int)((5./48)*currentError.getRedError()),
                    errorTab[x-1][y+1].getGreenError()+(int)((5./48)*currentError.getGreenError()),
                    errorTab[x-1][y+1].getBlueError()+(int)((5./48)*currentError.getBlueError())
            );

        if (y<getHeight()-1)
            errorTab[x][y+1] = new RGBErrors(
                    errorTab[x][y+1].getRedError()+(int)((7./48)*currentError.getRedError()),
                    errorTab[x][y+1].getGreenError()+(int)((7./48)*currentError.getGreenError()),
                    errorTab[x][y+1].getBlueError()+(int)((7./48)*currentError.getBlueError())
            );

        if (x<getWidth()-1 && y<getHeight()-1)
            errorTab[x+1][y+1] = new RGBErrors(
                    errorTab[x+1][y+1].getRedError()+(int)((5./48)*currentError.getRedError()),
                    errorTab[x+1][y+1].getGreenError()+(int)((5./48)*currentError.getGreenError()),
                    errorTab[x+1][y+1].getBlueError()+(int)((5./48)*currentError.getBlueError())
            );

        if (x<getWidth()-2 && y<getHeight()-1)
            errorTab[x+2][y+1] = new RGBErrors(
                    errorTab[x+2][y+1].getRedError()+(int)((3./48)*currentError.getRedError()),
                    errorTab[x+2][y+1].getGreenError()+(int)((3./48)*currentError.getGreenError()),
                    errorTab[x+2][y+1].getBlueError()+(int)((3./48)*currentError.getBlueError())
            );







        if (x>1 && y<getHeight()-2)
            errorTab[x-2][y+2] = new RGBErrors(
                    errorTab[x-2][y+2].getRedError()+(int)((1./48)*currentError.getRedError()),
                    errorTab[x-2][y+2].getGreenError()+(int)((1./48)*currentError.getGreenError()),
                    errorTab[x-2][y+2].getBlueError()+(int)((1./48)*currentError.getBlueError())
            );

        if (x>0 && y<getHeight()-2)
            errorTab[x-1][y+2] = new RGBErrors(
                    errorTab[x-1][y+2].getRedError()+(int)((3./48)*currentError.getRedError()),
                    errorTab[x-1][y+2].getGreenError()+(int)((3./48)*currentError.getGreenError()),
                    errorTab[x-1][y+2].getBlueError()+(int)((3./48)*currentError.getBlueError())
            );

        if (y<getHeight()-2)
            errorTab[x][y+2] = new RGBErrors(
                    errorTab[x][y+2].getRedError()+(int)((5./48)*currentError.getRedError()),
                    errorTab[x][y+2].getGreenError()+(int)((5./48)*currentError.getGreenError()),
                    errorTab[x][y+2].getBlueError()+(int)((5./48)*currentError.getBlueError())
            );

        if (x<getWidth()-1 && y<getHeight()-2)
            errorTab[x+1][y+2] = new RGBErrors(
                    errorTab[x+1][y+2].getRedError()+(int)((3./48)*currentError.getRedError()),
                    errorTab[x+1][y+2].getGreenError()+(int)((3./48)*currentError.getGreenError()),
                    errorTab[x+1][y+2].getBlueError()+(int)((3./48)*currentError.getBlueError())
            );

        if (x<getWidth()-2 && y<getHeight()-2)
            errorTab[x+2][y+2] = new RGBErrors(
                    errorTab[x+2][y+2].getRedError()+(int)((1./48)*currentError.getRedError()),
                    errorTab[x+2][y+2].getGreenError()+(int)((1./48)*currentError.getGreenError()),
                    errorTab[x+2][y+2].getBlueError()+(int)((1./48)*currentError.getBlueError())
            );
    }
}
