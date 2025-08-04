package com.groceryshop.groceryshop.model;

import java.util.List;
import java.util.Map;

/**
 * Represents the result of processing an order: status, route, and missing items.
 */
public record OrderResult(

        /*Boolean flag of success - keeps if the order is successfully made or not*/
        boolean success,

        /*Example route: [(0,0) , (0,1), (0,0)] - robot route from (0,0) to the same location,
        going through all needed products*/
        List<WarehouseLocation> route,

        /*Ordered: 10 boxes of milk, but available are only 5 => a missing item is { Milk : 10 }*/
        Map<String, Integer> missingItems,

        /*Ordered: 10 boxes of milk, but available are only - { Milk : 5 }*/
        Map<String, Integer> availableItems)
{}
