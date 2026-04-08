package com.shophub.controller;

import com.shophub.dto.ApiResponse;
import com.shophub.dto.CheckoutRequest;
import com.shophub.model.CartItem;
import com.shophub.model.Order;
import com.shophub.service.CartService;
import com.shophub.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Order REST API
 *
 * POST /api/orders/checkout    — place an order from the active cart
 * GET  /api/orders/{orderId}   — get order details + status
 * GET  /api/orders             — list all orders (admin use)
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService  cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService  = cartService;
    }

    // ── Checkout ──────────────────────────────────────────────────────────────

    /**
     * Place an order.
     * 1. Load cart for the given sessionId.
     * 2. Validate and create the order.
     * 3. Clear the cart on success.
     *
     * Mirrors the form-submit handler in payment.js.
     */
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkout(
            @Valid @RequestBody CheckoutRequest req) {

        List<CartItem> cart = cartService.getCart(req.sessionId);
        if (cart.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Cart is empty — cannot place order"));
        }

        try {
            Order order = orderService.placeOrder(req, cart);
            cartService.clearCart(req.sessionId);          // clear cart post-order

            Map<String, Object> payload = Map.of(
                "orderId",   order.getOrderId(),
                "status",    order.getStatus(),
                "total",     order.getTotal(),
                "message",   req.paymentMode.equalsIgnoreCase("cod")
                                 ? "Order placed! Pay on delivery."
                                 : "Payment confirmed! Thank you for your order."
            );
            return ResponseEntity.ok(ApiResponse.ok("Order placed successfully", payload));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(ex.getMessage()));
        }
    }

    // ── Order status ──────────────────────────────────────────────────────────

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Order>> getOrder(@PathVariable String orderId) {
        return orderService.getOrder(orderId)
            .map(o -> ResponseEntity.ok(ApiResponse.ok(o)))
            .orElse(ResponseEntity.status(404)
                .body(ApiResponse.error("Order not found: " + orderId)));
    }

    // ── Admin: all orders ─────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getAllOrders()));
    }
}
