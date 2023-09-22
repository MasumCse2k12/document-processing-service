package com.example.csv.core.repository;

import com.example.csv.core.entities.BatchJob;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatchJobRepository extends CrudRepository<BatchJob, Long> {
    Optional<BatchJob> findByJobId(Long jobId);
}
