package com.shophub.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shophub.entity.CartEntity;
import com.shophub.model.CartItem;
import com.shophub.model.Product;
import com.shophub.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ObjectMapper   objectMapper;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
        this.objectMapper   = new ObjectMapper();
    }

    public List<CartItem> getCart(String sessionId) {
        return cartRepository.findBySessionId(sessionId)
                .stream().map(this::toCartItem).toList();
    }

    public double getCartTotal(String sessionId) {
        return getCart(sessionId).stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public int getCartItemCount(String sessionId) {
        return getCart(sessionId).stream().mapToInt(CartItem::getQty).sum();
    }

    @Transactional
    public CartItem addToCart(String sessionId, Product product, int qty, Map<String, String> variants) {
        String itemKey = buildItemKey(product.getId(), variants);
        Optional<CartEntity> existing = cartRepository.findBySessionIdAndItemKey(sessionId, itemKey);
        if (existing.isPresent()) {
            CartEntity entity = existing.get();
            entity.setQty(entity.getQty() + qty);
            cartRepository.save(entity);
            return toCartItem(entity);
        }
        CartEntity entity = new CartEntity();
        entity.setSessionId(sessionId);
        entity.setItemKey(itemKey);
        entity.setProductId(product.getId());
        entity.setName(product.getName());
        entity.setBrand(product.getBrand());
        entity.setPrice(product.getPrice());
        entity.setImage(product.getImage());
        entity.setQty(qty);
        entity.setVariantsJson(toJson(variants));
        cartRepository.save(entity);
        return toCartItem(entity);
    }

    @Transactional
    public boolean updateQty(String sessionId, String itemKey, int qty) {
        Optional<CartEntity> opt = cartRepository.findBySessionIdAndItemKey(sessionId, itemKey);
        if (opt.isEmpty()) return false;
        opt.get().setQty(Math.max(1, qty));
        cartRepository.save(opt.get());
        return true;
    }

    @Transactional
    public boolean removeItem(String sessionId, String itemKey) {
        if (cartRepository.findBySessionIdAndItemKey(sessionId, itemKey).isEmpty()) return false;
        cartRepository.deleteBySessionIdAndItemKey(sessionId, itemKey);
        return true;
    }

    @Transactional
    public void clearCart(String sessionId) {
        cartRepository.deleteBySessionId(sessionId);
    }

    private String buildItemKey(int productId, Map<String, String> variants) {
        StringBuilder sb = new StringBuilder().append(productId);
        if (variants != null) {
            variants.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .forEach(e -> sb.append(":").append(e.getKey()).append("=").append(e.getValue()));
        }
        return sb.toString();
    }

    private CartItem toCartItem(CartEntity e) {
        CartItem item = new CartItem();
        item.setItemKey(e.getItemKey());
        item.setProductId(e.getProductId());
        item.setName(e.getName());
        item.setBrand(e.getBrand());
        item.setPrice(e.getPrice());
        item.setImage(e.getImage());
        item.setQty(e.getQty());
        item.setVariants(fromJson(e.getVariantsJson()));
        return item;
    }

    private String toJson(Map<String, String> map) {
        try { return objectMapper.writeValueAsString(map != null ? map : new HashMap<>()); }
        catch (Exception ex) { return "{}"; }
    }

    private Map<String, String> fromJson(String json) {
        try {
            if (json == null || json.isBlank()) return new HashMap<>();
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception ex) { return new HashMap<>(); }
    }
}
