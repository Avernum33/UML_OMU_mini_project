package omu.patterns.factory;

import omu.domain.Drink;
import omu.domain.Product;
import omu.domain.ProductType;

public final class DrinkFactory implements ProductFactory {
    @Override
    public Product create(ProductRequest request) {
        if (request.getType() != ProductType.DRINK) {
            throw new IllegalArgumentException("DrinkFactory can only create drinks");
        }
        return new Drink(request.getProductId(), request.getName(), request.getBasePrice(),
                request.getStatus(), request.getSupportedOptions());
    }
}
