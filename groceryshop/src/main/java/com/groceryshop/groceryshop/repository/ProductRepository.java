package com.groceryshop.groceryshop.repository;

import com.groceryshop.groceryshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByNameIgnoreCase(String name);
    boolean existsByLocation_XAndLocation_Y(int x, int y);
    Product findByLocation_XAndLocation_Y(int x, int y);

}
