package com.example.csv.batches;

import com.example.csv.core.entities.ErrorLead;
import com.example.csv.core.entities.Lead;
import com.example.csv.core.entities.TempLead;
import com.example.csv.core.repository.ErrorLeadRepository;
import com.example.csv.core.repository.TempLeadRepository;
import com.example.csv.core.service.BeanConverterService;
import com.example.csv.util.BatchConstants;
import com.example.csv.util.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component(BatchConstants.LEAD_PROCESSOR)
@Slf4j
public class LeadProcessor implements ItemProcessor<TempLead, Lead> {
    private Long jobId;

    @Autowired
    private Validator validator;

    private final ErrorLeadRepository errorLeadRepository;
    private final TempLeadRepository tempLeadRepository;
    private final BeanConverterService beanConverterService;

    @Autowired
    public LeadProcessor(BeanConverterService beanConverterService, ErrorLeadRepository errorLeadRepository, TempLeadRepository tempLeadRepository) {
        this.errorLeadRepository = errorLeadRepository;
        this.beanConverterService = beanConverterService;
        this.tempLeadRepository = tempLeadRepository;
    }

    @Override
    public Lead process(TempLead item) throws Exception {
        List<String> errors = new ArrayList<String>();
        try {

            List<String> tempViolations = ValidationUtils.validateTempLead(item);

            if (tempViolations.size() > 0) {
                errors.addAll(tempViolations);
                log.error("Error when validate temp lead!");
            }

            ImmutablePair<Lead, List<String>> tempResponse = beanConverterService.tempToLeadDomain(new Lead(), item, jobId);

            if (tempResponse.getValue() != null && tempResponse.getValue().size() > 0) {
                errors.addAll(tempResponse.getValue());
            }

            if (errors.size() == 0) {

                // insert data
                Lead lead = tempResponse.getKey(); //new Lead();


                Set<ConstraintViolation<Lead>> violations = validator.validate(lead);
                if (violations.size() > 0) {
                    for (ConstraintViolation<Lead> violation : violations) {
                        errors.add(violation.getMessage());
                        log.error("Error: " + violation.getMessageTemplate());
                    }

                    log.error("Error on lead validation!");
                } else {
                    return lead;
                }
            }
        } catch (Exception x) {
            log.error(x.getMessage());
            // write record to error table
            ErrorLead err = ErrorLead.builder()
                    .data(item.toString())
                    .errorDescription(x.getMessage())
                    .jobId(jobId != null ? jobId.intValue() : null)
                    .build();
            err.setCreated(new Date());
            errorLeadRepository.save(err);
            errors.add(x.getMessage());
        } finally {
            if (errors.size() > 0) {
                String err = String.join(", ", errors);
                log.error("Errors: " + err);
                item.setError(err);
                tempLeadRepository.save(item);
            }
        }
        return null;
    }
}
