package omu.patterns.observer;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import omu.domain.Product;

public final class KioskMenuCache implements CatalogObserver {
    private final String kioskId;
    private final ConcurrentMap<String, Product> cachedProducts = new ConcurrentHashMap<>();

    public KioskMenuCache(String kioskId) {
        if (kioskId == null || kioskId.isBlank()) {
            throw new IllegalArgumentException("kioskId must not be blank");
        }
        this.kioskId = kioskId;
    }

    public String getKioskId() {
        return kioskId;
    }

    public void loadInitial(List<Product> products) {
        cachedProducts.clear();
        for (Product product : products) {
            cachedProducts.put(product.getProductId(), product);
        }
    }

    public Optional<Product> findProduct(String productId) {
        return Optional.ofNullable(cachedProducts.get(productId));
    }

    public List<Product> listAvailable() {
        return cachedProducts.values().stream()
                .filter(Product::isAvailable)
                .sorted(Comparator.comparing(Product::getName))
                .toList();
    }

    @Override
    public void onCatalogChanged(CatalogEvent event) {
        cachedProducts.put(event.getProductId(), event.getProduct());
    }
}

