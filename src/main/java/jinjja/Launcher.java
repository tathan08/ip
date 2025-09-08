package jinjja;

import javafx.application.Application;
import jinjja.javafxui.JavaFxUi;

/**
 * A launcher class to workaround classpath issues.
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(JavaFxUi.class, args);
    }
}
