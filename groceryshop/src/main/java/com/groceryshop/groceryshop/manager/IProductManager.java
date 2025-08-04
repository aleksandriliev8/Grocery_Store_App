package com.groceryshop.groceryshop.manager;

import com.groceryshop.groceryshop.model.Product;
import java.util.List;
import java.util.Optional;

public interface IProductManager {
    List<Product> getAllProducts();
    Product findProductByName(String name);
    Product addProduct(String name, double price, int quantity, int x, int y);
    void updateProduct(String name, double price, int quantity);
    boolean deleteProduct(String name);
    boolean isLocationOccupied(int x, int y);
    Product findProductById(Long id);
    boolean existsByName(String name);
    void updateProductById(Long id, Product product);
    boolean deleteProductById(Long id);
    Product save(Product p);
    boolean deleteProductByName(String name);
    Product getProductByLocation(int x, int y);
}
