package com.library.service;

import com.library.model.BorrowRecord;
import com.library.model.Patron;
import com.library.repository.PatronRepository;
import com.library.util.LoggerUtil;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service managing patron profiles and history.
 */
public class PatronService {

    private static final Logger LOGGER = LoggerUtil.getLogger(PatronService.class);
    private final PatronRepository patronRepository;

    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public void addPatron(Patron patron) {
        if (patronRepository.existsById(patron.getPatronId())) {
            throw new IllegalArgumentException("Patron with ID " + patron.getPatronId() + " already exists.");
        }
        patronRepository.save(patron);
        LOGGER.info("Registered new patron: " + patron.getName() + " [ID: " + patron.getPatronId() + "]");
    }

    public void updatePatron(Patron patron) {
        if (!patronRepository.existsById(patron.getPatronId())) {
            throw new IllegalArgumentException("Cannot update. Patron with ID " + patron.getPatronId() + " does not exist.");
        }
        patronRepository.save(patron);
        LOGGER.info("Updated patron details for ID: " + patron.getPatronId());
    }

    public Optional<Patron> getPatronById(String patronId) {
        return patronRepository.findById(patronId);
    }

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    public List<BorrowRecord> getPatronBorrowingHistory(String patronId) {
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron not found with ID: " + patronId));
        return patron.getBorrowingHistory();
    }
}
