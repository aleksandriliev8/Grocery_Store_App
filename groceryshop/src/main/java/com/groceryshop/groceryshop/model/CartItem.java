package com.groceryshop.groceryshop.model;

/**
 * Represents a product and quantity in a shopping cart or order.
 */
public record CartItem(Product product, int quantity) {}
