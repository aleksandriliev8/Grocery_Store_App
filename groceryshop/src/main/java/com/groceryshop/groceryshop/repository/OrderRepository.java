package com.groceryshop.groceryshop.repository;

import com.groceryshop.groceryshop.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

}
