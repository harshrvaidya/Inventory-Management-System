package com.tcet.ivm2.modal;

public class PurchaseItem {
    private String brandName;
    private String productName;
    private String productType;
    private int quantity;
    private double price;

    public PurchaseItem(String brandName, String productName, String productType, int quantity, double price) {
        this.brandName = brandName;
        this.productName = productName;
        this.productType = productType;
        this.quantity = quantity;
        this.price = price;
    }

    public String getBrandName() { return brandName; }
    public String getProductName() { return productName; }
    public String getProductType() { return productType; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}