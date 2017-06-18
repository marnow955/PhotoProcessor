package gui.javafx.controllers;

import gui.javafx.OpenSaveImageDialog;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by marnow955 on 2017-04-15.
 */
public class MainController {
    // TODO: save file disable, name of file label, save multiple files...
    // TODO: percent of zoom
    // TODO: Ctrl+S

    BooleanProperty isImageSelectedProperty = new SimpleBooleanProperty(false);

    private Stage window;
    private Image originalImage;

    @FXML
    private HBox leftPanel;
    @FXML
    private Button leftPanelButton;
    @FXML
    private VBox leftCustomPanel;
    @FXML
    private PPCustomPanelController leftCustomPanelController;
    @FXML
    private VBox mainCustomPanel;
    @FXML
    private PPCustomPanelController mainCustomPanelController;
    @FXML
    private MenuItem saveFileMenuItem;

    public void setStageAndSetupView(Stage primaryStage) {
        window = primaryStage;
        leftCustomPanelController.injectMainController(this);
        mainCustomPanelController.injectMainController(this);

        leftCustomPanel.prefWidthProperty().bind(window.widthProperty().subtract(30).divide(2));
        leftPanelButton.prefHeightProperty().bind(leftPanel.heightProperty());
        leftPanelButton.disableProperty().bind(isImageSelectedProperty.not());
        leftPanelButton.setText("\u25C0");
        leftCustomPanel.managedProperty().bind(leftCustomPanel.visibleProperty());
        showLeftPanel();

        leftCustomPanelController.setupView();
        mainCustomPanelController.setupView();
        saveFileMenuItem.disableProperty().bind(isImageSelectedProperty.not());
        saveFileMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
    }

    public void openFile() {
        originalImage = new OpenSaveImageDialog().openImage(window);
        if (originalImage != null) {
            isImageSelectedProperty.set(true);
            mainCustomPanelController.setImage(originalImage);
            leftCustomPanelController.setImage(originalImage);
        }
    }

    public void saveFile() {
        if (isImageSelectedProperty.get()) {
            new OpenSaveImageDialog().saveImage(mainCustomPanelController.getImage(), window);
        }
    }

    public void closeProgram() {
        window.close();
    }

    public void showLeftPanel() {
        leftCustomPanel.setVisible(!leftCustomPanel.isVisible());
        if (leftPanelButton.getText().equals("\u25C0"))
            leftPanelButton.setText("\u25B6");
        else
            leftPanelButton.setText("\u25C0");
    }

    Image getOriginalImage() {
        return originalImage;
    }

    public void clearWorkspace() {
        // TODO: .
        originalImage = null;
        isImageSelectedProperty.set(false);
        mainCustomPanelController.setImage(null);
        leftCustomPanelController.setImage(null);
    }
}
