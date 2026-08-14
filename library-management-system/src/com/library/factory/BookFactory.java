package com.library.factory;

import com.library.builder.BookBuilder;
import com.library.model.Book;

/**
 * Factory Pattern implementation to encapsulate Book instantiation rules.
 */
public class BookFactory {

    public static Book createStandardBook(String isbn, String title, String author, int year, String genre, String branchId) {
        return new BookBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .setAuthor(author)
                .setPublicationYear(year)
                .setGenre(genre)
                .setBranchId(branchId)
                .build();
    }

    public static Book createReferenceBook(String isbn, String title, String author, int year, String branchId) {
        return new BookBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .setAuthor(author)
                .setPublicationYear(year)
                .setGenre("Reference")
                .setBranchId(branchId)
                .build();
    }

    public static Book createFictionBook(String isbn, String title, String author, int year, String branchId) {
        return new BookBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .setAuthor(author)
                .setPublicationYear(year)
                .setGenre("Fiction")
                .setBranchId(branchId)
                .build();
    }
}
