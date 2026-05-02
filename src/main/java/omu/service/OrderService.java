package omu.service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import omu.domain.Order;
import omu.domain.OrderItem;
import omu.domain.Transaction;
import omu.domain.User;

public final class OrderService {
    private final PaymentService paymentService;
    private final TransactionLogger transactionLogger;
    private final OrderNotificationSystem notificationSystem;
    private final AtomicInteger orderSequence = new AtomicInteger(1000);

    public OrderService(PaymentService paymentService, TransactionLogger transactionLogger,
            OrderNotificationSystem notificationSystem) {
        this.paymentService = Objects.requireNonNull(paymentService, "paymentService");
        this.transactionLogger = Objects.requireNonNull(transactionLogger, "transactionLogger");
        this.notificationSystem = Objects.requireNonNull(notificationSystem, "notificationSystem");
    }

    public Order placeOrder(User user, List<OrderItem> items) {
        List<OrderItem> checkedItems = Objects.requireNonNull(items, "items");
        boolean hasUnavailableItem = checkedItems.stream()
                .anyMatch(item -> !item.getProduct().isAvailable());
        if (hasUnavailableItem) {
            throw new IllegalStateException("Order contains an unavailable product");
        }
        Order order = new Order("ORD-" + orderSequence.incrementAndGet(), user, checkedItems);
        Transaction transaction = paymentService.charge(order.getOrderId(), user, order.total());
        order.markPaid();
        transactionLogger.log(transaction);
        notificationSystem.notify(order);
        order.markSentToPreparation();
        return order;
    }
}
