package com.groceryshop.groceryshop.manager;

import com.groceryshop.groceryshop.model.OrderResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface IOrderManager {
    OrderResult createOrder(Map<String, Integer> requested);
}