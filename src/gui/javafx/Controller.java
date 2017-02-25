package gui.javafx;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import photo.processor.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by marek on 2017-02-14.
 */
public class Controller {

    private boolean isImageSelected = false;
    FileChooser.ExtensionFilter[] filters = {
            new FileChooser.ExtensionFilter("Image (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif", "*.jpeg",
                    "*.PNG", "*.JPG", "*.GIF", "*.JPEG"),
            new FileChooser.ExtensionFilter(".png", "*.png", "*.PNG"),
            new FileChooser.ExtensionFilter(".jpg", "*.jpg", "*.JPG", "*.jpeg", "*.JPEG"),
            new FileChooser.ExtensionFilter(".gif", "*.gif", "*.GIF")
    };

    private Stage window;
    private Image beforeImg;
    private Image afterImg;
    private PhotoProcessor processor;
    private Color[] colorPalette;

    public ScrollPane leftSP;
    public ScrollPane rightSP;
    public StackPane leftStackPImg;
    public StackPane rightStackPImg;
    public ImageView beforeImageView;
    public ImageView afterImageView;

    public ToggleGroup beforeTG;
    public RadioButton beforeOSizeRB;
    public RadioButton beforeFitToWRB;
    public ToggleGroup afterTG;
    public RadioButton afterOSizeRB;
    public RadioButton afterFitToWRB;

    public VBox center;
    public ChoiceBox algorithmChoiceBox;
    public ChoiceBox paletteChoiceBox;
    public Button submitButton;

    public void setStageAndSetupView(Stage primaryStage) {
        window = primaryStage;
        leftSP.prefWidthProperty().bind(window.widthProperty().subtract(center.getPrefWidth()+70).divide(2));
        leftStackPImg.prefWidthProperty().bind(leftSP.widthProperty().subtract(10));
        leftStackPImg.prefHeightProperty().bind(leftSP.heightProperty().subtract(10));
        rightSP.prefWidthProperty().bind(window.widthProperty().subtract(center.getPrefWidth()+70).divide(2));
        rightStackPImg.prefWidthProperty().bind(rightSP.widthProperty().subtract(10));
        rightStackPImg.prefHeightProperty().bind(rightSP.heightProperty().subtract(10));

        paletteChoiceBox.prefWidthProperty().bind(algorithmChoiceBox.widthProperty());
        submitButton.prefWidthProperty().bind(algorithmChoiceBox.widthProperty());

        beforeTG = new ToggleGroup();
        beforeOSizeRB.setToggleGroup(beforeTG);
        beforeFitToWRB.setToggleGroup(beforeTG);
        beforeFitToWRB.setSelected(true);

        afterTG = new ToggleGroup();
        afterOSizeRB.setToggleGroup(afterTG);
        afterFitToWRB.setToggleGroup(afterTG);
        afterFitToWRB.setSelected(true);

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

    public void openFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(filters);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            if (checkFileExtension(file)) {
                isImageSelected = true;
                beforeImg = new Image(file.toURI().toString());
                displayBeforeImage();
            } else {
                new Alert(Alert.AlertType.ERROR, file.getName() + " has no valid file-extension").show();
            }
        }
    }

    public void saveFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(filters);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName("*.png");
        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            try {
                if (checkFileExtension(file)){
                    ImageIO.write(SwingFXUtils.fromFXImage(afterImg, null), file.getName().substring(
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

    public void displayBeforeImage() {
        if (isImageSelected)
            beforeImageView.setImage(beforeImg);
    }

    public void displayAfterImage() {
        afterImageView.setImage(afterImg);
    }

    public void mainProcessSubmitted(ActionEvent actionEvent) {
        if (isImageSelected) {
            selectPhotoProcessor();
            selectColorPalette();
            afterImg = SwingFXUtils.toFXImage(processor.getTransformedImage(SwingFXUtils.fromFXImage(beforeImg, null),
                    colorPalette), null);
            displayAfterImage();
        }
    }

    private void selectColorPalette() {
        switch (paletteChoiceBox.getSelectionModel().getSelectedItem().toString()) {
            case "2": colorPalette = PhotoProcessor.TWO_COLORS_PALETTE; break;
            case "8": colorPalette = PhotoProcessor.EIGHT_COLORS_PALETTE; break;
            case "27": colorPalette = PhotoProcessor.TWENTY_SEVEN_COLORS_PALETTE; break;
        }
    }

    private void selectPhotoProcessor() {
        switch (algorithmChoiceBox.getSelectionModel().getSelectedItem().toString()) {
            case "Nearest Color": processor = new NearestColor(); break;
            case "NC with Noise Signal": processor = new SignalNoiseApproximation(); break;
            case "Floyd-Steinberg Dithering": processor = new FloydSteinbergDithering(); break;
            case "Fan Dithering": processor = new FanDithering(); break;
            case "Burke Dithering": processor = new BurkeDithering(); break;
            case "Jarvis, Judice, Ninke Dithering": processor = new JaJuNiDithering(); break;
        }
    }

    public void closeProgram(ActionEvent actionEvent) {
        window.close();
    }
}
