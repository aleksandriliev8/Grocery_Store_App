package com.groceryshop.groceryshop.manager;

import com.groceryshop.groceryshop.model.CartItem;
import com.groceryshop.groceryshop.model.OrderEntity;
import com.groceryshop.groceryshop.model.OrderItemEntity;
import com.groceryshop.groceryshop.model.OrderResult;
import com.groceryshop.groceryshop.model.OrderStatus;
import com.groceryshop.groceryshop.model.Product;
import com.groceryshop.groceryshop.model.RouteStepEntity;
import com.groceryshop.groceryshop.model.WarehouseLocation;
import com.groceryshop.groceryshop.repository.OrderRepository;
import com.groceryshop.groceryshop.repository.ProductRepository;
import com.groceryshop.groceryshop.util.BotPathCalculator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderManager implements IOrderManager {
    private final IProductManager productManager;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;


    public OrderManager(IProductManager productManager, ProductRepository productRepository, OrderRepository orderRepository) {
        this.productManager = productManager;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResult createOrder(Map<String, Integer> requested) {
        List<CartItem> orderItems = new ArrayList<>();
        Map<String, Integer> missing = new LinkedHashMap<>();
        Map<String, Integer> available = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : requested.entrySet()) {
            String prodName = entry.getKey();
            int qty = entry.getValue();
            Product p = productManager.findProductByName(prodName);
            int avail = (p != null) ? p.getQuantity() : 0;

            if (p == null || avail < qty) {
                int missingQty = qty - avail;
                missing.put(prodName, missingQty);
                available.put(prodName, avail);
            } else {
                orderItems.add(new CartItem(p, qty));
            }
        }


        if (!missing.isEmpty()) {
            return new OrderResult(
                    false,
                    Collections.emptyList(),
                    new LinkedHashMap<>(missing),
                    new LinkedHashMap<>(available)
            );
        }

        for (CartItem item : orderItems) {
            Product p = item.product();
            p.setQuantity(p.getQuantity() - item.quantity());
            productRepository.save(p);
        }

        OrderEntity order = new OrderEntity();
        order.setCreatedAt(java.time.LocalDateTime.now());
        order.setStatus(OrderStatus.SUCCESS);

        for (CartItem cartItem : orderItems) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setProductName(cartItem.product().getName());
            orderItem.setQuantity(cartItem.quantity());
            order.addItem(orderItem);
        }

        List<WarehouseLocation> route = BotPathCalculator.calculateRoute(orderItems);
        for (WarehouseLocation loc : route) {
            RouteStepEntity step = new RouteStepEntity();
            step.setX(loc.x());
            step.setY(loc.y());
            order.addRouteStep(step);
        }

        order = orderRepository.save(order);

        return new OrderResult(
                true,
                route,
                Collections.emptyMap(),
                Collections.emptyMap()
        );
    }

}
