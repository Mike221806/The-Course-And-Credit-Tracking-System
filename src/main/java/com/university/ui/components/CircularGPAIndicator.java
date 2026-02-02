package com.university.ui.components;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Custom circular GPA indicator that displays as a ring/bar around the GPA number.
 * The ring fills up and changes color based on GPA value.
 */
public class CircularGPAIndicator extends StackPane {

    private final ProgressIndicator progressIndicator;
    private double currentGPA = 0.0;

    public CircularGPAIndicator() {
        // Progress indicator configured as a ring (not filled) - make it much larger and thicker
        progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(250, 250); // Make it larger
        progressIndicator.setProgress(0.0);

        // Style it as a thick, visible ring
        progressIndicator.setStyle(
                "-fx-progress-color: #4CAF50;" +
                        "-fx-accent: #4CAF50;" +
                        "-fx-background-insets: 0;" +
                        "-fx-padding: 0;" +
                        "-fx-skin: \"com.sun.javafx.scene.control.skin.ProgressIndicatorSkin\";" +
                        "-fx-indeterminate-segment-count: 8;" +
                        "-fx-progress-insets: 20;" +  // Make the ring thicker
                        "-fx-control-inner-background: transparent;" +  // Transparent center
                        "-fx-control-inner-background-alt: transparent;"
        );

        // No text in the center since CGPA is displayed to the right

        // Just add the progress indicator (no text stack)
        getChildren().add(progressIndicator);
    }

    /**
     * Updates the GPA value and visual representation.
     * @param gpa The GPA value (0.0 - 4.0)
     */
    public void setGPA(double gpa) {
        this.currentGPA = Math.max(0.0, Math.min(4.0, gpa)); // Clamp between 0 and 4

        // Update progress (GPA/4.0 = percentage)
        progressIndicator.setProgress(this.currentGPA / 4.0);

        // Update color based on GPA
        updateColor();
    }

    /**
     * Updates the color based on current GPA value.
     */
    private void updateColor() {
        Color color;

        if (currentGPA >= 3.5) {
            // Excellent - Green
            color = Color.rgb(76, 175, 80); // Material Green
        } else if (currentGPA >= 3.0) {
            // Good - Light Green
            color = Color.rgb(139, 195, 74); // Material Light Green
        } else if (currentGPA >= 2.5) {
            // Average - Yellow/Green mix
            color = Color.rgb(205, 220, 57); // Material Lime
        } else if (currentGPA >= 2.0) {
            // Below Average - Yellow
            color = Color.rgb(255, 235, 59); // Material Yellow
        } else if (currentGPA >= 1.5) {
            // Poor - Orange
            color = Color.rgb(255, 193, 7); // Material Amber
        } else {
            // Very Poor - Red
            color = Color.rgb(244, 67, 54); // Material Red
        }

        // Apply color to progress indicator ring only
        progressIndicator.setStyle(
                "-fx-progress-color: " + toRgbString(color) + ";" +
                        "-fx-accent: " + toRgbString(color) + ";" +
                        "-fx-background-insets: 0;" +
                        "-fx-padding: 0;"
        );
    }

    /**
     * Converts JavaFX Color to RGB string for CSS.
     */
    private String toRgbString(Color color) {
        return String.format("rgba(%d,%d,%d,%f)",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255),
                color.getOpacity());
    }

    /**
     * Gets the current GPA value.
     */
    public double getGPA() {
        return currentGPA;
    }
}
