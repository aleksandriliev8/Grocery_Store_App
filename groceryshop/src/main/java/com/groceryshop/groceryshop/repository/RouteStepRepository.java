package com.groceryshop.groceryshop.repository;

import com.groceryshop.groceryshop.model.RouteStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteStepRepository extends JpaRepository<RouteStepEntity, Long> {
    List<RouteStepEntity> findByOrderId(Long orderId);
}
