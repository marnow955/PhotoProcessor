package gui.javafx;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import photo.processor.*;

import java.awt.*;
import java.io.File;

/**
 * Created by marek on 2017-02-14.
 */
public class Controller {

    private boolean isImageSelected = false;

    private Stage window;
    private Image beforeImg;
    private Image afterImg;
    private PhotoProcessor processor;
    private Color[] colorPalette;

    public ScrollPane leftSP;
    public ScrollPane rightSP;
    public VBox center;
    public ChoiceBox algorithmChoiceBox;
    public ChoiceBox paletteChoiceBox;
    public Button submitButton;
    public ImageView beforeImageView;
    public ImageView afterImageView;

    public ToggleGroup beforeTG;
    public RadioButton beforeOSizeRB;
    public RadioButton beforeFitToWRB;
    public ToggleGroup afterTG;
    public RadioButton afterOSizeRB;
    public RadioButton afterFitToWRB;

    public void setStageAndSetupView(Stage primaryStage) {
        window = primaryStage;
        leftSP.prefWidthProperty().bind(window.widthProperty().subtract(center.getPrefWidth()+70).divide(2));
        rightSP.prefWidthProperty().bind(window.widthProperty().subtract(center.getPrefWidth()+70).divide(2));
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
//        afterOSizeRB.layoutXProperty().bind(rightSP.layoutXProperty());
        afterOSizeRB.setLayoutX(rightSP.getLayoutX());

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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".png, .jpg, .jpeg", "*.png", "*.jpg",
                "*.jpeg"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            isImageSelected = true;
            beforeImg = new Image(file.toURI().toString());
            displayBeforeImage();
        }
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
