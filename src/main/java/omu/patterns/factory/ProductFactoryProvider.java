package omu.patterns.factory;

import java.util.EnumMap;
import java.util.Map;
import omu.domain.ProductType;

public final class ProductFactoryProvider {
    private final Map<ProductType, ProductFactory> factories = new EnumMap<>(ProductType.class);

    public ProductFactoryProvider() {
        factories.put(ProductType.DRINK, new DrinkFactory());
        factories.put(ProductType.SNACK, new SnackFactory());
    }

    public ProductFactory factoryFor(ProductType type) {
        ProductFactory factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("No product factory registered for " + type);
        }
        return factory;
    }
}

