package com.groceryshop.groceryshop.controller;

import com.groceryshop.groceryshop.manager.IProductManager;
import com.groceryshop.groceryshop.model.Product;
import com.groceryshop.groceryshop.dto.UpdateProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductManager productManager;

    public ProductController(IProductManager productManager) {
        this.productManager = productManager;
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productManager.getAllProducts();
        if (products.isEmpty()) return ResponseEntity.ok("No products available.");
        products.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Product p = productManager.findProductById(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with id " + id + " was not found in the inventory.");
        }
        return ResponseEntity.ok(p);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable String name) {
        Product p = productManager.findProductByName(name);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with name '" + name + "' was not found in the inventory.");
        }
        return ResponseEntity.ok(p);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        String name = product.getName() != null ? product.getName().trim() : "";
        if (name.isEmpty() || name.matches("\\d+"))
            return ResponseEntity.badRequest().body("Invalid product name.");
        if (productManager.existsByName(name))
            return ResponseEntity.badRequest().body("Product already exists: " + name);
        if (product.getPrice() <= 0)
            return ResponseEntity.badRequest().body("Price must be positive.");
        if (product.getQuantity() <= 0)
            return ResponseEntity.badRequest().body("Quantity must be positive.");
        if (product.getLocation() == null)
            return ResponseEntity.badRequest().body("Location is required.");
        int x = product.getLocation().x(), y = product.getLocation().y();
        if (x < 0 || y < 0)
            return ResponseEntity.badRequest().body("Coordinates must be non-negative.");
        if (x == 0 && y == 0)
            return ResponseEntity.badRequest().body("Location (0,0) is occupied by the robot and cannot store products.");
        if (productManager.isLocationOccupied(x, y)) {
            Product occupying = productManager.getProductByLocation(x, y);
            if (occupying != null) {
                return ResponseEntity.badRequest().body(
                        "Location (" + x + "," + y + ") is occupied by product: " +
                                occupying.getName() + " (ID: " + occupying.getId() + ")"
                );
            } else {
                return ResponseEntity.badRequest().body("Location (" + x + "," + y + ") is occupied.");
            }
        }
        Product created = productManager.addProduct(name, product.getPrice(), product.getQuantity(), x, y);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody UpdateProductDTO updated) {
        Product p = productManager.findProductById(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with id " + id + " was not found in the inventory.");
        }
        if (updated.getPrice() <= 0)
            return ResponseEntity.badRequest().body("Price must be positive.");
        if (updated.getQuantity() < 0)
            return ResponseEntity.badRequest().body("Quantity must be non-negative.");
        p.setPrice(updated.getPrice());
        p.setQuantity(updated.getQuantity());
        productManager.save(p);
        return ResponseEntity.ok("Product updated successfully.");
    }

    @PutMapping("/name/{name}")
    public ResponseEntity<?> updateProductByName(@PathVariable String name, @RequestBody UpdateProductDTO updated) {
        Product p = productManager.findProductByName(name);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with name '" + name + "' was not found in the inventory.");
        }
        if (updated.getPrice() <= 0)
            return ResponseEntity.badRequest().body("Price must be positive.");
        if (updated.getQuantity() < 0)
            return ResponseEntity.badRequest().body("Quantity must be non-negative.");
        p.setPrice(updated.getPrice());
        p.setQuantity(updated.getQuantity());
        productManager.save(p);
        return ResponseEntity.ok("Product updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        boolean deleted = productManager.deleteProductById(id);
        if (!deleted)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with id " + id + " was not found in the inventory.");
        return ResponseEntity.ok("Product deleted.");
    }

    @DeleteMapping("/name/{name}")
    public ResponseEntity<?> deleteProductByName(@PathVariable String name) {
        boolean deleted = productManager.deleteProductByName(name);
        if (!deleted)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with name '" + name + "' was not found in the inventory.");
        return ResponseEntity.ok("Product deleted.");
    }
}
