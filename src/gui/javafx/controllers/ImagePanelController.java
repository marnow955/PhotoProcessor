package gui.javafx.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;

/**
 * Created by marnow955 on 2017-04-15.
 */
public class ImagePanelController {

    private BooleanProperty isImageFitToWindowProperty = new SimpleBooleanProperty(false);
    private BooleanProperty isZoom = new SimpleBooleanProperty(false);
    private DoubleProperty zoomProperty = new SimpleDoubleProperty(200);

    public ScrollPane scrollPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private ImageView imageView;

    public void initialize() {
        isImageFitToWindowProperty.addListener((observable, oldValue, newValue) -> setImageFitToWindow(newValue));

        zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                imageView.setFitWidth(zoomProperty.get() * 4);
                imageView.setFitHeight(zoomProperty.get() * 3);
            }
        });

        scrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                isZoom.set(true);
                imageView.fitWidthProperty().unbind();
                imageView.fitHeightProperty().unbind();
                if (event.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                } else if (event.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                }
            }
        });
    }

    void setupView() {
        stackPane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(2));
        stackPane.prefHeightProperty().bind(scrollPane.heightProperty().subtract(2));
    }

    public Image getImage() {
        return imageView.getImage();
    }

    public void setImage(Image image) {
        imageView.setPreserveRatio(true);
        imageView.setImage(image);
    }

    public boolean isImageFitToWindow() {
        return isImageFitToWindowProperty.get();
    }

    public BooleanProperty isImageFitToWindowProperty() {
        return isImageFitToWindowProperty;
    }

    public void setImageFitToWindow(boolean value) {
        zoomProperty.setValue(200);
        this.isImageFitToWindowProperty.set(value);
        if (isImageFitToWindowProperty.get()) {
            imageView.fitWidthProperty().bind(stackPane.widthProperty());
            imageView.fitHeightProperty().bind(stackPane.heightProperty());
        } else {
            imageView.fitWidthProperty().unbind();
            imageView.fitHeightProperty().unbind();
            imageView.setFitWidth(getImage().getWidth());
            imageView.setFitHeight(getImage().getHeight());
        }
        isZoom.set(false);
    }

    public boolean isZoom() {
        return isZoom.get();
    }

    public BooleanProperty isZoomProperty() {
        return isZoom;
    }

}
