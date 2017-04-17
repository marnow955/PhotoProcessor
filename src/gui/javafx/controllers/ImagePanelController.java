package gui.javafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Created by marnow955 on 2017-04-15.
 */
public class ImagePanelController {
    public ScrollPane scrollPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private ImageView imageView;

    void setupView() {
        stackPane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(2));
        stackPane.prefHeightProperty().bind(scrollPane.heightProperty().subtract(2));
    }

    public Image getImage() {
        return imageView.getImage();
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }
}
