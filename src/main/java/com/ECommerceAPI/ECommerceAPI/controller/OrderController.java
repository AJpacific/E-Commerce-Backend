package com.ECommerceAPI.ECommerceAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ECommerceAPI.ECommerceAPI.model.Order;
import com.ECommerceAPI.ECommerceAPI.model.OrderStatus;
import com.ECommerceAPI.ECommerceAPI.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    public record OrderRequest(Set<Long> productIds) {}

    @PostMapping("/{userId}")
    public ResponseEntity<?> createOrder(@PathVariable Long userId, @RequestBody OrderRequest request) {
        try {
            Order newOrder = orderService.createOrder(userId, request.productIds());
            return ResponseEntity.ok(newOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Order creation failed"));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getOrdersForUser(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getOrdersForUser(userId);

            orders.forEach(order -> {
                if (order.getUser() != null) {
                    order.getUser().setPassword(null); // Hide password in response
                }
            });
            
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        if (order.isPresent()) {
            if (order.get().getUser() != null) {
                order.get().getUser().setPassword(null); // Hide password in response
            }
            return ResponseEntity.ok(order.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> request) {
        try {
            String statusStr = request.get("status");
            String trackingNumber = request.get("trackingNumber");
            
            if (statusStr == null) {
                return ResponseEntity.badRequest().build();
            }
            
            OrderStatus status = OrderStatus.valueOf(statusStr.toUpperCase());
            Order updatedOrder = orderService.updateOrderStatus(orderId, status, trackingNumber);
            
            if (updatedOrder.getUser() != null) {
                updatedOrder.getUser().setPassword(null); // Hide password in response
            }
            
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            List<Order> orders = orderService.getOrdersByStatus(orderStatus);
            
            orders.forEach(order -> {
                if (order.getUser() != null) {
                    order.getUser().setPassword(null); // Hide password in response
                }
            });
            
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
