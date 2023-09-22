package com.example.csv.batches;

import com.example.csv.core.entities.ErrorLead;
import com.example.csv.core.entities.Lead;
import com.example.csv.core.repository.ErrorLeadRepository;
import com.example.csv.core.repository.LeadRepository;
import com.example.csv.util.BatchConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component(BatchConstants.LEAD_WRITTER)
@Slf4j
public class LeadWritter implements ItemWriter<Lead> {

    private final LeadRepository leadRepository;
    private final ErrorLeadRepository errorLeadRepository;

    private Long jobId;

    @Autowired
    public LeadWritter(LeadRepository leadRepository, ErrorLeadRepository errorLeadRepository) {
        this.leadRepository = leadRepository;
        this.errorLeadRepository = errorLeadRepository;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.jobId = stepExecution.getJobExecution().getJobId();
    }

    @Override
    public void write(List<? extends Lead> items) throws Exception {
        try {
            /**
             * Option 1
             * Could be save bulk size entity at a time using  repository saveAll
             */
//            leadRepository.saveAll(items);

            /**
             * Option 2
             * There might be a one ore more items that could not save after pre validation
             * Because of unique constraint in db level which could not be validate in code base
             * As code(repository) level validation has some extra cost in case of huge amount data
             * When user try to upload same data, system should block duplicate items
             * Pass single or more duplicate entity using try catch block
             */
            for (Lead lead : items) {
                try {
                    leadRepository.save(lead);
                } catch (Exception x) {
                    ErrorLead err = ErrorLead.builder()
                            .data(lead.toString())
                            .errorDescription(x.getMessage())
                            .jobId(Integer.parseInt(jobId.toString()))
                            .build();
                    err.setCreated(new Date());
                    errorLeadRepository.save(err);
                    log.error(x.getMessage());
                }
            }
        } catch (Exception x) {
            log.error(x.getMessage());
        }
    }
}
