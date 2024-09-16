package com.example.exp2s6b.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.exp2s6b.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}