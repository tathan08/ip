package jinjja.javafxui.window;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jinjja.Jinjja;
import jinjja.javafxui.DialogBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Jinjja jinjja;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/userLogo.jpeg"));
    private Image jinjjaImage = new Image(this.getClass().getResourceAsStream("/images/jinjjaLogo.jpeg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Jinjja instance */
    public void setJinjja(Jinjja j) {
        jinjja = j;
    }

    /**
     * Creates a dialog box containing user input, and appends it to the dialog container. Clears the user input after
     * processing.
     */
    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        String jinjjaText = jinjja.getResponse(userText);
        dialogContainer.getChildren().addAll(
            DialogBox.getUserDialog(userInput.getText(), userImage),
            DialogBox.getJinjjaDialog(jinjjaText, jinjjaImage)
        );
        userInput.clear();
    }
}
