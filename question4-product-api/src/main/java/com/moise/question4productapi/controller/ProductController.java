package com.moise.question4productapi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moise.question4productapi.model.Product;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private List<Product> productList = new ArrayList<>();

    public ProductController() {
        // Sample 10 products
        productList.add(new Product(1L, "Phone", "Samsung phone", 999.99, "Electronics", 10, "Samsung"));
        productList.add(new Product(2L, "Laptop", "HP", 899.99, "Electronics", 5, "HP"));
        productList.add(new Product(3L, "Earphones", "Apple", 1999.99, "Electronics", 3, "Apple"));
        productList.add(new Product(4L, "Table", "Wooden table", 249.99, "Furniture", 20, "Wood"));
        productList.add(new Product(5L, "Chair", "Wodden chair", 120.00, "Furniture", 15, "Wood"));
        productList.add(new Product(6L, "Shoes", "Nike shoes", 60.00, "Fashion", 0, "Nike"));
        productList.add(new Product(7L, "TV", "Samsung Smart TV", 15.50, "Electronics", 50, "Samsung"));
        productList.add(new Product(8L, "Tablet", "Apple tablet", 650.00, "Electronics", 4, "Apple"));
        productList.add(new Product(9L, "Mouse", "HP mouse", 99.99, "Electronics", 12, "HP"));
        productList.add(new Product(10L, "Keyboard", "HP keyboard", 199.99, "Electronics", 7, "HP"));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        int fromIndex = page * limit;
        int toIndex = Math.min(fromIndex + limit, productList.size());
        if (fromIndex >= productList.size()) return ResponseEntity.ok(Collections.emptyList());

        return ResponseEntity.ok(productList.subList(fromIndex, toIndex));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        return productList.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> results = productList.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand) {
        List<Product> results = productList.stream()
                .filter(p -> p.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> results = productList.stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                             p.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam double min, @RequestParam double max) {
        List<Product> results = productList.stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getInStockProducts() {
        List<Product> results = productList.stream()
                .filter(p -> p.getStockQuantity() > 0)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product newProduct) {
        newProduct.setProductId((long) (productList.size() + 1));
        productList.add(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getProductId().equals(productId)) {
                updatedProduct.setProductId(productId);
                productList.set(i, updatedProduct);
                return ResponseEntity.ok(updatedProduct);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long productId, @RequestParam int quantity) {
        for (Product p : productList) {
            if (p.getProductId().equals(productId)) {
                p.setStockQuantity(quantity);
                return ResponseEntity.ok(p);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        boolean removed = productList.removeIf(p -> p.getProductId().equals(productId));
        if (removed) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
