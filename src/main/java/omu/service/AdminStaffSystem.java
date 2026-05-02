package omu.service;

import java.math.BigDecimal;
import omu.domain.Product;
import omu.domain.ProductStatus;
import omu.patterns.factory.ProductFactory;
import omu.patterns.factory.ProductFactoryProvider;
import omu.patterns.factory.ProductRequest;
import omu.patterns.singleton.CentralCatalog;

public final class AdminStaffSystem {
    private final CentralCatalog catalog;
    private final ProductFactoryProvider factoryProvider;

    public AdminStaffSystem(CentralCatalog catalog, ProductFactoryProvider factoryProvider) {
        this.catalog = java.util.Objects.requireNonNull(catalog, "catalog");
        this.factoryProvider = java.util.Objects.requireNonNull(factoryProvider, "factoryProvider");
    }

    public Product addProduct(ProductRequest request) {
        ProductFactory factory = factoryProvider.factoryFor(request.getType());
        Product product = factory.create(request);
        catalog.saveProduct(product);
        return product;
    }

    public void updateAvailability(String productId, ProductStatus status) {
        catalog.updateAvailability(productId, status);
    }

    public void updatePrice(String productId, BigDecimal newPrice) {
        catalog.updatePrice(productId, newPrice);
    }
}
