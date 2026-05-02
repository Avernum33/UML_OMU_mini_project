package omu.service;

import omu.domain.Order;

public interface OrderNotificationSystem {
    void notify(Order order);
}

