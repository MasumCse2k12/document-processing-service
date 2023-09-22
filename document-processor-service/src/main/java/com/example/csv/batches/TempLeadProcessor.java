package com.example.csv.batches;

import com.example.csv.core.entities.BatchJob;
import com.example.csv.core.entities.TempLead;
import com.example.csv.core.repository.BatchJobRepository;
import com.example.csv.core.service.BeanConverterService;
import com.example.csv.core.dto.TempLeadDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class TempLeadProcessor implements ItemProcessor<TempLeadDto, TempLead> {

    private Long jobId;

    private final BatchJobRepository batchJobRepository;

    private final BeanConverterService beanConverterService;

    @Autowired
    public TempLeadProcessor(BatchJobRepository batchJobRepository, BeanConverterService beanConverterService) {
        this.batchJobRepository = batchJobRepository;
        this.beanConverterService = beanConverterService;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.jobId = stepExecution.getJobExecution().getJobId();
        BatchJob batchJob = batchJobRepository.findByJobId(this.jobId).orElse(null);
    }

    @Override
    public TempLead process(TempLeadDto item) throws Exception {
        try {
            log.info("Trying to save......................................................................");
            TempLead model = beanConverterService.tempDtoToTempDomain(new TempLead(), item, jobId); //new TempLead();
            model.setCreated(new Date());
            return model;
        } catch (Exception e) {
            log.error("Error when convert dto to model..." + e.getMessage());
        }
        return null;
    }
}
