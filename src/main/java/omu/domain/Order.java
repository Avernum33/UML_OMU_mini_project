package omu.domain;

import java.math.BigDecimal;
import java.util.List;

public final class Order {
    private final String orderId;
    private final User user;
    private final List<OrderItem> items;
    private OrderStatus status;

    public Order(String orderId, User user, List<OrderItem> items) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId must not be blank");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        this.orderId = orderId;
        this.user = java.util.Objects.requireNonNull(user, "user");
        this.items = List.copyOf(items);
        this.status = OrderStatus.DRAFT;
    }

    public String getOrderId() {
        return orderId;
    }

    public User getUser() {
        return user;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal total() {
        return items.stream()
                .map(OrderItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void markPaid() {
        status = OrderStatus.PAID;
    }

    public void markSentToPreparation() {
        status = OrderStatus.SENT_TO_PREPARATION;
    }

    public void cancel() {
        status = OrderStatus.CANCELLED;
    }
}

