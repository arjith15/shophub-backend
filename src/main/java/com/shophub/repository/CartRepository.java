package com.shophub.repository;

import com.shophub.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for cart items.
 */
@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    // Get all items for a session
    List<CartEntity> findBySessionId(String sessionId);

    // Find a specific item by session + itemKey
    Optional<CartEntity> findBySessionIdAndItemKey(String sessionId, String itemKey);

    // Delete all items for a session (used after checkout)
    void deleteBySessionId(String sessionId);

    // Delete a specific item
    void deleteBySessionIdAndItemKey(String sessionId, String itemKey);
}
