package com.example.csv.core.repository;

import com.example.csv.core.entities.TempLead;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempLeadRepository extends CrudRepository<TempLead, Long> {

    @Query("SELECT COUNT(t.id) FROM TempLead t WHERE t.jobId = :jobId and t.error is not null")
    int countErrorByJobId(int jobId);

    @Query("SELECT t FROM TempLead t WHERE t.jobId = :jobId and t.error is not null")
    List<TempLead> getErrorsByJobId(int jobId);
}
