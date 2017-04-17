package gui.javafx.controllers;

import gui.javafx.OpenSaveImageDialog;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by marnow955 on 2017-04-15.
 */
public class MainController {
    // TODO: save file disable, name of file label, save multiple files...
    // TODO: setDisableWhileNULL

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
    }

    public void openFile() {
        // TODO: set original size and then display
        originalImage = new OpenSaveImageDialog().openImage(window);
        if (originalImage != null) {
            isImageSelectedProperty.set(true);
            mainCustomPanelController.updateImage(originalImage);
            leftCustomPanelController.updateImage(originalImage);
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
}
