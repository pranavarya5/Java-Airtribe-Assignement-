package com.library.repository;

import com.library.model.Patron;
import java.util.List;
import java.util.Optional;

/**
 * Interface defining contract for Patron data access.
 */
public interface PatronRepository {
    void save(Patron patron);
    Optional<Patron> findById(String patronId);
    List<Patron> findAll();
    void deleteById(String patronId);
    boolean existsById(String patronId);
}
