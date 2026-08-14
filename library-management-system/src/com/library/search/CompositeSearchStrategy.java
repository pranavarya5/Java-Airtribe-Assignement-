package com.library.search;

import com.library.model.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Composite Search Strategy allowing combination of multiple strategies with logical AND/OR matching.
 */
public class CompositeSearchStrategy implements SearchStrategy {

    public enum LogicOperator {
        AND, OR
    }

    private final List<SearchStrategy> strategies;
    private final LogicOperator operator;

    public CompositeSearchStrategy(LogicOperator operator, SearchStrategy... strategies) {
        this.operator = operator != null ? operator : LogicOperator.AND;
        this.strategies = new ArrayList<>(Arrays.asList(strategies));
    }

    public void addStrategy(SearchStrategy strategy) {
        if (strategy != null) {
            this.strategies.add(strategy);
        }
    }

    @Override
    public boolean matches(Book book) {
        if (strategies.isEmpty()) return true;

        if (operator == LogicOperator.AND) {
            for (SearchStrategy strategy : strategies) {
                if (!strategy.matches(book)) return false;
            }
            return true;
        } else {
            for (SearchStrategy strategy : strategies) {
                if (strategy.matches(book)) return true;
            }
            return false;
        }
    }
}
