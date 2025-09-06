# 🛒 E-Commerce Backend API

A comprehensive, production-ready REST API for modern e-commerce platforms built with Spring Boot 3. This API provides complete functionality for managing products, users, shopping carts, orders, payments, and inventory with robust security and role-based access control.

## ✨ Features

### 🔐 **Authentication & Security**
- **JWT-based Authentication** with secure token generation and validation
- **Role-based Access Control** (Admin, Manager, Customer roles)
- **Password Security** with BCrypt hashing and strength validation
- **Protected Endpoints** with proper authorization
- **Stateless Security** using JWT tokens

### 👤 **User Management**
- User registration with validation
- User profile management
- Role assignment and management
- User search and filtering by role
- Secure password handling

### 🛍️ **Product Management**
- Complete CRUD operations for products
- **Advanced Search & Filtering** by name, price, availability
- **Inventory Tracking** with stock management
- **Low Stock Alerts** and availability status
- Price range filtering and availability filtering

### 🛒 **Shopping Cart Management**
- Add/remove products from cart
- Automatic total calculation
- Cart persistence per user
- Clear cart functionality
- Real-time cart updates

### 📦 **Order Management**
- Order creation with multiple products
- **Order Status Tracking** (Pending, Confirmed, Processing, Shipped, Delivered, Cancelled, Refunded)
- Order history for users
- Tracking number support
- Status update timestamps

### 💳 **Payment Integration**
- Multiple payment methods (Credit Card, PayPal, Bank Transfer, Cash on Delivery)
- **Payment Status Tracking** (Pending, Processing, Completed, Failed, Cancelled, Refunded)
- Transaction ID generation and tracking
- Payment processing simulation
- Refund management

### 📊 **Inventory Management**
- Stock quantity tracking
- Minimum stock level alerts
- Stock availability management
- Bulk stock operations
- Low stock and out-of-stock reporting

## 🏗️ **Architecture**

- **3-Tier Architecture**: Controller → Service → Repository
- **Spring Boot 3** with Java 21
- **Spring Data JPA** with Hibernate
- **MySQL Database** with automatic schema management
- **Maven Build System**
- **RESTful API Design**

## 🚀 **Quick Start**

### Prerequisites
- Java 21 or later
- Maven 3.6+
- MySQL Server 8.0+

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd E-Commerce-Backend
   ```

2. **Configure Database**
   - Start MySQL server
   - Set environment variable for database password:
     ```bash
     # Windows
     set DB_PASSWORD=your_mysql_password
     
     # macOS/Linux
     export DB_PASSWORD=your_mysql_password
     ```

3. **Run the Application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the API**
   - Base URL: `http://localhost:8080`
   - API Documentation: Available at `/api/` endpoints

## 📚 **API Documentation**

### 🔐 Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| `POST` | `/api/auth/login` | User login and JWT token generation | Public |

### 👤 User Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| `POST` | `/api/users/register` | Register new user | Public |
| `GET` | `/api/users` | Get all users | Admin/Manager |
| `GET` | `/api/users/{id}` | Get user by ID | Admin/Manager |
| `PUT` | `/api/users/{id}` | Update user profile | Admin/Manager |
| `PUT` | `/api/users/{id}/role` | Update user role | Admin/Manager |
| `GET` | `/api/users/role/{role}` | Get users by role | Admin/Manager |

### 🛍️ Product Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| `GET` | `/api/products` | Get all products | Public |
| `GET` | `/api/products/{id}` | Get product by ID | Public |
| `POST` | `/api/products` | Create new product | Admin/Manager |
| `PUT` | `/api/products/{id}` | Update product | Admin/Manager |
| `DELETE` | `/api/products/{id}` | Delete product | Admin/Manager |
| `GET` | `/api/products/search` | Search products by name | Public |
| `GET` | `/api/products/filter/price` | Filter by price range | Public |
| `GET` | `/api/products/available` | Get available products | Public |
| `GET` | `/api/products/in-stock` | Get products in stock | Public |
| `GET` | `/api/products/search-filter` | Advanced search & filter | Public |

### 🛒 Shopping Cart

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| `GET` | `/api/cart/{userId}` | Get user's cart | Customer/Admin/Manager |
| `POST` | `/api/cart/{userId}/add/{productId}` | Add product to cart | Customer/Admin/Manager |
| `DELETE` | `/api/cart/{userId}/remove/{productId}` | Remove product from cart | Customer/Admin/Manager |
| `DELETE` | `/api/cart/{userId}/clear` | Clear user's cart | Customer/Admin/Manager |

### 📦 Order Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| `POST` | `/api/orders/{userId}` | Create new order | Customer/Admin/Manager |
| `GET` | `/api/orders/{userId}` | Get user's orders | Customer/Admin/Manager |
| `GET` | `/api/orders/{orderId}` | Get order by ID | Customer/Admin/Manager |
| `PUT` | `/api/orders/{orderId}/status` | Update order status | Admin/Manager |
| `GET` | `/api/orders/status/{status}` | Get orders by status | Admin/Manager |

### 💳 Payment Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| `POST` | `/api/payments/order/{orderId}` | Create payment for order | Customer/Admin/Manager |
| `POST` | `/api/payments/{paymentId}/process` | Process payment | Customer/Admin/Manager |
| `POST` | `/api/payments/{paymentId}/refund` | Refund payment | Admin/Manager |
| `GET` | `/api/payments/order/{orderId}` | Get payment by order ID | Customer/Admin/Manager |
| `GET` | `/api/payments/{paymentId}` | Get payment by ID | Customer/Admin/Manager |
| `GET` | `/api/payments/status/{status}` | Get payments by status | Admin/Manager |

### 📊 Inventory Management

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| `POST` | `/api/inventory/{productId}/add-stock` | Add stock to product | Admin/Manager |
| `POST` | `/api/inventory/{productId}/reduce-stock` | Reduce stock from product | Admin/Manager |
| `PUT` | `/api/inventory/{productId}/stock` | Update product stock | Admin/Manager |
| `PUT` | `/api/inventory/{productId}/min-stock` | Set minimum stock level | Admin/Manager |
| `GET` | `/api/inventory/low-stock` | Get low stock products | Admin/Manager |
| `GET` | `/api/inventory/out-of-stock` | Get out of stock products | Admin/Manager |
| `GET` | `/api/inventory/available` | Get available products | Admin/Manager |

## 🔧 **Configuration**

### Environment Variables
```bash
DB_PASSWORD=your_mysql_password
```

### Application Properties
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT Configuration
jwt.secret=aVeryLongAndSecureSecretKeyThatIsAtLeast256BitsLongForHS256
jwt.expiration=86400000
```

## 🧪 **Testing**

### Using Postman

1. **Register a User**
   ```bash
   POST http://localhost:8080/api/users/register
   Content-Type: application/json
   
   {
     "username": "testuser",
     "email": "test@example.com",
     "password": "password123"
   }
   ```

2. **Login**
   ```bash
   POST http://localhost:8080/api/auth/login
   Content-Type: application/json
   
   {
     "username": "testuser",
     "password": "password123"
   }
   ```

3. **Create a Product** (Admin/Manager only)
   ```bash
   POST http://localhost:8080/api/products
   Authorization: Bearer <your-jwt-token>
   Content-Type: application/json
   
   {
     "name": "Wireless Keyboard",
     "description": "A mechanical keyboard with RGB lighting",
     "price": 79.99,
     "stockQuantity": 50,
     "minStockLevel": 5
   }
   ```

4. **Add Product to Cart**
   ```bash
   POST http://localhost:8080/api/cart/1/add/1
   Authorization: Bearer <your-jwt-token>
   ```

## 🔒 **Security Features**

- **JWT Authentication**: Secure token-based authentication
- **Role-based Access Control**: Different permissions for Admin, Manager, and Customer
- **Password Security**: BCrypt hashing with strength validation
- **Input Validation**: Comprehensive validation for all inputs
- **Error Handling**: Secure error responses without information leakage
- **CSRF Protection**: Disabled for API usage
- **Stateless Sessions**: No server-side session storage

## 📊 **Data Models**

### User
- `id`: Unique identifier
- `username`: Unique username
- `email`: Unique email address
- `password`: Hashed password
- `role`: User role (ADMIN, MANAGER, CUSTOMER)

### Product
- `id`: Unique identifier
- `name`: Product name
- `description`: Product description
- `price`: Product price (BigDecimal)
- `stockQuantity`: Available stock
- `isAvailable`: Availability status
- `minStockLevel`: Minimum stock threshold

### Order
- `id`: Unique identifier
- `user`: Associated user
- `products`: Set of ordered products
- `totalAmount`: Order total
- `orderDate`: Order creation date
- `status`: Order status
- `trackingNumber`: Shipping tracking number

### Payment
- `id`: Unique identifier
- `order`: Associated order
- `paymentMethod`: Payment method used
- `status`: Payment status
- `amount`: Payment amount
- `transactionId`: Unique transaction ID
- `paymentDate`: Payment date

## 🚀 **Deployment**

### Production Considerations
1. **Database**: Use production MySQL instance
2. **JWT Secret**: Use a strong, unique secret key
3. **Environment Variables**: Set secure environment variables
4. **HTTPS**: Enable HTTPS in production
5. **Logging**: Configure appropriate logging levels
6. **Monitoring**: Set up application monitoring

### Docker Support
```dockerfile
FROM openjdk:21-jdk-slim
COPY target/ECommerceAPI-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 **Contributing**

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 **License**

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 **Support**

For support and questions:
- Create an issue in the repository
- Check the API documentation
- Review the test examples

## 🎯 **Roadmap**

- [ ] Email notifications
- [ ] File upload for product images
- [ ] Advanced reporting and analytics
- [ ] Multi-currency support
- [ ] API rate limiting
- [ ] Caching implementation
- [ ] Microservices architecture
- [ ] GraphQL API support

---

**Built with ❤️ using Spring Boot 3 and Java 21**