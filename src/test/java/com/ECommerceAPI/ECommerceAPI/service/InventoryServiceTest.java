package com.ECommerceAPI.ECommerceAPI.service;

import com.ECommerceAPI.ECommerceAPI.model.Product;
import com.ECommerceAPI.ECommerceAPI.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        productRepository.deleteAll();

        // Create a test product
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setDescription("A test product for inventory testing");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setStockQuantity(100);
        testProduct.setMinStockLevel(10);
        testProduct.setIsAvailable(true);
        testProduct = productRepository.save(testProduct);
    }

    @Test
    void testAddStock() {
        int initialStock = testProduct.getStockQuantity();
        int stockToAdd = 50;

        Product updatedProduct = inventoryService.addStock(testProduct.getId(), stockToAdd);

        assertEquals(initialStock + stockToAdd, updatedProduct.getStockQuantity());
        assertTrue(updatedProduct.getIsAvailable());
    }

    @Test
    void testReduceStock() {
        int initialStock = testProduct.getStockQuantity();
        int stockToReduce = 30;

        Product updatedProduct = inventoryService.reduceStock(testProduct.getId(), stockToReduce);

        assertEquals(initialStock - stockToReduce, updatedProduct.getStockQuantity());
        assertTrue(updatedProduct.getIsAvailable());
    }

    @Test
    void testReduceStockToZero() {
        int stockToReduce = testProduct.getStockQuantity();

        Product updatedProduct = inventoryService.reduceStock(testProduct.getId(), stockToReduce);

        assertEquals(0, updatedProduct.getStockQuantity());
        assertFalse(updatedProduct.getIsAvailable());
    }

    @Test
    void testUpdateStock() {
        int newStock = 200;

        Product updatedProduct = inventoryService.updateStock(testProduct.getId(), newStock);

        assertEquals(newStock, updatedProduct.getStockQuantity());
        assertTrue(updatedProduct.getIsAvailable());
    }

    @Test
    void testSetMinStockLevel() {
        int newMinStock = 25;

        Product updatedProduct = inventoryService.setMinStockLevel(testProduct.getId(), newMinStock);

        assertEquals(newMinStock, updatedProduct.getMinStockLevel());
    }

    @Test
    void testGetLowStockProducts() {
        // Set a high minimum stock level to make the product low stock
        inventoryService.setMinStockLevel(testProduct.getId(), 150);

        List<Product> lowStockProducts = inventoryService.getLowStockProducts();

        assertEquals(1, lowStockProducts.size());
        assertEquals(testProduct.getId(), lowStockProducts.get(0).getId());
    }

    @Test
    void testGetOutOfStockProducts() {
        // Reduce stock to zero
        inventoryService.reduceStock(testProduct.getId(), testProduct.getStockQuantity());

        List<Product> outOfStockProducts = inventoryService.getOutOfStockProducts();

        assertEquals(1, outOfStockProducts.size());
        assertEquals(testProduct.getId(), outOfStockProducts.get(0).getId());
        assertEquals(0, outOfStockProducts.get(0).getStockQuantity());
    }

    @Test
    void testGetAvailableProducts() {
        List<Product> availableProducts = inventoryService.getAvailableProducts();

        assertEquals(1, availableProducts.size());
        assertEquals(testProduct.getId(), availableProducts.get(0).getId());
        assertTrue(availableProducts.get(0).getIsAvailable());
    }

    @Test
    void testCheckStockAvailability() {
        // Test with sufficient stock
        boolean isAvailable = inventoryService.checkStockAvailability(testProduct.getId(), 50);
        assertTrue(isAvailable);

        // Test with insufficient stock
        isAvailable = inventoryService.checkStockAvailability(testProduct.getId(), 150);
        assertFalse(isAvailable);
    }
}
