package com.ECommerceAPI.ECommerceAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ECommerceAPI.ECommerceAPI.model.Order;
import com.ECommerceAPI.ECommerceAPI.model.OrderStatus;
import com.ECommerceAPI.ECommerceAPI.model.Product;
import com.ECommerceAPI.ECommerceAPI.model.User;
import com.ECommerceAPI.ECommerceAPI.repository.OrderRepository;
import com.ECommerceAPI.ECommerceAPI.repository.ProductRepository;
import com.ECommerceAPI.ECommerceAPI.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(Long userId, Set<Long> productIds) {
        // Input validation
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product IDs cannot be null or empty");
        }
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id : " +userId));

        List<Product> productsFound = productRepository.findAllById(productIds);
        if (productsFound.size() != productIds.size()) {          
              throw new RuntimeException("One or more products not found");
        }

        Set<Product> productsToOrder = new HashSet<>(productsFound);

        BigDecimal totalPrice = productsToOrder.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setProducts(productsToOrder);
        order.setTotalAmount(totalPrice);
        order.setOrderDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public List<Order> getOrdersForUser (Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id : " + userId));
        return orderRepository.findByUser(user);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus newStatus, String trackingNumber) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        order.setStatus(newStatus);
        if (trackingNumber != null && !trackingNumber.trim().isEmpty()) {
            order.setTrackingNumber(trackingNumber);
        }
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findAll().stream()
            .filter(order -> order.getStatus() == status)
            .toList();
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
