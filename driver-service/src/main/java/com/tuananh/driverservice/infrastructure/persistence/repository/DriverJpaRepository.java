package com.tuananh.driverservice.infrastructure.persistence.repository;

import com.tuananh.driverservice.domain.model.DriverStatus;
import com.tuananh.driverservice.infrastructure.persistence.entity.DriverEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DriverJpaRepository extends JpaRepository<DriverEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select d from DriverEntity d where d.status = :status order by d.id asc")
    List<DriverEntity> findAvailableForAssignment(@Param("status") DriverStatus status, Pageable pageable);
}
