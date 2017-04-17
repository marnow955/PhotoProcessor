package gui.javafx;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by marnow955 on 2017-04-17.
 */
public class OpenSaveImageDialog {
    private FileChooser.ExtensionFilter[] filters = {
            new FileChooser.ExtensionFilter("Image (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif", "*.jpeg",
                    "*.PNG", "*.JPG", "*.GIF", "*.JPEG"),
            new FileChooser.ExtensionFilter(".png", "*.png", "*.PNG"),
            new FileChooser.ExtensionFilter(".jpg", "*.jpg", "*.JPG", "*.jpeg", "*.JPEG"),
            new FileChooser.ExtensionFilter(".gif", "*.gif", "*.GIF")
    };

    private boolean checkFileExtension(File file) {
        for (String filter: filters[0].getExtensions()) {
            if (file.getName().endsWith(filter.substring(1))) {
                return true;
            }
        }
        return false;
    }

    public Image openImage(Stage displayWindow){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(filters);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showOpenDialog(displayWindow);
        if (file != null) {
            if (checkFileExtension(file)) {
                return new Image(file.toURI().toString());
            } else {
                new Alert(Alert.AlertType.ERROR, file.getName() + " has no valid file-extension").show();
            }
        }
        return null;
    }

    public void saveImage(Image image, Stage displayWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(filters);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialFileName("*.png");
        File file = fileChooser.showSaveDialog(displayWindow);
        if (file != null) {
            try {
                if (checkFileExtension(file)){
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), file.getName().substring(
                            file.getName().lastIndexOf(".")+1), file);
                } else {
                    new Alert(Alert.AlertType.ERROR, file.getName() + " has no valid file-extension").show();
                }
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, String.format("Cannot save file %s", file.getPath())).show();
            }
        }
    }
}
