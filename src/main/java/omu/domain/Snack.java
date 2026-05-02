package omu.domain;

import java.math.BigDecimal;
import java.util.List;

public final class Snack extends Product {
    public Snack(String productId, String name, BigDecimal basePrice, ProductStatus status,
            List<String> supportedOptions) {
        super(productId, name, ProductType.SNACK, basePrice, status, supportedOptions);
    }

    @Override
    protected Product copy(BigDecimal newBasePrice, ProductStatus newStatus) {
        return new Snack(getProductId(), getName(), newBasePrice, newStatus, getSupportedOptions());
    }
}

