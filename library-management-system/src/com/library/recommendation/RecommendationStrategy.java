package com.library.recommendation;

import com.library.model.Book;
import com.library.model.Patron;

import java.util.List;

/**
 * Strategy Pattern Interface for generating book recommendations for a patron.
 */
public interface RecommendationStrategy {
    /**
     * Generates a list of recommended books for the specified patron from candidate books.
     *
     * @param patron The patron requesting recommendations
     * @param candidateBooks List of all available candidate books in the library
     * @param limit Maximum number of recommendations to return
     * @return List of recommended books sorted by relevance
     */
    List<Book> recommend(Patron patron, List<Book> candidateBooks, int limit);
}
