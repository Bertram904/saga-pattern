package com.tuananh.paymentservice.infrastructure.persistence.mapper;

import com.tuananh.paymentservice.application.port.in.result.WalletResult;
import com.tuananh.paymentservice.infrastructure.persistence.entity.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletPersistenceMapper {

    WalletResult toResult(WalletEntity entity);

    void copyToEntity(WalletResult wallet, @MappingTarget WalletEntity entity);
}
