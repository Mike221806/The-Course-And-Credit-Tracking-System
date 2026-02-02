package com.university;

/**
 * Simple launcher that handles JavaFX module requirements for standalone JAR.
 * This ensures the application can run with just java -jar without additional arguments.
 */
public class Launcher {
    public static void main(String[] args) {
        // Add JavaFX modules and native access flags before starting the real app
        System.setProperty("javafx.modules", "javafx.controls,javafx.fxml,javafx.graphics");
        System.setProperty("jdk.module.addExports", "javafx.graphics/com.sun.javafx.application=ALL-UNNAMED");

        // Launch the main application
        App.main(args);
    }
}
