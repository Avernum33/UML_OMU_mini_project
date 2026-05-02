package omu;

import java.math.BigDecimal;
import java.util.List;
import omu.domain.APCard;
import omu.domain.Customization;
import omu.domain.Order;
import omu.domain.OrderItem;
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

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        CentralCatalog catalog = CentralCatalog.getInstance();
        AdminStaffSystem adminSystem = new AdminStaffSystem(catalog, new ProductFactoryProvider());

        KioskMenuCache kioskCache = new KioskMenuCache("KIOSK-01");
        catalog.registerObserver(kioskCache);

        Product kopiAis = adminSystem.addProduct(new ProductRequest(
                "DRK-001",
                "Kopi Ais",
                ProductType.DRINK,
                new BigDecimal("4.50"),
                ProductStatus.AVAILABLE,
                List.of("size", "sugar", "milk", "ice")));

        adminSystem.addProduct(new ProductRequest(
                "SNK-001",
                "Curry Puff",
                ProductType.SNACK,
                new BigDecimal("2.50"),
                ProductStatus.AVAILABLE,
                List.of("spice-level")));

        SnackBoothOrderDisplay display = new SnackBoothOrderDisplay();
        OrderService orderService = new OrderService(new PaymentService(), new TransactionLogger(), display);
        Kiosk kiosk = new Kiosk("KIOSK-01", kioskCache, orderService);

        User user = new User("TP000001", "Demo Student", UserRole.STUDENT,
                new APCard("AP-1001", new BigDecimal("20.00")));

        OrderItem item = new OrderItem(kopiAis, 1, List.of(
                new Customization("size", "large", new BigDecimal("1.00")),
                new Customization("sugar", "less", BigDecimal.ZERO)));

        Order order = kiosk.submitOrder(user, List.of(item));

        System.out.println("Order " + order.getOrderId() + " status: " + order.getStatus());
        System.out.println("Remaining balance: " + user.getCard().getBalance());
        System.out.println("Orders waiting at booth: " + display.pendingOrders().size());
    }
}
