package omu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public abstract class Product {
    private final String productId;
    private final String name;
    private final ProductType type;
    private final BigDecimal basePrice;
    private final ProductStatus status;
    private final List<String> supportedOptions;

    protected Product(String productId, String name, ProductType type, BigDecimal basePrice,
            ProductStatus status, List<String> supportedOptions) {
        this.productId = requireText(productId, "productId");
        this.name = requireText(name, "name");
        this.type = Objects.requireNonNull(type, "type");
        this.basePrice = requireNonNegativeMoney(basePrice, "basePrice");
        this.status = Objects.requireNonNull(status, "status");
        this.supportedOptions = supportedOptions == null ? List.of() : List.copyOf(supportedOptions);
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public ProductType getType() {
        return type;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public List<String> getSupportedOptions() {
        return supportedOptions;
    }

    public boolean isAvailable() {
        return status == ProductStatus.AVAILABLE;
    }

    public Product withStatus(ProductStatus newStatus) {
        return copy(basePrice, Objects.requireNonNull(newStatus, "newStatus"));
    }

    public Product withBasePrice(BigDecimal newBasePrice) {
        return copy(requireNonNegativeMoney(newBasePrice, "newBasePrice"), status);
    }

    protected abstract Product copy(BigDecimal newBasePrice, ProductStatus newStatus);

    private static BigDecimal requireNonNegativeMoney(BigDecimal amount, String fieldName) {
        Objects.requireNonNull(amount, fieldName);
        if (amount.signum() < 0) {
            throw new IllegalArgumentException(fieldName + " must not be negative");
        }
        return amount;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}

