package com.library.recommendation;

import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Patron;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Concrete recommendation strategy analyzing patron's favorite authors.
 */
public class AuthorBasedRecommendationStrategy implements RecommendationStrategy {

    private final Map<String, Book> isbnToBookMap;

    public AuthorBasedRecommendationStrategy(List<Book> allBooks) {
        this.isbnToBookMap = allBooks.stream().collect(Collectors.toMap(Book::getIsbn, b -> b, (b1, b2) -> b1));
    }

    @Override
    public List<Book> recommend(Patron patron, List<Book> candidateBooks, int limit) {
        if (patron == null || candidateBooks == null || candidateBooks.isEmpty()) {
            return new ArrayList<>();
        }

        // Count frequency of authors in patron borrowing history
        Map<String, Integer> authorFrequency = new HashMap<>();
        Set<String> borrowedIsbns = new HashSet<>();

        for (BorrowRecord record : patron.getBorrowingHistory()) {
            borrowedIsbns.add(record.getIsbn());
            Book book = isbnToBookMap.get(record.getIsbn());
            if (book != null && book.getAuthor() != null) {
                authorFrequency.put(book.getAuthor(), authorFrequency.getOrDefault(book.getAuthor(), 0) + 1);
            }
        }

        // Score candidates based on author frequency
        return candidateBooks.stream()
                .filter(book -> !borrowedIsbns.contains(book.getIsbn()))
                .sorted(Comparator.comparingInt((Book b) -> authorFrequency.getOrDefault(b.getAuthor(), 0)).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
