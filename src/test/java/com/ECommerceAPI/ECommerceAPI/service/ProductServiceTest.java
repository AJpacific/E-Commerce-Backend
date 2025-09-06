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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

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
        testProduct.setDescription("A test product for unit testing");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setStockQuantity(100);
        testProduct.setMinStockLevel(10);
        testProduct.setIsAvailable(true);
    }

    @Test
    void testCreateProduct() {
        Product savedProduct = productService.createProduct(testProduct);

        assertNotNull(savedProduct.getId());
        assertEquals("Test Product", savedProduct.getName());
        assertEquals("A test product for unit testing", savedProduct.getDescription());
        assertEquals(new BigDecimal("99.99"), savedProduct.getPrice());
        assertEquals(100, savedProduct.getStockQuantity());
        assertEquals(10, savedProduct.getMinStockLevel());
        assertTrue(savedProduct.getIsAvailable());
    }

    @Test
    void testGetAllProducts() {
        // Create multiple products
        productService.createProduct(testProduct);

        Product product2 = new Product();
        product2.setName("Test Product 2");
        product2.setDescription("Another test product");
        product2.setPrice(new BigDecimal("49.99"));
        product2.setStockQuantity(50);
        product2.setMinStockLevel(5);
        product2.setIsAvailable(true);
        productService.createProduct(product2);

        List<Product> products = productService.getAllProducts();

        assertEquals(2, products.size());
    }

    @Test
    void testGetProductById() {
        Product savedProduct = productService.createProduct(testProduct);
        Optional<Product> foundProduct = productService.getProductById(savedProduct.getId());

        assertTrue(foundProduct.isPresent());
        assertEquals(savedProduct.getId(), foundProduct.get().getId());
        assertEquals("Test Product", foundProduct.get().getName());
    }

    @Test
    void testUpdateProduct() {
        Product savedProduct = productService.createProduct(testProduct);
        
        savedProduct.setName("Updated Product");
        savedProduct.setPrice(new BigDecimal("149.99"));
        
        Optional<Product> updatedProductOpt = productService.updateProduct(savedProduct.getId(), savedProduct);
        assertTrue(updatedProductOpt.isPresent());
        
        Product updatedProduct = updatedProductOpt.get();
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals(new BigDecimal("149.99"), updatedProduct.getPrice());
    }

    @Test
    void testDeleteProduct() {
        Product savedProduct = productService.createProduct(testProduct);
        Long productId = savedProduct.getId();

        productService.deleteProduct(productId);

        Optional<Product> deletedProduct = productService.getProductById(productId);
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    void testSearchProducts() {
        // Create products with different names
        productService.createProduct(testProduct);

        Product product2 = new Product();
        product2.setName("Laptop Computer");
        product2.setDescription("A high-end laptop");
        product2.setPrice(new BigDecimal("1299.99"));
        product2.setStockQuantity(25);
        product2.setMinStockLevel(5);
        product2.setIsAvailable(true);
        productService.createProduct(product2);

        List<Product> searchResults = productService.searchProducts("Test");
        assertEquals(1, searchResults.size());
        assertEquals("Test Product", searchResults.get(0).getName());

        searchResults = productService.searchProducts("Laptop");
        assertEquals(1, searchResults.size());
        assertEquals("Laptop Computer", searchResults.get(0).getName());
    }

    @Test
    void testGetAvailableProducts() {
        // Create available and unavailable products
        productService.createProduct(testProduct);

        Product unavailableProduct = new Product();
        unavailableProduct.setName("Unavailable Product");
        unavailableProduct.setDescription("This product is not available");
        unavailableProduct.setPrice(new BigDecimal("29.99"));
        unavailableProduct.setStockQuantity(0);
        unavailableProduct.setMinStockLevel(5);
        unavailableProduct.setIsAvailable(false);
        productService.createProduct(unavailableProduct);

        List<Product> availableProducts = productService.getAvailableProducts();
        assertEquals(1, availableProducts.size());
        assertEquals("Test Product", availableProducts.get(0).getName());
    }
}
