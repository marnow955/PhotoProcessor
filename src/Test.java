import photo.processor.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by marek on 2017-01-06.
 */
public class Test {

    public static void main(String[] args) throws IOException {
        File file = new File("Lenna.png");
        final BufferedImage normal = ImageIO.read(file).getSubimage(0,0,500,500);

        PhotoProcessor fS = new SignalNoiseApproximation();
        final BufferedImage resultImg = fS.getTransformedImage(normal,PhotoProcessor.TWO_COLORS_PALETTE);

        JFrame frame = new JFrame("Test");
        frame.setLayout(new GridLayout(1, 2));

        frame.add(new JComponent() {
            private static final long serialVersionUID = 2963702769416707676L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(normal, 0, 0, this);
            }
        });

        frame.add(new JComponent() {
            private static final long serialVersionUID = -6919658458441878769L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(resultImg, 0, 0, this);
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 600);
        frame.setVisible(true);
    }
}
