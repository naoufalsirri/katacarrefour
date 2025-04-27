package com.carrefour.kata.repository;

import com.carrefour.kata.model.PaymentEntity;
import com.carrefour.kata.model.PaymentOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void testSavePayment() {
        PaymentEntity payment = new PaymentEntity();
        payment.setAmount(new BigDecimal("100.00"));
        payment.setPaymentOption(PaymentOption.THREE_INSTALLMENTS);

        PaymentEntity savedPayment = paymentRepository.save(payment);

        assertNotNull(savedPayment.getId());
        assertEquals(payment.getAmount(), savedPayment.getAmount());
        assertEquals(payment.getPaymentOption(), savedPayment.getPaymentOption());
    }

    @Test
    public void testFindPaymentById() {
        PaymentEntity payment = new PaymentEntity();
        payment.setAmount(new BigDecimal("200.00"));
        payment.setPaymentOption(PaymentOption.FOUR_INSTALLMENTS);
        PaymentEntity savedPayment = paymentRepository.save(payment);

        PaymentEntity foundPayment = paymentRepository.findById(savedPayment.getId()).orElse(null);

        assertNotNull(foundPayment);
        assertEquals(savedPayment.getId(), foundPayment.getId());
        assertEquals(savedPayment.getAmount(), foundPayment.getAmount());
    }

    @Test
    public void testDeletePayment() {
        PaymentEntity payment = new PaymentEntity();
        payment.setAmount(new BigDecimal("300.00"));
        payment.setPaymentOption(PaymentOption.BANK_TRANSFER);
        PaymentEntity savedPayment = paymentRepository.save(payment);

        paymentRepository.delete(savedPayment);
        PaymentEntity deletedPayment = paymentRepository.findById(savedPayment.getId()).orElse(null);

        assertNull(deletedPayment);
    }
}