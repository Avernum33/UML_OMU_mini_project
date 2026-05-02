package omu.service;

import java.util.List;
import java.util.Optional;
import omu.domain.Order;
import omu.domain.OrderItem;
import omu.domain.Product;
import omu.domain.User;
import omu.patterns.observer.KioskMenuCache;

public final class Kiosk {
    private final String kioskId;
    private final KioskMenuCache menuCache;
    private final OrderService orderService;

    public Kiosk(String kioskId, KioskMenuCache menuCache, OrderService orderService) {
        if (kioskId == null || kioskId.isBlank()) {
            throw new IllegalArgumentException("kioskId must not be blank");
        }
        this.kioskId = kioskId;
        this.menuCache = java.util.Objects.requireNonNull(menuCache, "menuCache");
        this.orderService = java.util.Objects.requireNonNull(orderService, "orderService");
    }

    public String getKioskId() {
        return kioskId;
    }

    public List<Product> browseMenu() {
        return menuCache.listAvailable();
    }

    public Optional<Product> findProduct(String productId) {
        return menuCache.findProduct(productId).filter(Product::isAvailable);
    }

    public Order submitOrder(User user, List<OrderItem> items) {
        return orderService.placeOrder(user, items);
    }
}

