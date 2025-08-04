// OrderItemDTO.java
package com.groceryshop.groceryshop.dto;

public class OrderItemDTO {
    private String productName;
    private int quantity;

    public OrderItemDTO() {}

    public OrderItemDTO(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
