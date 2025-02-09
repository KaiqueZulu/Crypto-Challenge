package com.matdev.model;

import java.util.Objects;

public class User {
    private final long id;
    private final String userDocument;
    private final String creditCardToken;
    private final long value;

    public User(long id, String userDocument, String creditCardToken, long value) {
        this.id = id;
        this.userDocument = validateNotNull(userDocument, "userDocument");
        this.creditCardToken = validateNotNull(creditCardToken, "creditCardToken");
        this.value = value;
    }

    private String validateNotNull(String value, String fieldName) {
        return Objects.requireNonNull(value, fieldName + " cannot be null");
    }

    public long getId() {
        return id;
    }

    public String getUserDocument() {
        return userDocument;
    }

    public String getCreditCardToken() {
        return creditCardToken;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("User {id=%d, userDocument='%s', creditCardToken='%s', value=%d}",
                id, userDocument, creditCardToken, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return id == user.id &&
                value == user.value &&
                Objects.equals(userDocument, user.userDocument) &&
                Objects.equals(creditCardToken, user.creditCardToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userDocument, creditCardToken, value);
    }
}
