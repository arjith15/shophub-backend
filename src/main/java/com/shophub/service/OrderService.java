package com.shophub.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shophub.dto.CheckoutRequest;
import com.shophub.entity.OrderEntity;
import com.shophub.model.CartItem;
import com.shophub.model.Order;
import com.shophub.model.OrderItem;
import com.shophub.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper    objectMapper;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.objectMapper    = new ObjectMapper();
    }

    public Order placeOrder(CheckoutRequest req, List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty())
            throw new IllegalArgumentException("Cart is empty — cannot place order");

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setProductId(ci.getProductId());
            oi.setName(ci.getName());
            oi.setBrand(ci.getBrand());
            oi.setUnitPrice(ci.getPrice());
            oi.setQty(ci.getQty());
            oi.setVariants(ci.getVariants());
            oi.setImage(ci.getImage());
            orderItems.add(oi);
            total += ci.getSubtotal();
        }

        String paymentMode = req.paymentMode.toUpperCase();
        if (!List.of("CARD","UPI","COD").contains(paymentMode))
            throw new IllegalArgumentException("Unknown payment mode: " + req.paymentMode);

        OrderEntity entity = new OrderEntity();
        entity.setOrderId(generateOrderId());
        entity.setCustomerName(req.customerName);
        entity.setCustomerEmail(req.customerEmail);
        entity.setCustomerPhone(req.customerPhone);
        entity.setAddressLine1(req.addressLine1);
        entity.setAddressLine2(req.addressLine2);
        entity.setCity(req.city);
        entity.setState(req.state);
        entity.setPincode(req.pincode);
        entity.setCountry(req.country);
        entity.setPaymentMode(paymentMode);
        entity.setPaymentConfirmed(!paymentMode.equals("COD"));
        entity.setTotal(Math.round(total * 100.0) / 100.0);
        entity.setStatus("CONFIRMED");
        entity.setItemsJson(toJson(orderItems));

        orderRepository.save(entity);
        return toOrder(entity, orderItems);
    }

    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId).map(e -> toOrder(e, fromJson(e.getItemsJson())));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(e -> toOrder(e, fromJson(e.getItemsJson()))).toList();
    }

    private String generateOrderId() {
        return "SH-" + ThreadLocalRandom.current().nextInt(100_000, 999_999);
    }

    private Order toOrder(OrderEntity e, List<OrderItem> items) {
        Order o = new Order();
        o.setOrderId(e.getOrderId());
        o.setPlacedAt(e.getPlacedAt());
        o.setStatus(Order.Status.valueOf(e.getStatus()));
        o.setCustomerName(e.getCustomerName());
        o.setCustomerEmail(e.getCustomerEmail());
        o.setCustomerPhone(e.getCustomerPhone());
        o.setAddressLine1(e.getAddressLine1());
        o.setAddressLine2(e.getAddressLine2());
        o.setCity(e.getCity());
        o.setState(e.getState());
        o.setPincode(e.getPincode());
        o.setCountry(e.getCountry());
        o.setPaymentMode(Order.PaymentMode.valueOf(e.getPaymentMode()));
        o.setPaymentConfirmed(e.isPaymentConfirmed());
        o.setTotal(e.getTotal());
        o.setItems(items);
        return o;
    }

    private String toJson(Object obj) {
        try { return objectMapper.writeValueAsString(obj); }
        catch (Exception ex) { return "[]"; }
    }

    private List<OrderItem> fromJson(String json) {
        try {
            if (json == null || json.isBlank()) return new ArrayList<>();
            return objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, OrderItem.class));
        } catch (Exception ex) { return new ArrayList<>(); }
    }
}
