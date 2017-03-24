package gui.javafx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import photo.processor.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by marek on 2017-02-14.
 */
public class Controller {

    private BooleanProperty isImageSelected = new SimpleBooleanProperty(false);
//    private boolean isImageSelected = false;
    private FileChooser.ExtensionFilter[] filters = {
            new FileChooser.ExtensionFilter("Image (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif", "*.jpeg",
                    "*.PNG", "*.JPG", "*.GIF", "*.JPEG"),
            new FileChooser.ExtensionFilter(".png", "*.png", "*.PNG"),
            new FileChooser.ExtensionFilter(".jpg", "*.jpg", "*.JPG", "*.jpeg", "*.JPEG"),
            new FileChooser.ExtensionFilter(".gif", "*.gif", "*.GIF")
    };

    private Stage window;
    private Image originalImg;
    private Image mainImg;
    private PhotoProcessor processor;
    private Color[] colorPalette;

    public HBox leftHBox;
    public ScrollPane leftSP;
    public ScrollPane mainSP;
    public StackPane leftStackPImg;
    public StackPane mainStackPImg;
    public ImageView originalImageView;
    public ImageView mainImageView;
    public Button leftPanelButton;

    public ChoiceBox<String> algorithmChoiceBox;
    public ChoiceBox<String> paletteChoiceBox;
    public Button submitButton;

    void setStageAndSetupView(Stage primaryStage) {
        window = primaryStage;

        leftSP.prefWidthProperty().bind(window.widthProperty().subtract(30).divide(2));
        leftStackPImg.prefWidthProperty().bind(leftSP.widthProperty().subtract(2));
        leftStackPImg.prefHeightProperty().bind(leftSP.heightProperty().subtract(2));
        leftPanelButton.prefHeightProperty().bind(leftHBox.heightProperty());
        leftPanelButton.disableProperty().bind(isImageSelected.not());
        leftPanelButton.setText("\u25C0");
        leftSP.managedProperty().bind(leftSP.visibleProperty());
        showLeftPanel();    // Hide left panel
//        rightSP.prefWidthProperty().bind(window.widthProperty().subtract(center.getPrefWidth()+70).divide(2));
        mainStackPImg.prefWidthProperty().bind(mainSP.widthProperty().subtract(2));
        mainStackPImg.prefHeightProperty().bind(mainSP.heightProperty().subtract(2));

        paletteChoiceBox.prefWidthProperty().bind(algorithmChoiceBox.widthProperty());
        submitButton.prefWidthProperty().bind(algorithmChoiceBox.widthProperty());

        algorithmChoiceBox.getItems().addAll(
                "Nearest Color",
                "NC with Noise Signal",
                "Floyd-Steinberg Dithering",
                "Fan Dithering",
                "Burke Dithering",
                "Jarvis, Judice, Ninke Dithering"
        );
        algorithmChoiceBox.setValue("Floyd-Steinberg Dithering");

        paletteChoiceBox.getItems().addAll("2", "8", "27");
        paletteChoiceBox.setValue("27");
    }

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(filters);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            if (checkFileExtension(file)) {
                isImageSelected.set(true);
                originalImg = new Image(file.toURI().toString());
                mainImg = originalImg;
                displayOriginalImage();
                displayMainImage();
            } else {
                new Alert(Alert.AlertType.ERROR, file.getName() + " has no valid file-extension").show();
            }
        }
    }

    public void saveFile() {
//        TODO: check if is file to save
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(filters);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName("*.png");
        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            try {
                if (checkFileExtension(file)){
                    ImageIO.write(SwingFXUtils.fromFXImage(mainImg, null), file.getName().substring(
                            file.getName().lastIndexOf(".")+1), file);
                } else {
                    new Alert(Alert.AlertType.ERROR, file.getName() + " has no valid file-extension").show();
                }
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, String.format("Cannot save file %s", file.getPath())).show();
            }
        }
    }

    private boolean checkFileExtension(File file) {
        for (String filter: filters[0].getExtensions()) {
            if (file.getName().endsWith(filter.substring(1))) {
                return true;
            }
        }
        return false;
    }

    private void displayOriginalImage() {
        if (isImageSelected.get())
            originalImageView.setImage(originalImg);
    }

    private void displayMainImage() {
        mainImageView.setImage(mainImg);
    }

    public void mainProcessSubmitted() {
        if (isImageSelected.get()) {
            selectPhotoProcessor();
            selectColorPalette();
            mainImg = SwingFXUtils.toFXImage(processor.getTransformedImage(SwingFXUtils.fromFXImage(originalImg, null),
                    colorPalette), null);
            displayMainImage();
        }
    }

    private void selectColorPalette() {
        switch (paletteChoiceBox.getSelectionModel().getSelectedItem()) {
            case "2": colorPalette = PhotoProcessor.TWO_COLORS_PALETTE; break;
            case "8": colorPalette = PhotoProcessor.EIGHT_COLORS_PALETTE; break;
            case "27": colorPalette = PhotoProcessor.TWENTY_SEVEN_COLORS_PALETTE; break;
        }
    }

    private void selectPhotoProcessor() {
        switch (algorithmChoiceBox.getSelectionModel().getSelectedItem()) {
            case "Nearest Color": processor = new NearestColor(); break;
            case "NC with Noise Signal": processor = new SignalNoiseApproximation(); break;
            case "Floyd-Steinberg Dithering": processor = new FloydSteinbergDithering(); break;
            case "Fan Dithering": processor = new FanDithering(); break;
            case "Burke Dithering": processor = new BurkeDithering(); break;
            case "Jarvis, Judice, Ninke Dithering": processor = new JaJuNiDithering(); break;
        }
    }

    public void closeProgram() {
        window.close();
    }

    public void showLeftPanel() {
        leftSP.setVisible(!leftSP.isVisible());
        if (leftPanelButton.getText().equals("\u25C0"))
            leftPanelButton.setText("\u25B6");
        else
            leftPanelButton.setText("\u25C0");
    }
}
