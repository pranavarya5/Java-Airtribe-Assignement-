package com.library.builder;

import com.library.model.Book;

/**
 * Builder Pattern implementation for flexible and readable creation of Book objects.
 */
public class BookBuilder {
    private String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private String genre = "General";
    private String branchId = "MAIN";

    public BookBuilder setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public BookBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public BookBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public BookBuilder setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
        return this;
    }

    public BookBuilder setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public BookBuilder setBranchId(String branchId) {
        this.branchId = branchId;
        return this;
    }

    public Book build() {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ISBN is required.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book Title is required.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Book Author is required.");
        }
        return new Book(isbn, title, author, publicationYear, genre, branchId);
    }
}
