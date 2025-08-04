// OrderRequestDTO.java
package com.groceryshop.groceryshop.dto;

import com.groceryshop.groceryshop.dto.OrderItemDTO;

import java.util.List;

public class OrderRequestDTO {
    private List<OrderItemDTO> items;

    public OrderRequestDTO() {}

    public OrderRequestDTO(List<OrderItemDTO> items) {
        this.items = items;
    }

    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}
