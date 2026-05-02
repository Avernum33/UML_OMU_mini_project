package omu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public final class OrderItem {
    private final Product product;
    private final int quantity;
    private final List<Customization> customizations;

    public OrderItem(Product product, int quantity, List<Customization> customizations) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
        this.product = Objects.requireNonNull(product, "product");
        this.quantity = quantity;
        this.customizations = customizations == null ? List.of() : List.copyOf(customizations);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Customization> getCustomizations() {
        return customizations;
    }

    public BigDecimal subtotal() {
        BigDecimal customizationTotal = customizations.stream()
                .map(Customization::getPriceAdjustment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return product.getBasePrice()
                .add(customizationTotal)
                .multiply(BigDecimal.valueOf(quantity));
    }
}

