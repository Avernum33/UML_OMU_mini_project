package omu.domain;

import java.math.BigDecimal;
import java.util.Objects;

public final class APCard {
    private final String cardNumber;
    private BigDecimal balance;

    public APCard(String cardNumber, BigDecimal openingBalance) {
        this.cardNumber = requireText(cardNumber, "cardNumber");
        this.balance = requireNonNegativeMoney(openingBalance, "openingBalance");
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    public synchronized boolean canPay(BigDecimal amount) {
        return balance.compareTo(requireNonNegativeMoney(amount, "amount")) >= 0;
    }

    public synchronized void deduct(BigDecimal amount) {
        BigDecimal checkedAmount = requireNonNegativeMoney(amount, "amount");
        if (balance.compareTo(checkedAmount) < 0) {
            throw new IllegalStateException("Insufficient AP card balance");
        }
        balance = balance.subtract(checkedAmount);
    }

    private static BigDecimal requireNonNegativeMoney(BigDecimal amount, String fieldName) {
        Objects.requireNonNull(amount, fieldName);
        if (amount.signum() < 0) {
            throw new IllegalArgumentException(fieldName + " must not be negative");
        }
        return amount;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}

