package com.tcet.ivm2.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

public class WindowManager {

    public static void loadSmallWindow(Stage stage, String fxmlPath, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(WindowManager.class.getResource(fxmlPath));
        Parent root = loader.load();

        // Set exact size of your FXML layout for perfect pixel matching
        double width = 634;
        double height = 407;
        Scene scene = new Scene(root, width, height);

        stage.setScene(scene);
        stage.setTitle(title);

        // Lock the window size exactly
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setMaxWidth(width);
        stage.setMaxHeight(height);

        // Prevent window resizing
        stage.setResizable(false);

        // Adjust the window to fit scene exactly
        stage.sizeToScene();
        stage.centerOnScreen();

        stage.show();
    }

    public static void loadLargeWindow(Stage stage, String fxmlPath, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(WindowManager.class.getResource(fxmlPath));
        Parent root = loader.load();

        boolean isCurrentlyMaximized = stage.isMaximized();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);

        stage.setMinWidth(100);
        stage.setMinHeight(100);
        stage.setMaxWidth(Double.MAX_VALUE);
        stage.setMaxHeight(Double.MAX_VALUE);

        stage.setResizable(true);

        if (isCurrentlyMaximized) {
            stage.setMaximized(true);
        } else {
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth() * 0.9);
            stage.setHeight(bounds.getHeight() * 0.9);
        }

        stage.show();
    }

    public static void loadMaximizedWindow(Stage stage, String fxmlPath, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(WindowManager.class.getResource(fxmlPath));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
    }
}
