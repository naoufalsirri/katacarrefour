package com.carrefour.kata.mapper;


import com.carrefour.kata.dto.OrderRequestDto;
import com.carrefour.kata.dto.PaymentOptionDto;
import com.carrefour.kata.model.PaymentEntity;
import com.carrefour.kata.model.PaymentOption;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PaymentMapperTest {

    private final PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);

    @Test
    public void testToEntity() {
        OrderRequestDto dto = new OrderRequestDto();
        dto.setAmount(new BigDecimal("100.00"));
        dto.setPaymentOption(PaymentOptionDto.THREE_INSTALLMENTS);

        PaymentEntity entity = paymentMapper.toEntity(dto);

        assertEquals(dto.getAmount(), entity.getAmount());
        assertEquals(PaymentOption.THREE_INSTALLMENTS, entity.getPaymentOption());
    }

    @Test
    public void testToDto() {
        PaymentEntity entity = new PaymentEntity();
        entity.setAmount(new BigDecimal("200.00"));
        entity.setPaymentOption(PaymentOption.FOUR_INSTALLMENTS);

        OrderRequestDto dto = paymentMapper.toDto(entity);

        assertEquals(entity.getAmount(), dto.getAmount());
        assertEquals(PaymentOptionDto.FOUR_INSTALLMENTS, dto.getPaymentOption());
    }

    @Test
    public void testMapPaymentOptionDtoToEntity() {
        PaymentOptionDto dto = PaymentOptionDto.BANK_TRANSFER;

        PaymentOption entity = paymentMapper.mapPaymentOptionDtoToEntity(dto);

        assertEquals(PaymentOption.BANK_TRANSFER, entity);
    }

    @Test
    public void testMapPaymentOptionToDto() {
        PaymentOption entity = PaymentOption.THREE_INSTALLMENTS;

        PaymentOptionDto dto = paymentMapper.mapPaymentOptionToDto(entity);

        assertEquals(PaymentOptionDto.THREE_INSTALLMENTS, dto);
    }

    @Test
    public void testNullMapping() {
        PaymentOption entity = paymentMapper.mapPaymentOptionDtoToEntity(null);
        PaymentOptionDto dto = paymentMapper.mapPaymentOptionToDto(null);

        assertNull(entity);
        assertNull(dto);
    }
}