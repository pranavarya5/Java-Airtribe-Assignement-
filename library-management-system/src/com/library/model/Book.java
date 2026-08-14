package com.library.model;

import java.util.Objects;

/**
 * Represents a book in the library system.
 * Demonstrates Encapsulation by exposing properties via controlled accessors.
 */
public class Book {
    private final String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private String genre;
    private BookStatus status;
    private String branchId;

    public Book(String isbn, String title, String author, int publicationYear, String genre, String branchId) {
        this.isbn = Objects.requireNonNull(isbn, "ISBN cannot be null");
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        this.author = Objects.requireNonNull(author, "Author cannot be null");
        this.publicationYear = publicationYear;
        this.genre = genre != null ? genre : "General";
        this.branchId = branchId != null ? branchId : "MAIN";
        this.status = BookStatus.AVAILABLE;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = Objects.requireNonNull(author, "Author cannot be null");
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = Objects.requireNonNull(branchId, "BranchId cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return String.format("Book[ISBN='%s', Title='%s', Author='%s', Year=%d, Genre='%s', Status=%s, Branch='%s']",
                isbn, title, author, publicationYear, genre, status, branchId);
    }
}
