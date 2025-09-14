package jinjja.javafxui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jinjja.Jinjja;
import jinjja.javafxui.window.MainWindow;

/**
 * JavaFX UI for Jinjja chatbot.
 */
public class JavaFxUi extends Application {

    private Jinjja jinjja = new Jinjja(true);

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Jinjja");

            MainWindow controller = fxmlLoader.<MainWindow>getController();
            controller.setJinjja(jinjja); // inject the Jinjja instance

            // Handle window close request
            stage.setOnCloseRequest(event-> {
                // Save tasks and show farewell message before closing
                String farewellMessage = jinjja.shutdown();
                System.out.println(farewellMessage); // Print to console for debugging
                // The window will close automatically after this handler completes
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
