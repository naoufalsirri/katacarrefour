package com.carrefour.kata.mapper;

import com.carrefour.kata.dto.OrderRequestDto;
import com.carrefour.kata.dto.PaymentOptionDto;
import com.carrefour.kata.model.PaymentEntity;
import com.carrefour.kata.model.PaymentOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "paymentOption", target = "paymentOption", qualifiedByName = "toEntityEnum")
    PaymentEntity toEntity(OrderRequestDto dto);

    @Mapping(source = "paymentOption", target = "paymentOption", qualifiedByName = "toDtoEnum")
    OrderRequestDto toDto(PaymentEntity entity);

    @Named("toEntityEnum")
    default PaymentOption mapPaymentOptionDtoToEntity(PaymentOptionDto dto) {
        return dto == null ? null : PaymentOption.valueOf(dto.name());
    }

    @Named("toDtoEnum")
    default PaymentOptionDto mapPaymentOptionToDto(PaymentOption entity) {
        return entity == null ? null : PaymentOptionDto.valueOf(entity.name());
    }
}