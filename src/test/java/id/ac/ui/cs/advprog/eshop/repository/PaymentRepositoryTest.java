package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    List<Payment> payments;
    private Map<String, String> paymentData;


    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        this.paymentData = new HashMap<>();
        this.paymentData.put("voucherCode", "ESHOP1234ABC5678");


        payments = new ArrayList<>();
        Payment payment1 = new Payment("a0f9de46-90b1-437d-b0f0-dded07e0f912",
                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        payments.add(payment1);
        Payment payment2 = new Payment("a0f9de46-88abs-437d-b0f0-dded07e0f912",
                PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        payments.add(payment2);
    }

    @Test
    void testSaveCreate() {
        Payment payment = payments.get(1);
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getPaymentData(), findResult.getPaymentData());
        assertEquals(payment.getStatus(), findResult.getStatus());
    }

    @Test
    void testSaveUpdate() {
        Payment payment = payments.get(1);
        paymentRepository.save(payment);
        Payment newPayment = new Payment(payment.getId(), payment.getMethod(), payment.getPaymentData(), PaymentStatus.SUCCESS.getValue());
        Payment result = paymentRepository.save(newPayment);

        Payment findResult = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getPaymentData(), findResult.getPaymentData());
        assertEquals(PaymentStatus.SUCCESS.getValue(), findResult.getStatus());
    }

    @Test
    void testFindByIdIfIdFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payments.get(1).getId(), findResult.getId());
        assertEquals(payments.get(1).getMethod(), findResult.getMethod());
        assertEquals(payments.get(1).getPaymentData(), findResult.getPaymentData());
        assertEquals(payments.get(1).getStatus(), findResult.getStatus());
    }

    @Test
    void testFindByIdIfIdNotFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById("zczc");
        assertNull(findResult);
    }

    @Test
    void testFindAllIfMoreThanOnePayment() {
        Payment payment1 = new Payment("a0f9de46-90b1-437d-b0f0-dded07e0f912",
                PaymentMethod.VOUCHER_CODE.getValue(), paymentData);
        paymentRepository.save(payment1);
        Payment payment2 = new Payment("a0f9de46-ab44-437d-b0f0-dded07e0f912",
                PaymentMethod.BANK_TRANSFER.getValue(), paymentData);
        paymentRepository.save(payment2);

        Iterator<Payment> paymentIterator = paymentRepository.findAll();
        assertTrue(paymentIterator.hasNext());
        Payment savedPayment = paymentIterator.next();
        assertEquals(payment1.getId(), savedPayment.getId());
        savedPayment = paymentIterator.next();
        assertEquals(payment2.getId(), savedPayment.getId());
        assertFalse(paymentIterator.hasNext());
    }

}