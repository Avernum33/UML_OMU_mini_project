package omu.domain;

import java.math.BigDecimal;
import java.util.List;

public final class Drink extends Product {
    public Drink(String productId, String name, BigDecimal basePrice, ProductStatus status,
            List<String> supportedOptions) {
        super(productId, name, ProductType.DRINK, basePrice, status, supportedOptions);
    }

    @Override
    protected Product copy(BigDecimal newBasePrice, ProductStatus newStatus) {
        return new Drink(getProductId(), getName(), newBasePrice, newStatus, getSupportedOptions());
    }
}

