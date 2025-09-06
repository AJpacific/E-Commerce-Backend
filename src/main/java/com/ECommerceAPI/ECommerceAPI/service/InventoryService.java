package com.ECommerceAPI.ECommerceAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ECommerceAPI.ECommerceAPI.model.Product;
import com.ECommerceAPI.ECommerceAPI.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    public Product addStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        product.addStock(quantity);
        return productRepository.save(product);
    }

    public Product reduceStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        if (!product.reduceStock(quantity)) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity() + ", Requested: " + quantity);
        }
        
        return productRepository.save(product);
    }

    public Product updateStock(Long productId, Integer newQuantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        product.setStockQuantity(newQuantity);
        return productRepository.save(product);
    }

    public Product setMinStockLevel(Long productId, Integer minLevel) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        product.setMinStockLevel(minLevel);
        return productRepository.save(product);
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findAll().stream()
            .filter(Product::isLowStock)
            .toList();
    }

    public List<Product> getOutOfStockProducts() {
        return productRepository.findAll().stream()
            .filter(product -> !product.isInStock())
            .toList();
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findAll().stream()
            .filter(Product::isInStock)
            .toList();
    }

    public boolean checkStockAvailability(Long productId, Integer quantity) {
        Optional<Product> product = productRepository.findById(productId);
        return product.isPresent() && product.get().getStockQuantity() >= quantity;
    }
}
