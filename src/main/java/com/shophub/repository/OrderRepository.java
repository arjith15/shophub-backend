package com.shophub.repository;

import com.shophub.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for orders.
 * All CRUD operations are auto-generated — no SQL needed.
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    // Find all orders by customer email
    List<OrderEntity> findByCustomerEmail(String email);

    // Find all orders by status
    List<OrderEntity> findByStatus(String status);
}
