package com.groceryshop.groceryshop.util;

import com.groceryshop.groceryshop.model.CartItem;
import com.groceryshop.groceryshop.model.WarehouseLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BotPathCalculator {

    public static List<WarehouseLocation> calculateRoute(List<CartItem> items) {

        // Collecting the unique products locations that must be visited
        Set<WarehouseLocation> locationSet = new HashSet<>();
        for (CartItem item : items) {
            locationSet.add(item.product().getLocation());
        }
        List<WarehouseLocation> locations = new ArrayList<>(locationSet);
        WarehouseLocation start = new WarehouseLocation(0, 0);

        // Finding the best route
        List<WarehouseLocation> route;
        if (locations.size() <= 8) {
            route = tspBruteForce(locations, start); // brute force algorithm
        } else {
            route = tsp2opt(tspGreedy(locations, start)); // combination greedy and 2-opt algorithms
        }

        // Converting the order of points to a complete route
        List<WarehouseLocation> fullPath = new ArrayList<>();
        WarehouseLocation prev = route.getFirst();
        fullPath.add(prev);
        for (int i = 1; i < route.size(); i++) {
            WarehouseLocation next = route.get(i);
            addPath(prev, next, fullPath);
            prev = next;
        }
        return fullPath;
    }

    // Adds all intermediate (valid) steps between two locations
    private static void addPath(WarehouseLocation from, WarehouseLocation to, List<WarehouseLocation> path) {
        int x = from.x();
        int y = from.y();
        while (x != to.x()) {
            x += Integer.compare(to.x(), x); // +1 or -1
            path.add(new WarehouseLocation(x, y));
        }
        while (y != to.y()) {
            y += Integer.compare(to.y(), y); // +1 or -1
            path.add(new WarehouseLocation(x, y));
        }
    }

    // Brute force algorithm
    private static List<WarehouseLocation> tspBruteForce(List<WarehouseLocation> locations, WarehouseLocation start) {
        List<List<WarehouseLocation>> allPerms = permutations(locations);
        List<WarehouseLocation> bestRoute = null;
        int minDist = Integer.MAX_VALUE;
        for (List<WarehouseLocation> perm : allPerms) {
            int dist = 0;
            WarehouseLocation curr = start;
            for (WarehouseLocation next : perm) {
                dist += manhattan(curr, next);
                curr = next;
            }
            dist += manhattan(curr, start);
            if (dist < minDist) {
                minDist = dist;
                bestRoute = new ArrayList<>();
                bestRoute.add(start);
                bestRoute.addAll(perm);
                bestRoute.add(start);
            }
        }
        return bestRoute;
    }

    // Greedy algorithm
    private static List<WarehouseLocation> tspGreedy(List<WarehouseLocation> locations, WarehouseLocation start) {
        List<WarehouseLocation> toVisit = new ArrayList<>(locations);
        List<WarehouseLocation> route = new ArrayList<>();
        WarehouseLocation curr = start;
        route.add(curr);

        while (!toVisit.isEmpty()) {
            WarehouseLocation nearest = toVisit.getFirst();
            int minDist = manhattan(curr, nearest);
            for (WarehouseLocation loc : toVisit) {
                int dist = manhattan(curr, loc);
                if (dist < minDist) {
                    minDist = dist;
                    nearest = loc;
                }
            }
            route.add(nearest);
            curr = nearest;
            toVisit.remove(nearest);
        }

        route.add(start);
        return route;
    }

    // 2-opt local optimization
    private static List<WarehouseLocation> tsp2opt(List<WarehouseLocation> route) {
        boolean improvement = true;
        int maxIterations = 1000;
        int count = 0;
        while (improvement && count < maxIterations) {
            improvement = false;
            for (int i = 1; i < route.size() - 2; i++) {
                for (int k = i + 1; k < route.size() - 1; k++) {
                    int delta = -manhattan(route.get(i - 1), route.get(i)) - manhattan(route.get(k), route.get(k + 1))
                            + manhattan(route.get(i - 1), route.get(k)) + manhattan(route.get(i), route.get(k + 1));
                    if (delta < 0) {
                        Collections.reverse(route.subList(i, k + 1));
                        improvement = true;
                    }
                }
            }
            count++;
        }
        return route;
    }

    // Permutations for brute force
    private static List<List<WarehouseLocation>> permutations(List<WarehouseLocation> input) {
        List<List<WarehouseLocation>> result = new ArrayList<>();
        permute(input, 0, result);
        return result;
    }
    private static void permute(List<WarehouseLocation> arr, int k, List<List<WarehouseLocation>> result) {
        if (k == arr.size()) {
            result.add(new ArrayList<>(arr));
        } else {
            for (int i = k; i < arr.size(); i++) {
                Collections.swap(arr, i, k);
                permute(arr, k + 1, result);
                Collections.swap(arr, i, k);
            }
        }
    }

    private static int manhattan(WarehouseLocation a, WarehouseLocation b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }
}
