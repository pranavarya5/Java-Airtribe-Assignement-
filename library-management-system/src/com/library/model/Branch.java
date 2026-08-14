package com.library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a physical or logical library branch location.
 */
public class Branch {
    private final String branchId;
    private String name;
    private String location;
    private final List<String> bookIsbns;

    public Branch(String branchId, String name, String location) {
        this.branchId = Objects.requireNonNull(branchId, "Branch ID cannot be null");
        this.name = Objects.requireNonNull(name, "Branch name cannot be null");
        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.bookIsbns = new ArrayList<>();
    }

    public String getBranchId() {
        return branchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Branch name cannot be null");
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = Objects.requireNonNull(location, "Location cannot be null");
    }

    public List<String> getBookIsbns() {
        return Collections.unmodifiableList(bookIsbns);
    }

    public void addBookIsbn(String isbn) {
        if (!bookIsbns.contains(isbn)) {
            bookIsbns.add(isbn);
        }
    }

    public void removeBookIsbn(String isbn) {
        bookIsbns.remove(isbn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(branchId, branch.branchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchId);
    }

    @Override
    public String toString() {
        return String.format("Branch[ID='%s', Name='%s', Location='%s', TotalBooks=%d]",
                branchId, name, location, bookIsbns.size());
    }
}
