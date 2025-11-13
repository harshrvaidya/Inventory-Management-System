package com.tcet.ivm2.controller;

import com.tcet.ivm2.modal.DBconnection;
import com.tcet.ivm2.modal.PurchaseItem;
import com.tcet.ivm2.controller.WindowManager;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PurchasePageController implements Initializable {

    // ===== NAVIGATION BUTTONS =====
    @FXML private Button dashboardBtn;
    @FXML private Button addMedicineBtn;
    @FXML private Button purchaseBtn;
    @FXML private Button signOutBtn;

    // ===== PURCHASE INTERFACE COMPONENTS =====
    @FXML private ChoiceBox<String> productTypeBox;
    @FXML private ChoiceBox<String> brandBox;
    @FXML private ChoiceBox<String> productNameBox;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private TextField totalField, amountField, balanceField;
    @FXML private Button addButton, payButton;
    @FXML private TableView<PurchaseItem> medicineTable;
    @FXML private TableColumn<PurchaseItem, String> colBrandName, colProductName, colProductType;
    @FXML private TableColumn<PurchaseItem, Integer> colQuantity;
    @FXML private TableColumn<PurchaseItem, Double> colPrice;
    
    private ObservableList<PurchaseItem> purchaseList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupSpinner();
        setupTable();
        loadProductTypes();
    }

    // ===== SETUP METHODS =====
    private void setupSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        quantitySpinner.setValueFactory(valueFactory);
    }

    private void setupTable() {
        colBrandName.setCellValueFactory(new PropertyValueFactory<>("brandName"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colProductType.setCellValueFactory(new PropertyValueFactory<>("productType"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        medicineTable.setItems(purchaseList);
    }

    // ===== LOAD DATA METHODS =====
    private void loadProductTypes() {
        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT type FROM product_master")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                productTypeBox.getItems().add(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        productTypeBox.setOnAction(event -> loadBrands(productTypeBox.getValue()));
    }

    private void loadBrands(String type) {
        brandBox.getItems().clear();
        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT DISTINCT Brand FROM product_master WHERE type=?")) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                brandBox.getItems().add(rs.getString("Brand"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        brandBox.setOnAction(event -> loadProducts(type, brandBox.getValue()));
    }

    private void loadProducts(String type, String brand) {
        productNameBox.getItems().clear();
        try (Connection conn = DBconnection.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT Name FROM product_master WHERE type=? AND Brand=?")) {
            stmt.setString(1, type);
            stmt.setString(2, brand);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                productNameBox.getItems().add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===== PURCHASE METHODS =====
    @FXML
    private void handleAdd(ActionEvent event) {
        String type = productTypeBox.getValue();
        String brand = brandBox.getValue();
        String product = productNameBox.getValue();
        int qty = quantitySpinner.getValue();

        if (type == null || brand == null || product == null) {
            showAlert("Please select all fields before adding!");
            return;
        }

        try (Connection conn = DBconnection.connect()) {

            // ⭐ Check available quantity first
            PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT Price, Quantity FROM product_master WHERE type=? AND Brand=? AND Name=?"
            );
            checkStmt.setString(1, type);
            checkStmt.setString(2, brand);
            checkStmt.setString(3, product);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int availableQty = rs.getInt("Quantity");
                double price = rs.getDouble("Price");

                // ⭐ Check stock availability
                if (availableQty == 0) {
                    showAlert("⚠ Medicine is out of stock!");
                    return;
                } else if (qty > availableQty) {
                    showAlert("Only " + availableQty + " units available in stock!");
                    return;
                }

                // ⭐ Calculate price and add to table
                double totalPrice = price * qty;
                purchaseList.add(new PurchaseItem(brand, product, type, qty, totalPrice));
                updateTotal();

                // ⭐ Reduce stock in DB
                PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE product_master SET Quantity = Quantity - ? WHERE type=? AND Brand=? AND Name=?"
                );
                updateStmt.setInt(1, qty);
                updateStmt.setString(2, type);
                updateStmt.setString(3, brand);
                updateStmt.setString(4, product);
                updateStmt.executeUpdate();

                // ⭐ If stock becomes zero, show message
                if (availableQty - qty == 0) {
                    showAlert("⚠ Medicine '" + product + "' is now out of stock!");
                }

            } else {
                showAlert("Medicine not found in database!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void handlePay(ActionEvent event) {
        try {
            double total = Double.parseDouble(totalField.getText());
            double amount = Double.parseDouble(amountField.getText());
            
            if (amount < total) {
                showAlert("❌ Insufficient amount! Required: ₹" + total);
                return;
            }
            
            double balance = amount - total;
            balanceField.setText(String.format("%.2f", balance));

            showAlert("✅ Payment Successful!\nBalance: ₹" + String.format("%.2f", balance));
            
            // Clear all fields after payment
            purchaseList.clear();
            totalField.setText("0.0");
            amountField.setText("0.0");
            balanceField.setText("0.0");
            productTypeBox.setValue(null);
            brandBox.getItems().clear();
            productNameBox.getItems().clear();
            quantitySpinner.getValueFactory().setValue(1);
            
        } catch (NumberFormatException e) {
            showAlert("❌ Please enter valid numbers!");
        }
    }

    private void updateTotal() {
        double total = purchaseList.stream().mapToDouble(PurchaseItem::getPrice).sum();
        totalField.setText(String.format("%.2f", total));
    }

    // ===== NAVIGATION METHODS =====
    @FXML
    private void handleDashBoard(ActionEvent event) {
        Stage stage = (Stage) dashboardBtn.getScene().getWindow();
        try {
            WindowManager.loadLargeWindow(
                stage, "/com/tcet/ivm2/view/dashboard.fxml", "Dashboard"
            );
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to open Dashboard!");
        }
    }

    @FXML
    private void handleAdminPage(ActionEvent event) {
        Stage stage = (Stage) addMedicineBtn.getScene().getWindow();
        try {
            WindowManager.loadLargeWindow(
                stage, "/com/tcet/ivm2/view/AdminPage.fxml", "Admin Page"
            );
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to open Admin Page!");
        }
    }

    @FXML
    private void handlePurchase(ActionEvent event) {
        // Already on Purchase Page - do nothing
    }

    @FXML
    private void handleSignout(ActionEvent event) {
        Stage stage = (Stage) signOutBtn.getScene().getWindow();
        try {
            WindowManager.loadSmallWindow(
                stage, "/com/tcet/ivm2/view/login.fxml", "Inventory Management System - Login"
            );
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to logout!");
        }
    }

    // ===== HELPER METHODS =====
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
