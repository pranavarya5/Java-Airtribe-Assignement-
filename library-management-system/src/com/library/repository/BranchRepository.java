package com.library.repository;

import com.library.model.Branch;
import java.util.List;
import java.util.Optional;

/**
 * Interface defining contract for Branch data access.
 */
public interface BranchRepository {
    void save(Branch branch);
    Optional<Branch> findById(String branchId);
    List<Branch> findAll();
    void deleteById(String branchId);
    boolean existsById(String branchId);
}
