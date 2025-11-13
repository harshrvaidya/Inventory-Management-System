package com.tcet.ivm2.modal;
public class Products {
    private int id;
    private String  brand;
    private String Name;
    private int shelf;
    private char compartment;
    private int quantity;   
    private int price;
    private String type;

    public Products(int id, String brand, String Name, int shelf, char compartment, int quantity, int price, String type) {
        this.id = id;
        this.brand = brand;
        this.Name = Name;
        this.shelf = shelf;
        this.compartment = compartment;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

 
    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getName() {
        return Name;
    }

    public int getShelf() {
        return shelf;
    }

    public char getCompartment() {
        return compartment;
    }

    public int getQuantity() {
        return quantity;
    }
    
    
}
