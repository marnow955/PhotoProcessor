package gui.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by marek on 2017-02-14.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStageAndSetupView(primaryStage);
        Scene scene = new Scene(root,800, 500);
        scene.getStylesheets().add(getClass().getResource("MainViewCSS.css").toExternalForm());
        primaryStage.setTitle("Photo Processor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
