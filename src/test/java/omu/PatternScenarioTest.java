package omu;

import java.math.BigDecimal;
import java.util.List;
import omu.domain.APCard;
import omu.domain.Customization;
import omu.domain.Order;
import omu.domain.OrderItem;
import omu.domain.OrderStatus;
import omu.domain.Product;
import omu.domain.ProductStatus;
import omu.domain.ProductType;
import omu.domain.User;
import omu.domain.UserRole;
import omu.patterns.factory.ProductFactoryProvider;
import omu.patterns.factory.ProductRequest;
import omu.patterns.observer.KioskMenuCache;
import omu.patterns.singleton.CentralCatalog;
import omu.service.AdminStaffSystem;
import omu.service.Kiosk;
import omu.service.OrderService;
import omu.service.PaymentService;
import omu.service.SnackBoothOrderDisplay;
import omu.service.TransactionLogger;

public final class PatternScenarioTest {
    private PatternScenarioTest() {
    }

    public static void main(String[] args) {
        CentralCatalog catalog = CentralCatalog.getInstance();
        catalog.clearForScenario();

        AdminStaffSystem admin = new AdminStaffSystem(catalog, new ProductFactoryProvider());
        KioskMenuCache cache = new KioskMenuCache("KIOSK-01");
        catalog.registerObserver(cache);

        Product drink = admin.addProduct(new ProductRequest(
                "DRK-TEST",
                "Kopi Ais",
                ProductType.DRINK,
                new BigDecimal("4.50"),
                ProductStatus.AVAILABLE,
                List.of("size", "sugar", "milk", "ice")));

        assertTrue(cache.findProduct("DRK-TEST").isPresent(), "Observer should update kiosk cache");

        User user = new User("TP123456", "Scenario Student", UserRole.STUDENT,
                new APCard("AP-TEST", new BigDecimal("10.00")));

        SnackBoothOrderDisplay display = new SnackBoothOrderDisplay();
        TransactionLogger logger = new TransactionLogger();
        OrderService orderService = new OrderService(new PaymentService(), logger, display);
        Kiosk kiosk = new Kiosk("KIOSK-01", cache, orderService);

        Order order = kiosk.submitOrder(user, List.of(new OrderItem(
                drink,
                1,
                List.of(new Customization("size", "large", new BigDecimal("1.00"))))));

        assertTrue(order.getStatus() == OrderStatus.SENT_TO_PREPARATION, "Order should be sent to preparation");
        assertTrue(user.getCard().getBalance().compareTo(new BigDecimal("4.50")) == 0,
                "Balance should be deducted by item total");
        assertTrue(display.pendingOrders().size() == 1, "Preparation area should receive order");
        assertTrue(logger.entries().size() == 1, "Transaction should be logged");

        admin.updateAvailability("DRK-TEST", ProductStatus.UNAVAILABLE);
        assertTrue(kiosk.browseMenu().isEmpty(), "Unavailable product should disappear from kiosk menu");

        catalog.unregisterObserver(cache);
        System.out.println("PatternScenarioTest passed");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
