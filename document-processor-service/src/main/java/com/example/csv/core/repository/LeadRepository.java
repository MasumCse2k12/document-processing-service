package com.example.csv.core.repository;

import com.example.csv.core.entities.Lead;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepository extends CrudRepository<Lead, Long> {
    int countByJobId(int jobId);
}
