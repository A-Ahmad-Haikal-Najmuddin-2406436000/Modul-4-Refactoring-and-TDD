package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String paymentId = UUID.randomUUID().toString();
        String status = determineStatus(method, paymentData);

        Payment payment = new Payment(paymentId, method, paymentData, status);
        paymentRepository.save(payment);

        if (status.equals(PaymentStatus.SUCCESS.getValue())) {
            order.setStatus(OrderStatus.SUCCESS.getValue());
        } else if (status.equals(PaymentStatus.REJECTED.getValue())) {
            order.setStatus(OrderStatus.FAILED.getValue());
        }
        orderRepository.save(order);

        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        Iterator<Payment> paymentIterator = paymentRepository.findAll();
        List<Payment> allPayments = new ArrayList<>();
        paymentIterator.forEachRemaining(allPayments::add);
        return allPayments;
    }

    private String determineStatus(String method, Map<String, String> paymentData) {
        if ("VOUCHER_CODE".equals(method)) {
            return validateVoucherCode(paymentData);
        } else if ("BANK_TRANSFER".equals(method)) {
            return validateBankTransfer(paymentData);
        }
        return PaymentStatus.REJECTED.getValue();
    }

    private String validateVoucherCode(Map<String, String> paymentData) {
        String voucherCode = paymentData.get("voucherCode");
        if (voucherCode == null) {
            return PaymentStatus.REJECTED.getValue();
        }
        if (voucherCode.length() != 16) {
            return PaymentStatus.REJECTED.getValue();
        }
        if (!voucherCode.startsWith("ESHOP")) {
            return PaymentStatus.REJECTED.getValue();
        }
        long digitCount = voucherCode.chars().filter(Character::isDigit).count();
        if (digitCount != 8) {
            return PaymentStatus.REJECTED.getValue();
        }
        return PaymentStatus.SUCCESS.getValue();
    }

    private String validateBankTransfer(Map<String, String> paymentData) {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");
        if (bankName == null || bankName.isEmpty()) {
            return PaymentStatus.REJECTED.getValue();
        }
        if (referenceCode == null || referenceCode.isEmpty()) {
            return PaymentStatus.REJECTED.getValue();
        }
        return PaymentStatus.SUCCESS.getValue();
    }
}