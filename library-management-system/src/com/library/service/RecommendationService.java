package com.library.service;

import com.library.model.Book;
import com.library.model.Patron;
import com.library.recommendation.HybridRecommendationStrategy;
import com.library.recommendation.RecommendationStrategy;
import com.library.repository.BookRepository;
import com.library.repository.PatronRepository;
import com.library.util.LoggerUtil;

import java.util.List;
import java.util.logging.Logger;

/**
 * Service providing book recommendation functionality based on patron history and customizable strategy algorithms.
 */
public class RecommendationService {

    private static final Logger LOGGER = LoggerUtil.getLogger(RecommendationService.class);
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private RecommendationStrategy defaultStrategy;

    public RecommendationService(BookRepository bookRepository, PatronRepository patronRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.defaultStrategy = new HybridRecommendationStrategy(bookRepository.findAll());
    }

    public void setStrategy(RecommendationStrategy strategy) {
        if (strategy != null) {
            this.defaultStrategy = strategy;
        }
    }

    public List<Book> getRecommendations(String patronId, int limit) {
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron not found with ID: " + patronId));

        List<Book> candidateBooks = bookRepository.findAll();
        LOGGER.info(String.format("Generating recommendations for Patron [%s] using strategy %s",
                patron.getName(), defaultStrategy.getClass().getSimpleName()));

        return defaultStrategy.recommend(patron, candidateBooks, limit);
    }

    public List<Book> getRecommendationsWithStrategy(String patronId, RecommendationStrategy strategy, int limit) {
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron not found with ID: " + patronId));

        List<Book> candidateBooks = bookRepository.findAll();
        LOGGER.info(String.format("Generating recommendations for Patron [%s] using custom strategy %s",
                patron.getName(), strategy.getClass().getSimpleName()));

        return strategy.recommend(patron, candidateBooks, limit);
    }
}
