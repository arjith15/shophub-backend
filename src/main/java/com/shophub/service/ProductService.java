package com.shophub.service;

import com.shophub.data.ProductDataStore;
import com.shophub.dto.PricePoint;
import com.shophub.model.Category;
import com.shophub.model.Product;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Business logic for product listing, filtering, searching, and price history.
 * Mirrors the filter / search logic in app.js — applyFilters(), runSearch().
 */
@Service
public class ProductService {

    // ── Catalog ───────────────────────────────────────────────────────────────

    public List<Product> getAllProducts() {
        return ProductDataStore.PRODUCTS;
    }

    public List<Category> getAllCategories() {
        return ProductDataStore.CATEGORIES;
    }

    public Optional<Product> getById(int id) {
        return ProductDataStore.PRODUCTS.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    // ── Filter & Search ───────────────────────────────────────────────────────

    /**
     * Filter products by any combination of:
     *   category, brand, minPrice, maxPrice, size, color, storage, ram, query (text search)
     *
     * All parameters are optional — null / empty means "no filter on this field".
     */
    public List<Product> filter(String category, String brand,
                                Double minPrice, Double maxPrice,
                                String size, String color,
                                String storage, String ram,
                                String query) {

        return ProductDataStore.PRODUCTS.stream()
                .filter(p -> category == null || category.isBlank() || "all".equals(category)
                             || p.getCategory().equalsIgnoreCase(category))
                .filter(p -> brand == null || brand.isBlank()
                             || brand.equalsIgnoreCase(p.getBrand()))
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .filter(p -> size == null || size.isBlank()
                             || (p.getSizes() != null && p.getSizes().contains(size)))
                .filter(p -> color == null || color.isBlank()
                             || (p.getColors() != null && p.getColors().contains(color)))
                .filter(p -> storage == null || storage.isBlank()
                             || (p.getStorage() != null && p.getStorage().contains(storage)))
                .filter(p -> ram == null || ram.isBlank()
                             || (p.getRam() != null && p.getRam().contains(ram)))
                .filter(p -> query == null || query.isBlank() || matchesQuery(p, query))
                .collect(Collectors.toList());
    }

    private boolean matchesQuery(Product p, String q) {
        String lq = q.toLowerCase();
        return p.getName().toLowerCase().contains(lq)
            || (p.getBrand() != null && p.getBrand().toLowerCase().contains(lq))
            || p.getCategory().toLowerCase().contains(lq);
    }

    // ── Price History ─────────────────────────────────────────────────────────

    /**
     * Returns 12 months of mock price history for a product.
     * Algorithm mirrors getPriceHistory() in app.js.
     */
    public List<PricePoint> getPriceHistory(Product product) {
        List<PricePoint> history = new ArrayList<>();
        LocalDate now = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM yy");

        for (int i = 11; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            String label = month.format(fmt);

            double t      = (11 - i) / 11.0;
            double base   = product.getPrice() * (0.82 + t * 0.2);
            double wiggle = Math.sin(product.getId() + i) * product.getPrice() * 0.03;
            double price;

            if (i == 0) {
                price = product.getPrice();  // current month is actual price
            } else {
                price = Math.max(product.getPrice() * 0.7,
                         Math.min(product.getPrice() * 1.15, base + wiggle));
            }

            history.add(new PricePoint(label, Math.round(price)));
        }
        return history;
    }
}
