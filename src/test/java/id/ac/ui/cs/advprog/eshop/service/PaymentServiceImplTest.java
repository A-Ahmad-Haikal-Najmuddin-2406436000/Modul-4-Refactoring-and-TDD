package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId(UUID.fromString("eb558e9f-1c39-460e-8860-71af6af63bd6"));
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testAddPaymentVoucherCodeValid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals("VOUCHER_CODE", result.getMethod());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testAddPaymentVoucherCodeInvalidNotSixteenChars() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC");

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
    }

    @Test
    void testAddPaymentVoucherCodeInvalidNotStartWithEshop() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "XSHOP1234ABC5678");

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentVoucherCodeInvalidNotEightDigits() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABCDEFG");

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentVoucherCodeNull() {
        Map<String, String> paymentData = new HashMap<>();

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferValid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF123456");

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals("BANK_TRANSFER", result.getMethod());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
    }

    @Test
    void testAddPaymentBankTransferEmptyBankName() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", "REF123456");

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
    }

    @Test
    void testAddPaymentBankTransferNullBankName() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("referenceCode", "REF123456");

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferEmptyReferenceCode() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "");

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferNullReferenceCode() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testSetStatusSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-id-1", "VOUCHER_CODE", paymentData, PaymentStatus.REJECTED.getValue());

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusInvalid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-id-1", "VOUCHER_CODE", paymentData, PaymentStatus.REJECTED.getValue());

        assertThrows(IllegalArgumentException.class, () ->
                paymentService.setStatus(payment, "MEOW"));
    }

    @Test
    void testGetPaymentFound() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-id-1", "VOUCHER_CODE", paymentData);

        doReturn(payment).when(paymentRepository).findById("pay-id-1");

        Payment result = paymentService.getPayment("pay-id-1");
        assertEquals("pay-id-1", result.getId());
    }

    @Test
    void testGetPaymentNotFound() {
        doReturn(null).when(paymentRepository).findById("nonexistent");

        Payment result = paymentService.getPayment("nonexistent");
        assertNull(result);
    }


    @Test
    void testGetAllPayments() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = new Payment("pay-1", "VOUCHER_CODE", paymentData);
        Payment payment2 = new Payment("pay-2", "BANK_TRANSFER", paymentData);

        List<Payment> paymentList = new ArrayList<>(List.of(payment1, payment2));
        doReturn(paymentList.iterator()).when(paymentRepository).findAll();

        List<Payment> results = paymentService.getAllPayments();
        assertEquals(2, results.size());
    }

    @Test
    void testGetAllPaymentsEmpty() {
        List<Payment> emptyList = new ArrayList<>();
        doReturn(emptyList.iterator()).when(paymentRepository).findAll();

        List<Payment> results = paymentService.getAllPayments();
        assertTrue(results.isEmpty());
    }
}