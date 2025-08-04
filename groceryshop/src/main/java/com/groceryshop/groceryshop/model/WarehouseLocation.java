package com.groceryshop.groceryshop.model;

import jakarta.persistence.Embeddable;

/**
 * Represents a location in the warehouse by X and Y coordinates.
 */

@Embeddable
public record WarehouseLocation(int x, int y) {
    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}
