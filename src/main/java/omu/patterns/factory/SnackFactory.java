package omu.patterns.factory;

import omu.domain.Product;
import omu.domain.ProductType;
import omu.domain.Snack;

public final class SnackFactory implements ProductFactory {
    @Override
    public Product create(ProductRequest request) {
        if (request.getType() != ProductType.SNACK) {
            throw new IllegalArgumentException("SnackFactory can only create snacks");
        }
        return new Snack(request.getProductId(), request.getName(), request.getBasePrice(),
                request.getStatus(), request.getSupportedOptions());
    }
}
