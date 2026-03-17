package com.tuananh.paymentservice.infrastructure.persistence.repository;

import com.tuananh.paymentservice.infrastructure.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletJpaRepository extends JpaRepository<WalletEntity, String> {}
