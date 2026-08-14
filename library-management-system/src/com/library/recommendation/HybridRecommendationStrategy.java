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
 * Hybrid recommendation strategy combining genre and author preference weights.
 */
public class HybridRecommendationStrategy implements RecommendationStrategy {

    private final Map<String, Book> isbnToBookMap;

    public HybridRecommendationStrategy(List<Book> allBooks) {
        this.isbnToBookMap = allBooks.stream().collect(Collectors.toMap(Book::getIsbn, b -> b, (b1, b2) -> b1));
    }

    @Override
    public List<Book> recommend(Patron patron, List<Book> candidateBooks, int limit) {
        if (patron == null || candidateBooks == null || candidateBooks.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, Integer> genreFreq = new HashMap<>();
        Map<String, Integer> authorFreq = new HashMap<>();
        Set<String> borrowedIsbns = new HashSet<>();

        for (BorrowRecord record : patron.getBorrowingHistory()) {
            borrowedIsbns.add(record.getIsbn());
            Book book = isbnToBookMap.get(record.getIsbn());
            if (book != null) {
                if (book.getGenre() != null) {
                    genreFreq.put(book.getGenre(), genreFreq.getOrDefault(book.getGenre(), 0) + 1);
                }
                if (book.getAuthor() != null) {
                    authorFreq.put(book.getAuthor(), authorFreq.getOrDefault(book.getAuthor(), 0) + 1);
                }
            }
        }

        // Composite Score = (GenreScore * 2) + (AuthorScore * 3)
        return candidateBooks.stream()
                .filter(book -> !borrowedIsbns.contains(book.getIsbn()))
                .sorted(Comparator.comparingInt((Book b) -> {
                    int gScore = genreFreq.getOrDefault(b.getGenre(), 0);
                    int aScore = authorFreq.getOrDefault(b.getAuthor(), 0);
                    return (gScore * 2) + (aScore * 3);
                }).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
