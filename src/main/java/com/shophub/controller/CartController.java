package com.shophub.controller;

import com.shophub.dto.AddToCartRequest;
import com.shophub.dto.ApiResponse;
import com.shophub.dto.UpdateCartQtyRequest;
import com.shophub.model.CartItem;
import com.shophub.model.Product;
import com.shophub.service.CartService;
import com.shophub.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Cart REST API
 *
 * GET    /api/cart/{sessionId}                          — get cart items + total
 * POST   /api/cart/add                                  — add item to cart
 * PUT    /api/cart/update                               — update item qty
 * DELETE /api/cart/{sessionId}/remove/{itemKey}         — remove one item
 * DELETE /api/cart/{sessionId}                          — clear entire cart
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService    = cartService;
        this.productService = productService;
    }

    // ── Get cart ──────────────────────────────────────────────────────────────

    @GetMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCart(
            @PathVariable String sessionId) {

        List<CartItem> items = cartService.getCart(sessionId);
        Map<String, Object> payload = Map.of(
            "sessionId",  sessionId,
            "items",      items,
            "itemCount",  cartService.getCartItemCount(sessionId),
            "total",      cartService.getCartTotal(sessionId)
        );
        return ResponseEntity.ok(ApiResponse.ok(payload));
    }

    // ── Add item ──────────────────────────────────────────────────────────────

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartItem>> addToCart(
            @Valid @RequestBody AddToCartRequest req) {

        Optional<Product> product = productService.getById(req.productId);
        if (product.isEmpty()) {
            return ResponseEntity.status(404)
                .body(ApiResponse.error("Product not found: " + req.productId));
        }

        CartItem added = cartService.addToCart(
            req.sessionId, product.get(), req.qty, req.variants
        );
        return ResponseEntity.ok(ApiResponse.ok("Item added to cart", added));
    }

    // ── Update quantity ───────────────────────────────────────────────────────

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<String>> updateQty(
            @Valid @RequestBody UpdateCartQtyRequest req) {

        boolean updated = cartService.updateQty(req.sessionId, req.itemKey, req.qty);
        if (!updated) {
            return ResponseEntity.status(404)
                .body(ApiResponse.error("Cart item not found: " + req.itemKey));
        }
        return ResponseEntity.ok(ApiResponse.ok("Quantity updated", "OK"));
    }

    // ── Remove item ───────────────────────────────────────────────────────────

    @DeleteMapping("/{sessionId}/remove/{itemKey}")
    public ResponseEntity<ApiResponse<String>> removeItem(
            @PathVariable String sessionId,
            @PathVariable String itemKey) {

        boolean removed = cartService.removeItem(sessionId, itemKey);
        if (!removed) {
            return ResponseEntity.status(404)
                .body(ApiResponse.error("Cart item not found: " + itemKey));
        }
        return ResponseEntity.ok(ApiResponse.ok("Item removed", "OK"));
    }

    // ── Clear cart ────────────────────────────────────────────────────────────

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<String>> clearCart(
            @PathVariable String sessionId) {

        cartService.clearCart(sessionId);
        return ResponseEntity.ok(ApiResponse.ok("Cart cleared", "OK"));
    }
}
