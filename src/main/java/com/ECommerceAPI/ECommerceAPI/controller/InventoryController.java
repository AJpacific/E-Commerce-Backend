package com.ECommerceAPI.ECommerceAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ECommerceAPI.ECommerceAPI.model.Product;
import com.ECommerceAPI.ECommerceAPI.service.InventoryService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/{productId}/add-stock")
    public ResponseEntity<Product> addStock(@PathVariable Long productId, @RequestBody Map<String, Integer> request) {
        try {
            Integer quantity = request.get("quantity");
            if (quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().build();
            }
            Product product = inventoryService.addStock(productId, quantity);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{productId}/reduce-stock")
    public ResponseEntity<Product> reduceStock(@PathVariable Long productId, @RequestBody Map<String, Integer> request) {
        try {
            Integer quantity = request.get("quantity");
            if (quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().build();
            }
            Product product = inventoryService.reduceStock(productId, quantity);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{productId}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long productId, @RequestBody Map<String, Integer> request) {
        try {
            Integer quantity = request.get("quantity");
            if (quantity == null || quantity < 0) {
                return ResponseEntity.badRequest().build();
            }
            Product product = inventoryService.updateStock(productId, quantity);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{productId}/min-stock")
    public ResponseEntity<Product> setMinStockLevel(@PathVariable Long productId, @RequestBody Map<String, Integer> request) {
        try {
            Integer minLevel = request.get("minLevel");
            if (minLevel == null || minLevel < 0) {
                return ResponseEntity.badRequest().build();
            }
            Product product = inventoryService.setMinStockLevel(productId, minLevel);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        List<Product> products = inventoryService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<Product>> getOutOfStockProducts() {
        List<Product> products = inventoryService.getOutOfStockProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        List<Product> products = inventoryService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}/check-stock")
    public ResponseEntity<Map<String, Object>> checkStockAvailability(@PathVariable Long productId, @RequestParam Integer quantity) {
        try {
            boolean available = inventoryService.checkStockAvailability(productId, quantity);
            Map<String, Object> response = Map.of(
                "productId", productId,
                "requestedQuantity", quantity,
                "available", available
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
