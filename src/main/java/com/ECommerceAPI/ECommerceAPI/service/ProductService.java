package com.ECommerceAPI.ECommerceAPI.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ECommerceAPI.ECommerceAPI.model.Product;
import com.ECommerceAPI.ECommerceAPI.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        // Input validation
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be null or negative");
        }
        if (product.getStockQuantity() == null) {
            product.setStockQuantity(0);
        }
        if (product.getMinStockLevel() == null) {
            product.setMinStockLevel(5);
        }
        
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                       .map(existingProduct -> {
                            existingProduct.setName(productDetails.getName());
                            existingProduct.setDescription(productDetails.getDescription());
                            existingProduct.setPrice(productDetails.getPrice());
                            if (productDetails.getStockQuantity() != null) {
                                existingProduct.setStockQuantity(productDetails.getStockQuantity());
                            }
                            if (productDetails.getMinStockLevel() != null) {
                                existingProduct.setMinStockLevel(productDetails.getMinStockLevel());
                            }
                            return productRepository.save(existingProduct);
                       });

    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByNameContainingIgnoreCase(name.trim());
    }

    public List<Product> filterProductsByPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null && maxPrice == null) {
            return getAllProducts();
        }
        if (minPrice == null) {
            minPrice = BigDecimal.ZERO;
        }
        if (maxPrice == null) {
            maxPrice = new BigDecimal("999999.99");
        }
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findByIsAvailableTrue();
    }

    public List<Product> getUnavailableProducts() {
        return productRepository.findByIsAvailableFalse();
    }

    public List<Product> getProductsInStock() {
        return productRepository.findByStockQuantityGreaterThan(0);
    }

    public List<Product> getOutOfStockProducts() {
        return productRepository.findByStockQuantityLessThanEqual(0);
    }

    public List<Product> searchAndFilterProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Boolean available) {
        return productRepository.findProductsWithFilters(name, minPrice, maxPrice, available);
    }
}
