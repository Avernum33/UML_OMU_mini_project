package omu.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import omu.domain.Transaction;
import omu.domain.User;

public final class PaymentService {
    public Transaction charge(String orderId, User user, BigDecimal amount) {
        Objects.requireNonNull(user, "user");
        Objects.requireNonNull(amount, "amount");
        user.getCard().deduct(amount);
        return new Transaction(
                "TX-" + UUID.randomUUID(),
                orderId,
                user.getCard().getCardNumber(),
                amount,
                Instant.now());
    }
}

