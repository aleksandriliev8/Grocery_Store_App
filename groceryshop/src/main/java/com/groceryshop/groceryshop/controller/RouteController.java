package com.groceryshop.groceryshop.controller;

import com.groceryshop.groceryshop.model.OrderEntity;
import com.groceryshop.groceryshop.model.RouteStepEntity;
import com.groceryshop.groceryshop.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/routes")
public class RouteController {
    private final OrderRepository orderRepository;

    public RouteController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public ResponseEntity<?> getRouteByOrderId(@RequestParam(value = "orderId", required = false) Long orderId) {
        if (orderId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing orderId parameter"));
        }
        OrderEntity order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return ResponseEntity.status(404).body(Map.of("error", "Order not found: " + orderId));
        List<List<Integer>> visitedLocations = new ArrayList<>();
        for (RouteStepEntity step : order.getRoute()) {
            visitedLocations.add(List.of(step.getX(), step.getY()));
        }
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("orderId", orderId);
        response.put("status", order.getStatus().name());
        response.put("visitedLocations", visitedLocations);
        return ResponseEntity.ok(response);
    }
}
