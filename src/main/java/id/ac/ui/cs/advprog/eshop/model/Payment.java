package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;
import java.util.Set;

@Getter @Setter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty string");
        }
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Method cannot be null or empty string");
        }
        if (paymentData == null) {
            throw new IllegalArgumentException("Payment Data cannot be null");
        }
        this.id  = id;
        this.method = method;
        this.paymentData = paymentData;
    }

    public Payment(String id, String method, Map<String, String> paymentData, String status) {
        this(id, method, paymentData);
        this.status = status;
    }

    public void setStatus(String status) {
        if (!Set.of("SUCCESS", "REJECTED", "PENDING").contains(status)) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.status = status;
    }
}