package com.example.csv.batches;

import com.example.csv.core.entities.Lead;
import com.example.csv.core.repository.LeadRepository;
import com.example.csv.util.BatchConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(BatchConstants.LEAD_WRITTER)
@Slf4j
public class LeadWritter implements ItemWriter<Lead> {

    private final LeadRepository leadRepository;

    private Long jobId;

    @Autowired
    public LeadWritter(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.jobId = stepExecution.getJobExecution().getJobId();
    }

    @Override
    public void write(List<? extends Lead> items) throws Exception {
        try {
            leadRepository.saveAll(items);
        } catch (Exception x) {
            log.error(x.getMessage());
        }
    }
}
