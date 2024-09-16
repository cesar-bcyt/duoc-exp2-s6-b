package com.example.exp2s6b.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.exp2s6b.model.OrdenItem;

@Repository
public interface OrdenItemRepository extends JpaRepository<OrdenItem, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM orden_items WHERE orden_id = :orden_id", nativeQuery = true)
    void deleteOrdenItemByOrdenId(@Param("orden_id") Long orden_id);
}