package com.groceryshop.groceryshop.model;


import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


/**
 * Represents a product in the warehouse.
 * Each product has a name, price, quantity, and warehouse location.
 */

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private int quantity;

    @Embedded
    private WarehouseLocation location;

    public Product() {}

    public Product(String name, double price, int quantity, WarehouseLocation location) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.location = location;
    }

    // Гетъри и сетъри (ако не ползваш Lombok)

    @Override
    public String toString() {
        return String.format(
                "Name: %s | Price: %.2f | Qty: %d | Location: %s",
                name, price, quantity, location
        );
    }
}