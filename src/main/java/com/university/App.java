package com.university;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.university.data.DatabaseInitializer;

/**
 * Main application class for the Course & Credit Tracking System.
 * Initializes the database and launches the JavaFX application.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database with sample data
            DatabaseInitializer initializer = new DatabaseInitializer();
            if (!initializer.initializeSampleData()) {
                System.err.println("Failed to initialize database");
                return;
            }

            // Load login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginPage.fxml"));
            Parent root = loader.load();

            // Use FXML preferred size
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            primaryStage.setTitle("Course & Credit Tracking System");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Clean up database connection
        com.university.data.DatabaseConnection.getInstance().closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}