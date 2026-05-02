package omu.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import omu.domain.Order;

public final class SnackBoothOrderDisplay implements OrderNotificationSystem {
    private final CopyOnWriteArrayList<Order> pendingOrders = new CopyOnWriteArrayList<>();

    @Override
    public void notify(Order order) {
        pendingOrders.add(order);
    }

    public List<Order> pendingOrders() {
        return List.copyOf(pendingOrders);
    }
}

