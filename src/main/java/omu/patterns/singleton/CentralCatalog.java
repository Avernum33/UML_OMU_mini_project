package omu.patterns.singleton;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import omu.domain.Product;
import omu.domain.ProductStatus;
import omu.patterns.observer.CatalogEvent;
import omu.patterns.observer.CatalogEventType;
import omu.patterns.observer.CatalogObserver;

public final class CentralCatalog {
    private static volatile CentralCatalog instance;

    private final ConcurrentMap<String, Product> products = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<CatalogObserver> observers = new CopyOnWriteArrayList<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private CentralCatalog() {
    }

    public static CentralCatalog getInstance() {
        CentralCatalog local = instance;
        if (local == null) {
            synchronized (CentralCatalog.class) {
                local = instance;
                if (local == null) {
                    local = new CentralCatalog();
                    instance = local;
                }
            }
        }
        return local;
    }

    public void registerObserver(CatalogObserver observer) {
        observers.addIfAbsent(Objects.requireNonNull(observer, "observer"));
    }

    public void unregisterObserver(CatalogObserver observer) {
        observers.remove(observer);
    }

    public void saveProduct(Product product) {
        CatalogEvent event;
        lock.writeLock().lock();
        try {
            boolean existed = products.containsKey(product.getProductId());
            products.put(product.getProductId(), product);
            CatalogEventType eventType = existed ? CatalogEventType.PRODUCT_UPDATED : CatalogEventType.PRODUCT_ADDED;
            event = new CatalogEvent(eventType, product.getProductId(), product, "Product saved");
        } finally {
            lock.writeLock().unlock();
        }
        publish(event);
    }

    public void updateAvailability(String productId, ProductStatus status) {
        Product updated;
        lock.writeLock().lock();
        try {
            Product current = productOrFail(productId);
            updated = current.withStatus(status);
            products.put(productId, updated);
        } finally {
            lock.writeLock().unlock();
        }
        CatalogEventType eventType = status == ProductStatus.UNAVAILABLE
                ? CatalogEventType.PRODUCT_UNAVAILABLE
                : CatalogEventType.PRODUCT_UPDATED;
        publish(new CatalogEvent(eventType, productId, updated, "Availability updated"));
    }

    public void updatePrice(String productId, BigDecimal newPrice) {
        Product updated;
        lock.writeLock().lock();
        try {
            Product current = productOrFail(productId);
            updated = current.withBasePrice(newPrice);
            products.put(productId, updated);
        } finally {
            lock.writeLock().unlock();
        }
        publish(new CatalogEvent(CatalogEventType.PRODUCT_UPDATED, productId, updated, "Price updated"));
    }

    public Optional<Product> findProduct(String productId) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(products.get(productId));
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Product> listAvailable() {
        lock.readLock().lock();
        try {
            return products.values().stream()
                    .filter(Product::isAvailable)
                    .sorted(Comparator.comparing(Product::getName))
                    .toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Product> snapshot() {
        lock.readLock().lock();
        try {
            return products.values().stream()
                    .sorted(Comparator.comparing(Product::getName))
                    .toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clearForScenario() {
        lock.writeLock().lock();
        try {
            products.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Product productOrFail(String productId) {
        Product product = products.get(productId);
        if (product == null) {
            throw new IllegalArgumentException("Unknown product: " + productId);
        }
        return product;
    }

    private void publish(CatalogEvent event) {
        for (CatalogObserver observer : observers) {
            observer.onCatalogChanged(event);
        }
    }
}
