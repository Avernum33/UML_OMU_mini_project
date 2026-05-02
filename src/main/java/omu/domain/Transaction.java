package omu.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public final class Transaction {
    private final String transactionId;
    private final String orderId;
    private final String cardNumber;
    private final BigDecimal amount;
    private final Instant createdAt;

    public Transaction(String transactionId, String orderId, String cardNumber, BigDecimal amount, Instant createdAt) {
        this.transactionId = requireText(transactionId, "transactionId");
        this.orderId = requireText(orderId, "orderId");
        this.cardNumber = requireText(cardNumber, "cardNumber");
        this.amount = Objects.requireNonNull(amount, "amount");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}

