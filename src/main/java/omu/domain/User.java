package omu.domain;

import java.util.Objects;

public final class User {
    private final String userId;
    private final String fullName;
    private final UserRole role;
    private final APCard card;

    public User(String userId, String fullName, UserRole role, APCard card) {
        this.userId = requireText(userId, "userId");
        this.fullName = requireText(fullName, "fullName");
        this.role = Objects.requireNonNull(role, "role");
        this.card = Objects.requireNonNull(card, "card");
    }

    public String getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public UserRole getRole() {
        return role;
    }

    public APCard getCard() {
        return card;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}

