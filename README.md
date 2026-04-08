# ShopHub Backend — Spring Boot REST API

A full Java Spring Boot backend for the ShopHub e-commerce frontend.
Mirrors all business logic from `app.js`, `chatbot.js`, `payment.js`, and `outfit.js`.

---

## Tech Stack
| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.2.5 |
| Language | Java 17 |
| Validation | Jakarta Validation (`@Valid`) |
| Build | Maven |
| Storage | In-memory (swap to JPA + PostgreSQL for production) |

---

## Project Structure
```
src/main/java/com/shophub/
├── ShopHubApplication.java          ← Entry point
├── config/
│   └── CorsConfig.java              ← CORS (allows frontend calls)
├── model/
│   ├── Product.java
│   ├── Category.java
│   ├── CartItem.java
│   ├── Order.java
│   └── OrderItem.java
├── dto/
│   ├── ApiResponse.java             ← { success, message, data }
│   ├── AddToCartRequest.java
│   ├── UpdateCartQtyRequest.java
│   ├── CheckoutRequest.java
│   ├── ChatRequest.java
│   ├── PricePoint.java
│   └── OutfitRequest.java
├── data/
│   └── ProductDataStore.java        ← In-memory product catalog
├── service/
│   ├── ProductService.java          ← Filter, search, price history
│   ├── CartService.java             ← Session-based cart
│   ├── OrderService.java            ← Order creation & tracking
│   └── ChatbotService.java          ← Product usage knowledge base
└── controller/
    ├── ProductController.java
    ├── CartController.java
    ├── OrderController.java
    ├── ChatbotController.java
    ├── OutfitController.java
    └── GlobalExceptionHandler.java
```

---

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+

### Run
```bash
./mvnw spring-boot:run
# Server starts at http://localhost:8080
```

---

## API Reference

All responses follow this envelope:
```json
{ "success": true, "message": "OK", "data": { ... } }
```

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/categories` | All 8 product categories |
| `GET` | `/api/products` | All products (filterable) |
| `GET` | `/api/products/{id}` | Single product |
| `GET` | `/api/products/{id}/price-history` | 12-month price chart data |
| `GET` | `/api/products/search?q=iphone` | Text search |

**Filter params** (all optional):
`category`, `brand`, `minPrice`, `maxPrice`, `size`, `color`, `storage`, `ram`, `q`

```bash
# All Apple mobiles under ₹1,00,000
curl "http://localhost:8080/api/products?category=mobiles&brand=Apple&maxPrice=100000"

# Search
curl "http://localhost:8080/api/products/search?q=gaming"

# Price history
curl "http://localhost:8080/api/products/401/price-history"
```

---

### Cart

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/cart/{sessionId}` | Get cart + total |
| `POST` | `/api/cart/add` | Add item |
| `PUT` | `/api/cart/update` | Update quantity |
| `DELETE` | `/api/cart/{sessionId}/remove/{itemKey}` | Remove item |
| `DELETE` | `/api/cart/{sessionId}` | Clear cart |

```bash
# Add to cart
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "sess_abc123",
    "productId": 401,
    "qty": 1,
    "variants": { "color": "Blue", "storage": "256GB" }
  }'

# View cart
curl "http://localhost:8080/api/cart/sess_abc123"
```

---

### Orders / Checkout

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/orders/checkout` | Place order from cart |
| `GET` | `/api/orders/{orderId}` | Order status |
| `GET` | `/api/orders` | All orders (admin) |

```bash
curl -X POST http://localhost:8080/api/orders/checkout \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "sess_abc123",
    "customerName": "Priya Sharma",
    "customerEmail": "priya@example.com",
    "customerPhone": "+91 9876543210",
    "addressLine1": "42 MG Road",
    "city": "Bangalore",
    "state": "Karnataka",
    "pincode": "560001",
    "country": "India",
    "paymentMode": "card"
  }'
```

---

### Chatbot

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/chatbot/message` | Product usage query |

```bash
curl -X POST http://localhost:8080/api/chatbot/message \
  -H "Content-Type: application/json" \
  -d '{ "message": "how do I use my yoga mat?" }'
```

---

### Outfit Studio

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/outfit/body-types` | Supported body types |
| `POST` | `/api/outfit/recommend` | Get outfit recommendations |

```bash
curl -X POST http://localhost:8080/api/outfit/recommend \
  -H "Content-Type: application/json" \
  -d '{ "bodyType": "Athletic", "stylePrefs": ["Casual", "Smart-Casual"] }'
```

---

## Connecting the Frontend

In your frontend JS files, replace `localStorage`-based cart/checkout with API calls:

```js
// Add to cart
await fetch('http://localhost:8080/api/cart/add', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    sessionId: getSessionId(),   // generate/store in localStorage
    productId: product.id,
    qty: 1,
    variants: selectedVariants
  })
});

// Checkout (payment.js submit handler)
await fetch('http://localhost:8080/api/orders/checkout', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ sessionId, ...formData })
});
```

---

## Production Upgrades

| Feature | Current | Recommended |
|---------|---------|-------------|
| Product data | In-memory (`ProductDataStore`) | JPA + PostgreSQL / MySQL |
| Cart | In-memory `ConcurrentHashMap` | Redis |
| Orders | In-memory | JPA + PostgreSQL |
| Auth | None | Spring Security + JWT |
| Payment | Mock | Razorpay / Stripe SDK |
| CORS | `allowedOrigins("*")` | Lock to your domain |
