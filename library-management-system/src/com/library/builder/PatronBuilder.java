package com.library.builder;

import com.library.model.Patron;

/**
 * Builder Pattern implementation for Patron entity creation.
 */
public class PatronBuilder {
    private String patronId;
    private String name;
    private String email;
    private String phone = "";

    public PatronBuilder setPatronId(String patronId) {
        this.patronId = patronId;
        return this;
    }

    public PatronBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PatronBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public PatronBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Patron build() {
        if (patronId == null || patronId.trim().isEmpty()) {
            throw new IllegalArgumentException("Patron ID is required.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Patron Name is required.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Patron Email is required.");
        }
        return new Patron(patronId, name, email, phone);
    }
}
