package com.FoodServe.Dilevery.Userrepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.FoodServe.Dilevery.entity.OrdersEntity;

public interface OrderRepository
        extends JpaRepository<OrdersEntity, Long> {

    // Latest order by user email
    Optional<OrdersEntity>
    findTopByUserEmailOrderByOrderDateDesc(String email);

    // Receipt query with items + product
    @Query("""
        SELECT o
        FROM OrdersEntity o
        JOIN FETCH o.items i
        JOIN FETCH i.product
        WHERE o.orderId = :id
    """)
    Optional<OrdersEntity> findByIdWithItems(
            @Param("id") Long id);
}