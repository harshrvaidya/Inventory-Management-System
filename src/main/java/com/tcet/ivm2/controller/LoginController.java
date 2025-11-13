package com.tcet.ivm2.controller;

import static com.tcet.ivm2.modal.DBconnection.connect;
import com.tcet.ivm2.controller.WindowManager;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button signupLink;  // ← CHANGED from Hyperlink to Button

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter username and password!");
            return;
        }

        try (Connection conn = connect()) {
            String query = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");
                Stage stage = (Stage) loginBtn.getScene().getWindow();
                WindowManager.loadLargeWindow(
                    stage, "/com/tcet/ivm2/view/dashboard.fxml", "Dashboard"
                );
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to login!");
        }
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        Stage stage = (Stage) signupLink.getScene().getWindow();
        try {
            WindowManager.loadSmallWindow(
                stage, "/com/tcet/ivm2/view/signup.fxml", "Sign Up"
            );
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to open Sign Up page!");
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
