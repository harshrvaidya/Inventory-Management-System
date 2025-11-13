package com.tcet.ivm2.controller;

import static com.tcet.ivm2.modal.DBconnection.connect;
import com.tcet.ivm2.modal.Products;
import com.tcet.ivm2.controller.WindowManager;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminPageController implements Initializable {

    // ===== NAVIGATION BUTTONS =====
    @FXML private Button dashboardBtn;
    @FXML private Button addMedicineBtn;
    @FXML private Button purchaseBtn;
    @FXML private Button signOutBtn;

    // ===== PRODUCT MANAGEMENT COMPONENTS =====
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private TextField TfPrice;
    @FXML private TextField TfComp;
    @FXML private TextField TfType;
    @FXML private TextField TfProduct;
    @FXML private TextField TfMedID;
    @FXML private TextField TfBrand;
    @FXML private TextField TfShelf;
    @FXML private TextField TFquantity;
    @FXML private TableView<Products> myProductTable;
    @FXML private TableColumn<Products, Integer> colID;
    @FXML private TableColumn<Products, String> colBrand;
    @FXML private TableColumn<Products, String> colName;
    @FXML private TableColumn<Products, String> colType;
    @FXML private TableColumn<Products, String> colShelf;
    @FXML private TableColumn<Products, String> colCompartment;
    @FXML private TableColumn<Products, Integer> colPrice;
    @FXML private TableColumn<Products, String> colQuantity;
    @FXML private TextField TfSearch;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colShelf.setCellValueFactory(new PropertyValueFactory<>("shelf"));
        colCompartment.setCellValueFactory(new PropertyValueFactory<>("compartment"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        showProducts();
    }

    public ObservableList<Products> getProducts() throws SQLException {
        ObservableList<Products> myproductlist = FXCollections.observableArrayList();
        Connection conn = connect();
        String query = "SELECT * FROM product_master";

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Products objProd = new Products(
                    rs.getInt("Product_Id"),
                    rs.getString("Brand"),
                    rs.getString("Name"),
                    rs.getInt("Shelf"),
                    rs.getString("Compartment").charAt(0),
                    rs.getInt("Quantity"),
                    rs.getInt("Price"),
                    rs.getString("type")
                );
                myproductlist.add(objProd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myproductlist;
    }

    public void showProducts() {
        try {
            myProductTable.setItems(getProducts());
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load products!");
        }
    }

    // ===== CRUD OPERATIONS =====
    private void insertRecords() {
        if (!validateInputs()) {
            return;
        }

        String myquery = "INSERT INTO product_master(Product_Id,Name, Quantity, Price, Shelf, Compartment, Brand, type) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(myquery)) {

            pst.setInt(1, Integer.parseInt(TfMedID.getText()));
            pst.setString(2, TfProduct.getText());
            pst.setInt(3, Integer.parseInt(TFquantity.getText()));
            pst.setInt(4, Integer.parseInt(TfPrice.getText()));
            pst.setString(5, TfShelf.getText());
            pst.setString(6, TfComp.getText());
            pst.setString(7, TfBrand.getText());
            pst.setString(8, TfType.getText());
            pst.executeUpdate();
            System.out.println("✅ Insert successful");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine added successfully!");
            clearFields();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numbers for ID, Quantity, and Price!");
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add medicine!");
        }
        showProducts();
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        insertRecords();
    }

    private void updateRecords() {
        if (!validateInputs()) {
            return;
        }

        String myquery = "UPDATE product_master SET Name=?, Quantity=?, Price=?, Shelf=?, Compartment=?, Brand=?, type=? WHERE Product_Id=?";

        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(myquery)) {

            pst.setString(1, TfProduct.getText());
            pst.setInt(2, Integer.parseInt(TFquantity.getText()));
            pst.setInt(3, Integer.parseInt(TfPrice.getText()));
            pst.setString(4, TfShelf.getText());
            pst.setString(5, TfComp.getText());
            pst.setString(6, TfBrand.getText());
            pst.setString(7, TfType.getText());
            pst.setInt(8, Integer.parseInt(TfMedID.getText()));

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Record updated successfully!");
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine updated successfully!");
                clearFields();
            } else {
                System.out.println("❌ No record found with that Product_Id.");
                showAlert(Alert.AlertType.WARNING, "Not Found", "No medicine found with that ID!");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numbers for ID, Quantity, and Price!");
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update medicine!");
        }

        showProducts();
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        updateRecords();
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        if (TfMedID.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter Medicine ID to delete!");
            return;
        }

        String q = "DELETE FROM product_master WHERE Product_Id=?";
        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(q)) {

            int x = Integer.parseInt(TfMedID.getText());
            pst.setInt(1, x);

            int y = pst.executeUpdate();
            if (y > 0) {
                System.out.println("✅ Deletion successful");
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine deleted successfully!");
                clearFields();
            } else {
                System.out.println("❌ No record found");
                showAlert(Alert.AlertType.WARNING, "Not Found", "No medicine found with that ID!");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid ID!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete medicine!");
        }
        showProducts();
    }

    @FXML
    private void handleClear(ActionEvent event) {
        clearFields();
    }

    private boolean validateInputs() {
        if (TfMedID.getText().isEmpty() || TfProduct.getText().isEmpty() ||
            TFquantity.getText().isEmpty() || TfPrice.getText().isEmpty() ||
            TfShelf.getText().isEmpty() || TfComp.getText().isEmpty() ||
            TfBrand.getText().isEmpty() || TfType.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill all fields!");
            return false;
        }
        return true;
    }

    private void clearFields() {
        TfMedID.clear();
        TfProduct.clear();
        TFquantity.clear();
        TfPrice.clear();
        TfShelf.clear();
        TfComp.clear();
        TfBrand.clear();
        TfType.clear();
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
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to open Dashboard!");
        }
    }

    @FXML
    private void handleAdminPage(ActionEvent event) {
        // Already on Admin Page - do nothing
    }

    @FXML
    private void handlePurchase(ActionEvent event) {
        Stage stage = (Stage) purchaseBtn.getScene().getWindow();
        try {
            WindowManager.loadLargeWindow(
                stage,
                "/com/tcet/ivm2/view/PurchasePage.fxml",
                "Purchase Page"
            );
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to open Purchase Page!");
        }
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
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to logout!");
        }
    }

    // ===== HELPER METHODS =====
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
