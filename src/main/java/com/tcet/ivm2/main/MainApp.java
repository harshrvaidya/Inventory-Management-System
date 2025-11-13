package com.tcet.ivm2.main;

import javafx.application.Application;
import javafx.stage.Stage;
import com.tcet.ivm2.controller.WindowManager;
import static javafx.application.Application.launch;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            WindowManager.loadSmallWindow(
                primaryStage,
                "/com/tcet/ivm2/view/login.fxml",
                "Inventory Management System - Login"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
