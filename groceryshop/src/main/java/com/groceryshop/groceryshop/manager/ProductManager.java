package com.groceryshop.groceryshop.manager;

import com.groceryshop.groceryshop.model.Product;
import com.groceryshop.groceryshop.model.WarehouseLocation;
import com.groceryshop.groceryshop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductManager implements IProductManager {

    private final ProductRepository productRepository;

    public ProductManager(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductByName(String name) {
        return productRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Product addProduct(String name, double price, int quantity, int x, int y) {
        if (existsByName(name)) {
            Product existing = productRepository.findByNameIgnoreCase(name);
            existing.setQuantity(existing.getQuantity() + quantity);
            return productRepository.save(existing);
        } else {
            WarehouseLocation loc = new WarehouseLocation(x, y);
            Product p = new Product(name, price, quantity, loc);
            return productRepository.save(p);
        }
    }

    @Override
    public void updateProduct(String name, double price, int quantity) {
        Product p = productRepository.findByNameIgnoreCase(name);
        if (p != null) {
            p.setPrice(price);
            p.setQuantity(quantity);
            productRepository.save(p);
        }
    }

    @Override
    public boolean deleteProduct(String name) {
        Product p = productRepository.findByNameIgnoreCase(name);
        if (p != null) {
            productRepository.delete(p);
            return true;
        }
        return false;
    }

    @Override
    public boolean isLocationOccupied(int x, int y) {
        return productRepository.existsByLocation_XAndLocation_Y(x, y);
    }

    // Методи по id

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.findByNameIgnoreCase(name) != null;
    }

    @Override
    public void updateProductById(Long id, Product updated) {
        productRepository.findById(id).ifPresent(existing -> {
            existing.setName(updated.getName());
            existing.setPrice(updated.getPrice());
            existing.setQuantity(updated.getQuantity());
            existing.setLocation(updated.getLocation());
            productRepository.save(existing);
        });
    }

    @Override
    public boolean deleteProductById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Product save(Product p) {
        return productRepository.save(p);
    }

    @Override
    public boolean deleteProductByName(String name) {
        Product p = productRepository.findByNameIgnoreCase(name);
        if (p != null) {
            productRepository.delete(p);
            return true;
        }
        return false;
    }

    @Override
    public Product getProductByLocation(int x, int y) {
        return productRepository.findByLocation_XAndLocation_Y(x, y);
    }

}
