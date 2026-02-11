package com.moise.question3restaurantapi.controller;

import com.moise.question3restaurantapi.model.MenuItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private List<MenuItem> menuList = new ArrayList<>();
    public MenuController() {
        menuList.add(new MenuItem(1L, "Salad", "Salad with tomatoes, carrots, etc", 5.99, "Appetizer", true));
        menuList.add(new MenuItem(2L, "Beef", "Cow meat", 4.50, "Appetizer", true));
        menuList.add(new MenuItem(3L, "Chicken", "Hen meat", 12.99, "Main Course", true));
        menuList.add(new MenuItem(4L, "Spaghetti", "Classic spaghetti", 10.50, "Main Course", true));
        menuList.add(new MenuItem(5L, "Cake", "Cheesecake", 6.00, "Dessert", true));
        menuList.add(new MenuItem(6L, "Pizza", "Pizza with cheese and beef", 4.75, "Dessert", false));
        menuList.add(new MenuItem(7L, "Coca-Cola", "Soft drink", 2.00, "Beverage", true));
        menuList.add(new MenuItem(8L, "Orange Juice", "Inyange orange juice", 3.50, "Beverage", false));
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenu() {
        return ResponseEntity.ok(menuList);
    }
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        return menuList.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getMenuByCategory(@PathVariable String category) {
        List<MenuItem> results = menuList.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }
    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailableItems(@RequestParam boolean available) {
        List<MenuItem> results = menuList.stream()
                .filter(item -> item.isAvailable() == available)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }
    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchMenuByName(@RequestParam String name) {
        List<MenuItem> results = menuList.stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }
    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem newItem) {
        newItem.setId((long) (menuList.size() + 1));
        menuList.add(newItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }
    @PutMapping("/{id}/availability")
    public ResponseEntity<MenuItem> toggleAvailability(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        for (MenuItem item : menuList) {
            if (item.getId().equals(id)) {
                Boolean available = body.get("available");
                if (available != null) item.setAvailable(available);
                return ResponseEntity.ok(item);
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        boolean removed = menuList.removeIf(item -> item.getId().equals(id));
        if (removed) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
