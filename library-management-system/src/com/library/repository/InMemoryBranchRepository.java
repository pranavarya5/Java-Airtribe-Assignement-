package com.library.repository;

import com.library.model.Branch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe In-Memory implementation of BranchRepository.
 */
public class InMemoryBranchRepository implements BranchRepository {
    private final Map<String, Branch> branchStorage = new ConcurrentHashMap<>();

    @Override
    public void save(Branch branch) {
        branchStorage.put(branch.getBranchId(), branch);
    }

    @Override
    public Optional<Branch> findById(String branchId) {
        return Optional.ofNullable(branchStorage.get(branchId));
    }

    @Override
    public List<Branch> findAll() {
        return new ArrayList<>(branchStorage.values());
    }

    @Override
    public void deleteById(String branchId) {
        branchStorage.remove(branchId);
    }

    @Override
    public boolean existsById(String branchId) {
        return branchStorage.containsKey(branchId);
    }
}
