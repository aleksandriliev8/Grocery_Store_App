package com.groceryshop.groceryshop.dto;

public class UpdateProductDTO {
    private double price;
    private int quantity;

    public UpdateProductDTO() {}

    public UpdateProductDTO(double price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
