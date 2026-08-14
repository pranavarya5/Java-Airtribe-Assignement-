package com.library.service;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Branch;
import com.library.repository.BookRepository;
import com.library.repository.BranchRepository;
import com.library.util.LoggerUtil;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service handling multiple library branches and inter-branch book transfers.
 */
public class BranchService {

    private static final Logger LOGGER = LoggerUtil.getLogger(BranchService.class);
    private final BranchRepository branchRepository;
    private final BookRepository bookRepository;

    public BranchService(BranchRepository branchRepository, BookRepository bookRepository) {
        this.branchRepository = branchRepository;
        this.bookRepository = bookRepository;
    }

    public void addBranch(Branch branch) {
        if (branchRepository.existsById(branch.getBranchId())) {
            throw new IllegalArgumentException("Branch with ID " + branch.getBranchId() + " already exists.");
        }
        branchRepository.save(branch);
        LOGGER.info("Created new library branch: " + branch.getName() + " [ID: " + branch.getBranchId() + "]");
    }

    public Optional<Branch> getBranchById(String branchId) {
        return branchRepository.findById(branchId);
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    /**
     * Transfers a book from its current branch to a target destination branch.
     */
    public void transferBook(String isbn, String targetBranchId) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book with ISBN " + isbn + " not found."));

        Branch targetBranch = branchRepository.findById(targetBranchId)
                .orElseThrow(() -> new IllegalArgumentException("Target branch with ID " + targetBranchId + " not found."));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new IllegalStateException("Book " + book.getTitle() + " cannot be transferred because its status is " + book.getStatus());
        }

        String sourceBranchId = book.getBranchId();
        if (sourceBranchId.equalsIgnoreCase(targetBranchId)) {
            LOGGER.warning("Book is already located at branch " + targetBranchId);
            return;
        }

        // Remove from source branch if recorded
        Optional<Branch> sourceBranchOpt = branchRepository.findById(sourceBranchId);
        sourceBranchOpt.ifPresent(branch -> branch.removeBookIsbn(isbn));

        // Update book branch and status
        book.setStatus(BookStatus.IN_TRANSFER);
        LOGGER.info(String.format("Transfer initiating for '%s' from Branch '%s' to Branch '%s'",
                book.getTitle(), sourceBranchId, targetBranch.getName()));

        // Complete transfer
        book.setBranchId(targetBranchId);
        book.setStatus(BookStatus.AVAILABLE);
        targetBranch.addBookIsbn(isbn);
        bookRepository.save(book);

        LOGGER.info(String.format("Transfer completed! Book '%s' is now available at Branch '%s'",
                book.getTitle(), targetBranch.getName()));
    }
}
