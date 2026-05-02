package omu.patterns.factory;

import omu.domain.Product;

public interface ProductFactory {
    Product create(ProductRequest request);
}
