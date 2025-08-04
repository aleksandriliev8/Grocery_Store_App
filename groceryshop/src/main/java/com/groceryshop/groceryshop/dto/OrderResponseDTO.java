package com.groceryshop.groceryshop.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {
    public Long id;
    public LocalDateTime createdAt;
    public String status;
    public List<OrderProductDTO> items;
    public List<RouteStepDTO> route;
}

