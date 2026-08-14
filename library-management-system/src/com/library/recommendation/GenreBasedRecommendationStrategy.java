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
 * Concrete recommendation strategy analyzing patron's borrowed genres.
 */
public class GenreBasedRecommendationStrategy implements RecommendationStrategy {

    private final Map<String, Book> isbnToBookMap;

    public GenreBasedRecommendationStrategy(List<Book> allBooks) {
        this.isbnToBookMap = allBooks.stream().collect(Collectors.toMap(Book::getIsbn, b -> b, (b1, b2) -> b1));
    }

    @Override
    public List<Book> recommend(Patron patron, List<Book> candidateBooks, int limit) {
        if (patron == null || candidateBooks == null || candidateBooks.isEmpty()) {
            return new ArrayList<>();
        }

        // Count frequency of genres in patron borrowing history
        Map<String, Integer> genreFrequency = new HashMap<>();
        Set<String> borrowedIsbns = new HashSet<>();

        for (BorrowRecord record : patron.getBorrowingHistory()) {
            borrowedIsbns.add(record.getIsbn());
            Book book = isbnToBookMap.get(record.getIsbn());
            if (book != null && book.getGenre() != null) {
                genreFrequency.put(book.getGenre(), genreFrequency.getOrDefault(book.getGenre(), 0) + 1);
            }
        }

        // Score candidates based on genre frequency, filter out already borrowed books
        return candidateBooks.stream()
                .filter(book -> !borrowedIsbns.contains(book.getIsbn()))
                .sorted(Comparator.comparingInt((Book b) -> genreFrequency.getOrDefault(b.getGenre(), 0)).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
