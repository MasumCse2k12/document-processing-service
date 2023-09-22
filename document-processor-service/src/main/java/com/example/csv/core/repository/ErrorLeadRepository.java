package com.example.csv.core.repository;

import com.example.csv.core.entities.ErrorLead;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorLeadRepository extends CrudRepository<ErrorLead, Long> {
}
