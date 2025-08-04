package com.groceryshop.groceryshop.repository;

import com.groceryshop.groceryshop.model.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    // По избор, ако ще ти трябва
}
