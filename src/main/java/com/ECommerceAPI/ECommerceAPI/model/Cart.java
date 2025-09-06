package com.ECommerceAPI.ECommerceAPI.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "cart_products",
        joinColumns = @JoinColumn(name = "cart_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    public Cart() {
        this.totalAmount = BigDecimal.ZERO;
        this.lastUpdated = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
        this.lastUpdated = LocalDateTime.now();
        calculateTotal();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void addProduct(Product product) {
        this.products.add(product);
        this.lastUpdated = LocalDateTime.now();
        calculateTotal();
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
        this.lastUpdated = LocalDateTime.now();
        calculateTotal();
    }

    public void clearCart() {
        this.products.clear();
        this.totalAmount = BigDecimal.ZERO;
        this.lastUpdated = LocalDateTime.now();
    }

    private void calculateTotal() {
        if (products == null) {
            this.totalAmount = BigDecimal.ZERO;
            return;
        }
        this.totalAmount = products.stream()
            .filter(product -> product != null && product.getPrice() != null)
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
