package com.tcet.ivm2.controller;

import com.tcet.ivm2.modal.DBconnection;
import com.tcet.ivm2.controller.WindowManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Desktop;
import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.colors.ColorConstants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

public class DashboardController implements Initializable {
    
    @FXML
    private PieChart pie;
    
    @FXML
    private LineChart<String, Number> line;
    
    @FXML
    private Label availableMedicinesLabel;
    
    @FXML
    private Label totalIncomeLabel;
    
    @FXML
    private Label totalCustomersLabel;
    
    @FXML
    private Button report;
    
    @FXML private Button dashboardBtn;

    
    @FXML
    private Button addMedicineBtn;
    
    @FXML
    private Button purchaseBtn;
    
    @FXML
    private Button logoutBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadSalesPieChart();
        loadSalesLineChart();
        loadDashboardCards();
    }

    private void loadSalesPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        String sql = "SELECT pm.type, SUM(s.Quantity) AS total_sold " +
                     "FROM sales s " +
                     "JOIN product_master pm ON s.Product_Id = pm.Product_Id " +
                     "GROUP BY pm.type";

        try (Connection conn = DBconnection.connect();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String medType = rs.getString("type");
                int totalSold = rs.getInt("total_sold");
                pieChartData.add(new PieChart.Data(medType, totalSold));
            }
            pie.setData(pieChartData);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load pie chart!");
        }
    }
    
    private void loadSalesLineChart() {
        line.getData().clear();
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Daily Sales");
        
        String sql = "SELECT SalesDate, SUM(Quantity) AS daily_total " +
                     "FROM sales " +
                     "GROUP BY SalesDate " +
                     "ORDER BY SalesDate";
        
        try (Connection conn = DBconnection.connect();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            
            while (rs.next()) {
                String date = rs.getString("SalesDate");
                int totalSold = rs.getInt("daily_total");
                series.getData().add(new XYChart.Data<>(date, totalSold));
            }
            
            line.getData().add(series);
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load line chart!");
        }
    }
    
    private void loadDashboardCards() {
        loadAvailableMedicines();
        loadTotalIncome();
        loadTotalStockQuantity();
    }
    
    private void loadAvailableMedicines() {
        String sql = "SELECT COUNT(*) AS total FROM product_master WHERE Quantity > 0";
        
        try (Connection conn = DBconnection.connect();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            
            if (rs.next()) {
                int total = rs.getInt("total");
                availableMedicinesLabel.setText(String.valueOf(total));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadTotalIncome() {
        String sql = "SELECT SUM(Total_price) AS total_income FROM sales";
        
        try (Connection conn = DBconnection.connect();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            
            if (rs.next()) {
                double totalIncome = rs.getDouble("total_income");
                totalIncomeLabel.setText("₹" + String.format("%.2f", totalIncome));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadTotalStockQuantity() {
        String sql = "SELECT SUM(Quantity) AS total_stock FROM product_master";
        
        try (Connection conn = DBconnection.connect();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            
            if (rs.next()) {
                int totalStock = rs.getInt("total_stock");
                totalCustomersLabel.setText(String.valueOf(totalStock));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ===== PDF REPORT GENERATION WITH DATE RANGE & FILE CHOOSER =====
    @FXML
    private void generatePDFReport() {
        // Show report options dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Generate Report");
        dialog.setHeaderText("Configure Report Settings");
        
        // Create dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        // Date range selection
        ComboBox<String> rangeCombo = new ComboBox<>();
        rangeCombo.getItems().addAll("This Month", "This Year", "Custom Range", "All Time");
        rangeCombo.setValue("This Month");
        
        // Date pickers for custom range
        DatePicker fromDatePicker = new DatePicker();
        DatePicker toDatePicker = new DatePicker();
        fromDatePicker.setValue(LocalDate.now().minusMonths(1));
        toDatePicker.setValue(LocalDate.now());
        
        // Show/hide date pickers based on selection
        fromDatePicker.setVisible(false);
        toDatePicker.setVisible(false);
        fromDatePicker.setManaged(false);
        toDatePicker.setManaged(false);
        
        rangeCombo.setOnAction(e -> {
            boolean isCustom = rangeCombo.getValue().equals("Custom Range");
            fromDatePicker.setVisible(isCustom);
            toDatePicker.setVisible(isCustom);
            fromDatePicker.setManaged(isCustom);
            toDatePicker.setManaged(isCustom);
        });
        
        // Add to grid
        grid.add(new Label("Date Range:"), 0, 0);
        grid.add(rangeCombo, 1, 0);
        grid.add(new Label("From Date:"), 0, 1);
        grid.add(fromDatePicker, 1, 1);
        grid.add(new Label("To Date:"), 0, 2);
        grid.add(toDatePicker, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Optional<ButtonType> result = dialog.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Get date range
            String startDate = null;
            String endDate = null;
            String rangeTitle = rangeCombo.getValue();
            
            switch (rangeCombo.getValue()) {
                case "This Month":
                    LocalDate firstDayMonth = LocalDate.now().withDayOfMonth(1);
                    LocalDate lastDayMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
                    startDate = firstDayMonth.toString();
                    endDate = lastDayMonth.toString();
                    break;
                case "This Year":
                    LocalDate firstDayYear = LocalDate.now().withDayOfYear(1);
                    LocalDate lastDayYear = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());
                    startDate = firstDayYear.toString();
                    endDate = lastDayYear.toString();
                    break;
                case "Custom Range":
                    if (fromDatePicker.getValue() != null && toDatePicker.getValue() != null) {
                        startDate = fromDatePicker.getValue().toString();
                        endDate = toDatePicker.getValue().toString();
                    } else {
                        showError("Please select both From and To dates.");
                        return;
                    }
                    break;
                case "All Time":
                    startDate = null;
                    endDate = null;
                    break;
            }
            
         FileChooser fileChooser = new FileChooser();
fileChooser.setTitle("Save Report");
fileChooser.setInitialFileName(
    "Inventory_Report_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf"
);
fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

// show dialog
File file = fileChooser.showSaveDialog(report.getScene().getWindow());

// 🚀 ***THIS WAS MISSING***
// If user selected a file, generate PDF
if (file != null) {
    generatePDF(file.getAbsolutePath(), startDate, endDate, rangeTitle);
} else {
    showError("No file selected. Report not generated.");
}
} // END if pressed OK
} // END generatePDFReport

    private void generatePDF(String filePath, String startDate, String endDate, String rangeTitle) {
        try {
            // Create PDF
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            // Title
            document.add(new Paragraph("INVENTORY MANAGEMENT SYSTEM - ANALYTICS REPORT")
                    .setFontSize(18).setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            
            // Date and Range
            document.add(new Paragraph("Generated: " + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));
            
            document.add(new Paragraph("Report Period: " + rangeTitle)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT));
            
            if (startDate != null && endDate != null) {
                document.add(new Paragraph("From: " + startDate + " To: " + endDate)
                        .setFontSize(10)
                        .setTextAlignment(TextAlignment.RIGHT));
            }
            
            document.add(new Paragraph("\n"));
            
            // Build WHERE clause for date filtering
            String dateFilter = "";
            if (startDate != null && endDate != null) {
                dateFilter = " WHERE s.SalesDate BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }
            
            // Get data from database
            Connection conn = DBconnection.connect();
            
            // Summary Data
            String sql1 = "SELECT COUNT(*) AS total FROM product_master WHERE Quantity > 0";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ResultSet rs1 = ps1.executeQuery();
            int availableMeds = rs1.next() ? rs1.getInt("total") : 0;
            
            String sql2 = "SELECT SUM(Total_price) AS total_income FROM sales s" + dateFilter;
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ResultSet rs2 = ps2.executeQuery();
            double totalIncome = rs2.next() ? rs2.getDouble("total_income") : 0;
            
            String sql3 = "SELECT SUM(Quantity) AS total_stock FROM product_master";
            PreparedStatement ps3 = conn.prepareStatement(sql3);
            ResultSet rs3 = ps3.executeQuery();
            int totalStock = rs3.next() ? rs3.getInt("total_stock") : 0;
            
            String sql6 = "SELECT SUM(Quantity) AS total_sold FROM sales s" + dateFilter;
            PreparedStatement ps6 = conn.prepareStatement(sql6);
            ResultSet rs6 = ps6.executeQuery();
            int totalSold = rs6.next() ? rs6.getInt("total_sold") : 0;
            
            // Add summary
            document.add(new Paragraph("SUMMARY")
                    .setFontSize(14).setBold());
            
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                    .useAllAvailableWidth();
            
            summaryTable.addHeaderCell(createHeaderCell("Available Medicines"));
            summaryTable.addHeaderCell(createHeaderCell("Total Income"));
            summaryTable.addHeaderCell(createHeaderCell("Total Stock"));
            summaryTable.addHeaderCell(createHeaderCell("Total Sold"));
            
            summaryTable.addCell(createCell(String.valueOf(availableMeds)));
            summaryTable.addCell(createCell("₹‚¹" + String.format("%.2f", totalIncome)));
            summaryTable.addCell(createCell(String.valueOf(totalStock)));
            summaryTable.addCell(createCell(String.valueOf(totalSold)));
            
            document.add(summaryTable);
            document.add(new Paragraph("\n"));
            
            // Sales by Type Table
            document.add(new Paragraph("SALES BY MEDICINE TYPE")
                    .setFontSize(14).setBold());
            
            Table typeTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
                    .useAllAvailableWidth();
            
            typeTable.addHeaderCell(createHeaderCell("Medicine Type"));
            typeTable.addHeaderCell(createHeaderCell("Total Sold"));
            
            String sql4 = "SELECT pm.type, SUM(s.Quantity) AS total_sold FROM sales s " +
                         "JOIN product_master pm ON s.Product_Id = pm.Product_Id" + dateFilter +
                         " GROUP BY pm.type ORDER BY total_sold DESC";
            PreparedStatement ps4 = conn.prepareStatement(sql4);
            ResultSet rs4 = ps4.executeQuery();
            
            while(rs4.next()) {
                typeTable.addCell(createCell(rs4.getString("type")));
                typeTable.addCell(createCell(String.valueOf(rs4.getInt("total_sold"))));
            }
            
            document.add(typeTable);
            document.add(new Paragraph("\n"));
            
            // Top 10 Products
            document.add(new Paragraph("TOP 10 SELLING PRODUCTS")
                    .setFontSize(14).setBold());
            
            Table productTable = new Table(UnitValue.createPercentArray(new float[]{3, 2, 1}))
                    .useAllAvailableWidth();
            
            productTable.addHeaderCell(createHeaderCell("Product Name"));
            productTable.addHeaderCell(createHeaderCell("Brand"));
            productTable.addHeaderCell(createHeaderCell("Sold"));
            
            String sql5 = "SELECT pm.Name, pm.Brand, SUM(s.Quantity) AS total_sold FROM sales s " +
                         "JOIN product_master pm ON s.Product_Id = pm.Product_Id" + dateFilter +
                         " GROUP BY pm.Product_Id ORDER BY total_sold DESC LIMIT 10";
            PreparedStatement ps5 = conn.prepareStatement(sql5);
            ResultSet rs5 = ps5.executeQuery();
            
            while(rs5.next()) {
                productTable.addCell(createCell(rs5.getString("Name")));
                productTable.addCell(createCell(rs5.getString("Brand")));
                productTable.addCell(createCell(String.valueOf(rs5.getInt("total_sold"))));
            }
            
            document.add(productTable);
            
            conn.close();
            document.close();
            
            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "PDF Report Generated!\nReport saved at:\n" + filePath);
            
            // Open PDF
            if(Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(filePath));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to generate report: " + e.getMessage());
        }
    }
    
    // ===== NAVIGATION METHODS =====
    
    @FXML
    private void handleAdminPage(ActionEvent event) {
        Stage stage = (Stage) addMedicineBtn.getScene().getWindow();
        try {
            WindowManager.loadLargeWindow(stage,
                "/com/tcet/ivm2/view/AdminPage.fxml",
                "Medicine Management");
            System.out.println("₹œ… Navigated to Admin Page!");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to navigate to Admin Page!");
        }
    }

    @FXML
    private void handlePurchasePage(ActionEvent event) {
        Stage stage = (Stage) purchaseBtn.getScene().getWindow();
        try {
            WindowManager.loadLargeWindow(stage,
                "/com/tcet/ivm2/view/PurchasePage.fxml",
                "Purchase - Billing");
            System.out.println("₹œ… Navigated to Purchase Page!");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to navigate to Purchase Page!");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        try {
            WindowManager.loadSmallWindow(stage,
                "/com/tcet/ivm2/view/Login.fxml",
                "Inventory Management System - Login");
            System.out.println("₹œ… Logged out successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to logout!");
        }
    }
    
    // ===== HELPER METHODS =====
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
private void handleDashBoard(ActionEvent event) {
    // Already on Dashboard - do nothing
    // or refresh dashboard
}

    
    private Cell createHeaderCell(String content) {
        Cell cell = new Cell().add(new Paragraph(content));
        cell.setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        return cell;
    }
    
    private Cell createCell(String content) {
        Cell cell = new Cell().add(new Paragraph(content));
        cell.setTextAlignment(TextAlignment.CENTER);
        return cell;
    }
}