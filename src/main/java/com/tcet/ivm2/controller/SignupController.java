package com.tcet.ivm2.controller;

import static com.tcet.ivm2.modal.DBconnection.connect;
import com.tcet.ivm2.controller.WindowManager;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class SignupController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button signupBtn;
    @FXML private Button loginLink;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill all fields!");
            return;
        }

        try (Connection conn = connect()) {
            String query = "INSERT INTO users (username, password, Email) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, email);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
                Stage stage = (Stage) signupBtn.getScene().getWindow();
                WindowManager.loadSmallWindow(
                    stage, "/com/tcet/ivm2/view/login.fxml", "Inventory Management System - Login"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Signup Error", "Failed to create account! Username may already exist.");
        }
    }

    @FXML
    private void handleLoginLink(ActionEvent event) {
        Stage stage = (Stage) loginLink.getScene().getWindow();
        try {
            WindowManager.loadSmallWindow(
                stage, "/com/tcet/ivm2/view/login.fxml", "Inventory Management System - Login"
            );
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to open Login page!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
