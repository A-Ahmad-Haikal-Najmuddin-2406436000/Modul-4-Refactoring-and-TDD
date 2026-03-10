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

    }

    @Override
    public Payment setStatus(Payment payment, String status) {

    }

    @Override
    public Payment getPayment(String paymentId) {
    }

    @Override
    public List<Payment> getAllPayments() {

    }

    private String determineStatus(String method, Map<String, String> paymentData) {

    }

    private String validateVoucherCode(Map<String, String> paymentData) {

    }

    private String validateBankTransfer(Map<String, String> paymentData) {

    }
}