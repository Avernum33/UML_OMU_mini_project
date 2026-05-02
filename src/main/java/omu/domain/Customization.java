package omu.domain;

import java.math.BigDecimal;
import java.util.Objects;

public final class Customization {
    private final String name;
    private final String value;
    private final BigDecimal priceAdjustment;

    public Customization(String name, String value, BigDecimal priceAdjustment) {
        this.name = requireText(name, "name");
        this.value = requireText(value, "value");
        this.priceAdjustment = Objects.requireNonNull(priceAdjustment, "priceAdjustment");
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public BigDecimal getPriceAdjustment() {
        return priceAdjustment;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}

