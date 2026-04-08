package com.shophub.controller;

import com.shophub.dto.ApiResponse;
import com.shophub.dto.PricePoint;
import com.shophub.model.Category;
import com.shophub.model.Product;
import com.shophub.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Product REST API
 *
 * GET /api/products                   — all products (with optional filter params)
 * GET /api/products/{id}              — single product
 * GET /api/products/{id}/price-history — 12-month price history
 * GET /api/categories                 — all categories
 * GET /api/products/search?q=...      — text search across name, brand, category
 */
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ── All categories ────────────────────────────────────────────────────────

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<Category>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.ok(productService.getAllCategories()));
    }

    // ── Product listing with optional filters ─────────────────────────────────

    /**
     * Supported query params (all optional):
     *   category, brand, minPrice, maxPrice, size, color, storage, ram, q (search text)
     *
     * Example:
     *   GET /api/products?category=mobiles&brand=Apple&minPrice=50000
     */
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String storage,
            @RequestParam(required = false) String ram,
            @RequestParam(required = false, name = "q") String query) {

        List<Product> results = productService.filter(
            category, brand, minPrice, maxPrice, size, color, storage, ram, query
        );

        return ResponseEntity.ok(
            ApiResponse.ok("Found " + results.size() + " product(s)", results)
        );
    }

    // ── Single product ────────────────────────────────────────────────────────

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable int id) {
        return productService.getById(id)
            .map(p -> ResponseEntity.ok(ApiResponse.ok(p)))
            .orElse(ResponseEntity.status(404)
                .body(ApiResponse.error("Product not found: " + id)));
    }

    // ── Price history ─────────────────────────────────────────────────────────

    @GetMapping("/products/{id}/price-history")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPriceHistory(@PathVariable int id) {
        return productService.getById(id)
            .map(p -> {
                List<PricePoint> history = productService.getPriceHistory(p);
                Map<String, Object> payload = Map.of(
                    "productId",   p.getId(),
                    "productName", p.getName(),
                    "currentPrice", p.getPrice(),
                    "history",     history
                );
                return ResponseEntity.ok(ApiResponse.ok(payload));
            })
            .orElse(ResponseEntity.status(404)
                .body(ApiResponse.error("Product not found: " + id)));
    }

    // ── Text search shortcut ──────────────────────────────────────────────────

    @GetMapping("/products/search")
    public ResponseEntity<ApiResponse<List<Product>>> search(
            @RequestParam String q) {
        List<Product> results = productService.filter(
            null, null, null, null, null, null, null, null, q
        );
        return ResponseEntity.ok(
            ApiResponse.ok("Found " + results.size() + " result(s) for: " + q, results)
        );
    }
}
