package com.groceryshop.groceryshop.controller;

import com.groceryshop.groceryshop.dto.OrderProductDTO;
import com.groceryshop.groceryshop.dto.OrderResponseDTO;
import com.groceryshop.groceryshop.dto.RouteStepDTO;
import com.groceryshop.groceryshop.manager.IOrderManager;
import com.groceryshop.groceryshop.manager.IProductManager;
import com.groceryshop.groceryshop.model.OrderResult;
import com.groceryshop.groceryshop.model.OrderEntity;
import com.groceryshop.groceryshop.dto.OrderItemDTO;
import com.groceryshop.groceryshop.dto.OrderRequestDTO;
import com.groceryshop.groceryshop.model.Product;
import com.groceryshop.groceryshop.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final IOrderManager orderManager;
    private final IProductManager productManager;
    private final OrderRepository orderRepository;

    public OrderController(IOrderManager orderManager, IProductManager productManager, OrderRepository orderRepository) {
        this.orderManager = orderManager;
        this.productManager = productManager;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO orderReq) {
        if (orderReq == null || orderReq.getItems() == null || orderReq.getItems().isEmpty())
            return ResponseEntity.badRequest().body("Order must contain at least one item.");

        Map<String, Integer> items = new LinkedHashMap<>();
        for (OrderItemDTO dto : orderReq.getItems()) {
            String name = dto.getProductName();
            int qty = dto.getQuantity();
            if (name == null || name.trim().isEmpty())
                return ResponseEntity.badRequest().body("Product name in order cannot be empty.");
            if (qty <= 0)
                return ResponseEntity.badRequest().body("Quantity for product " + name + " must be positive.");
            if (productManager.findProductByName(name) == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found: " + name);
            items.put(name, qty);
        }
        OrderResult result = orderManager.createOrder(items);
        if (result.success()) return ResponseEntity.ok(result);
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            return ResponseEntity.ok("No orders available.");
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        OrderEntity order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order with id " + id + " was not found in the system.");
        }

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.id = order.getId();
        dto.createdAt = order.getCreatedAt();
        dto.status = order.getStatus().toString();

        // Items
        dto.items = order.getItems().stream().map(item -> {
            OrderProductDTO p = new OrderProductDTO();
            // Вземи id-то на продукта по име (или пази productId в OrderItemEntity)
            Product prod = productManager.findProductByName(item.getProductName());
            p.productId = (prod != null) ? prod.getId() : null;
            p.productName = item.getProductName();
            p.quantity = item.getQuantity();
            return p;
        }).toList();

        // Route
        dto.route = order.getRoute().stream().map(step -> {
            RouteStepDTO r = new RouteStepDTO();
            r.x = step.getX();
            r.y = step.getY();
            return r;
        }).toList();

        return ResponseEntity.ok(dto);
    }
}
