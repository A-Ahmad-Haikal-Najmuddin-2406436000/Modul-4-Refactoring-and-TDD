package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        this.paymentData = new HashMap<>();
        this.paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment("a0f9de46-90b1-437d-b0f0-dded07e0f912",
                "VOUCHER_CODE", paymentData);

        assertEquals("a0f9de46-90b1-437d-b0f0-dded07e0f912", payment.getId());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentWithStatus() {
        Payment payment = new Payment("a0f9de46-90b1-437d-b0f0-dded07e0f912",
                "BANK_TRANSFER", paymentData, "SUCCESS");

        assertEquals("a0f9de46-90b1-437d-b0f0-dded07e0f912", payment.getId());
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentEmptyId() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(
                    null,
                    "BANK_TRANSFER", paymentData, "SUCCESS"
            );
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(
                    "      ",
                    "BANK_TRANSFER", paymentData, "SUCCESS"
            );
        });
    }

    @Test
    void testCreatePaymentEmptyMethod() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(
                    "a0f9de46-90b1-437d-b0f0-dded07e0f912",
                    null, paymentData, "SUCCESS"
            );
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(
                    "a0f9de46-90b1-437d-b0f0-dded07e0f912",
                    "    ", paymentData, "SUCCESS"
            );
        });
    }

    @Test
    void testCreatePaymentEmptyPaymentData() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(
                    "a0f9de46-90b1-437d-b0f0-dded07e0f912",
                    "BANK_TRANSFER", null, "SUCCESS"
            );
        });
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment("a0f9de46-90b1-437d-b0f0-dded07e0f912",
                "VOUCHER_CODE", paymentData);

        payment.setStatus(PaymentStatus.REJECTED.getValue());

        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        Payment payment = new Payment("a0f9de46-90b1-437d-b0f0-dded07e0f912",
                "VOUCHER_CODE", paymentData);

        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("MEOW"));
    }
}