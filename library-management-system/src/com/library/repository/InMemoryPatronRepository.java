package com.library.repository;

import com.library.model.Patron;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe In-Memory implementation of PatronRepository.
 */
public class InMemoryPatronRepository implements PatronRepository {
    private final Map<String, Patron> patronStorage = new ConcurrentHashMap<>();

    @Override
    public void save(Patron patron) {
        patronStorage.put(patron.getPatronId(), patron);
    }

    @Override
    public Optional<Patron> findById(String patronId) {
        return Optional.ofNullable(patronStorage.get(patronId));
    }

    @Override
    public List<Patron> findAll() {
        return new ArrayList<>(patronStorage.values());
    }

    @Override
    public void deleteById(String patronId) {
        patronStorage.remove(patronId);
    }

    @Override
    public boolean existsById(String patronId) {
        return patronStorage.containsKey(patronId);
    }
}
