package omu.patterns.observer;

import java.util.Objects;
import omu.domain.Product;

public final class CatalogEvent {
    private final CatalogEventType eventType;
    private final String productId;
    private final Product product;
    private final String message;

    public CatalogEvent(CatalogEventType eventType, String productId, Product product, String message) {
        this.eventType = Objects.requireNonNull(eventType, "eventType");
        this.productId = requireText(productId, "productId");
        this.product = Objects.requireNonNull(product, "product");
        this.message = message == null ? "" : message;
    }

    public CatalogEventType getEventType() {
        return eventType;
    }

    public String getProductId() {
        return productId;
    }

    public Product getProduct() {
        return product;
    }

    public String getMessage() {
        return message;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}

