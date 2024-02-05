package com.store.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.security.entities.KeyEntity;
import com.store.security.entities.User;

@Repository
public interface KeyRepository extends JpaRepository<KeyEntity, Integer> {
   
    Optional<KeyEntity> findByStatus(String estatus);
}
