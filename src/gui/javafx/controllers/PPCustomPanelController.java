package gui.javafx.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import photo.processor.*;

import java.awt.*;

/**
 * Created by marnow955 on 2017-04-15.
 */
public class PPCustomPanelController {

    @FXML
    private VBox container;
    private MainController mainController;

    @FXML
    private ChoiceBox<String> algorithmChoiceBox;
    @FXML
    private ChoiceBox<String> paletteChoiceBox;
    @FXML
    private ScrollPane imagePanel;
    @FXML
    private ImagePanelController imagePanelController;

    private final ContextMenu contextMenu = new ContextMenu();
    private Image image;
    private PhotoProcessor processor;
    private Color[] colorPalette;

    void setupView() {
        imagePanel.prefHeightProperty().bind(container.heightProperty());
        imagePanelController.setupView();

        setupChoiceBoxes();
        createContextMenu();
    }

    void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }

    void updateImage(Image originalImage) {
        image = originalImage;
        imagePanelController.setImage(image);
    }

    Image getImage() {
        return imagePanelController.getImage();
    }

    public void mainProcessSubmitted() {
        if (mainController.isImageSelectedProperty.get()) {
            if (algorithmChoiceBox.getSelectionModel().getSelectedItem().equals("Original")) {
                image = mainController.getOriginalImage();
            } else {
                selectPhotoProcessor();
                selectColorPalette();
                image = SwingFXUtils.toFXImage(processor.getTransformedImage(SwingFXUtils.fromFXImage(mainController
                                .getOriginalImage(), null),
                        colorPalette), null);
            }
            imagePanelController.setImage(image);
        }
    }

    private void selectColorPalette() {
        switch (paletteChoiceBox.getSelectionModel().getSelectedItem()) {
            case "2":
                colorPalette = PhotoProcessor.TWO_COLORS_PALETTE;
                break;
            case "8":
                colorPalette = PhotoProcessor.EIGHT_COLORS_PALETTE;
                break;
            case "27":
                colorPalette = PhotoProcessor.TWENTY_SEVEN_COLORS_PALETTE;
                break;
        }
    }

    private void selectPhotoProcessor() {
        switch (algorithmChoiceBox.getSelectionModel().getSelectedItem()) {
            case "Nearest Color":
                processor = new NearestColor();
                break;
            case "NC with Noise Signal":
                processor = new SignalNoiseApproximation();
                break;
            case "Floyd-Steinberg Dithering":
                processor = new FloydSteinbergDithering();
                break;
            case "Fan Dithering":
                processor = new FanDithering();
                break;
            case "Burke Dithering":
                processor = new BurkeDithering();
                break;
            case "Jarvis, Judice, Ninke Dithering":
                processor = new JaJuNiDithering();
                break;
        }
    }

    private void setupChoiceBoxes() {
        algorithmChoiceBox.disableProperty().bind(mainController.isImageSelectedProperty.not());
        algorithmChoiceBox.getItems().addAll(
                "Original",
                "Nearest Color",
                "NC with Noise Signal",
                "Floyd-Steinberg Dithering",
                "Fan Dithering",
                "Burke Dithering",
                "Jarvis, Judice, Ninke Dithering"
        );
        algorithmChoiceBox.getSelectionModel().selectFirst();

        paletteChoiceBox.disableProperty().bind(algorithmChoiceBox.getSelectionModel().selectedItemProperty()
                .isEqualTo("Original"));
        paletteChoiceBox.getItems().addAll("2", "8", "27");
        paletteChoiceBox.setValue("27");
    }

    private void createContextMenu() {
        RadioMenuItem originalSizeItem = new RadioMenuItem("Original size");
        RadioMenuItem fitToWindowItem = new RadioMenuItem("Fit to window");
        ToggleGroup sizeTG = new ToggleGroup();
        sizeTG.getToggles().addAll(originalSizeItem, fitToWindowItem);
        MenuItem openFileItem = new MenuItem("Open file...");
        openFileItem.setOnAction(event -> mainController.openFile());
        MenuItem saveFileItem = new MenuItem("Save file...");
        saveFileItem.setOnAction(event -> mainController.saveFile());
        contextMenu.getItems().addAll(originalSizeItem, fitToWindowItem, new SeparatorMenuItem(), openFileItem,
                saveFileItem);

        imagePanel.setContextMenu(contextMenu);
    }
}
