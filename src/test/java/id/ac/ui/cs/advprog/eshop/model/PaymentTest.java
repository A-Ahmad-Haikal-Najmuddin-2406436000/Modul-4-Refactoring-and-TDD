package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Map<String, String> paymentData;
    private Order order;

    @BeforeEach
    void setUp() {
        this.paymentData = new HashMap<>();
        this.paymentData.put("bankName", "BCA");
        this.paymentData.put("referenceCode", "69420");

        this.order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                null, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testCreatePaymentValid() {
        Payment payment = new Payment("a0f9de46-90b1-437d-b0f0-dded07e0f912",
                "BANK_TRANSFER", "SUCCESS", paymentData, order);

        assertEquals("a0f9de46-90b1-437d-b0f0-dded07e0f912", payment.getId());
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
        assertEquals(order, payment.getOrder());
    }

    @Test
    void testSetStatusValid() {
        Payment payment = new Payment("a0f9de46-90b1-437d-b0f0-dded07e0f912",
                "VOUCHER_CODE", "PENDING", paymentData, order);

        payment.setStatus("SUCCESS");
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testSetStatusInvalid() {
        Payment payment = new Payment("a0f9de46-90b1-437d-b0f0-dded07e0f912",
                "VOUCHER_CODE", "PENDING", paymentData, order);

        // Testing that it throws an exception if status is not one of the allowed values
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("MEOW");
        });
    }

    @Test
    void testCreatePaymentEmptyData() {
        Map<String, String> emptyData = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("a0f9de46", "BANK_TRANSFER", "SUCCESS", emptyData, order);
        });
    }
}