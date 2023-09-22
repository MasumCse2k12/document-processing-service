package com.example.csv.core.service;

import com.example.csv.core.entities.Lead;
import com.example.csv.core.entities.TempLead;
import com.example.csv.endpoints.TempLeadDto;
import com.example.csv.util.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class BeanConverterService {

    @Autowired
    private Validator validator;


    public <T extends Lead> ImmutablePair<T, List<String>> tempToLeadDomain(T lead, TempLead temp, Long jobId) {

        List<String> errors = new ArrayList<String>();
        List<String> tempViolations = ValidationUtils.validateTempLead(temp);

        if (tempViolations.size() > 0) {
            errors.addAll(tempViolations);
            log.error("Error when validate temp lead!");
        }

        if (errors.size() == 0) {

            lead.setJobId(Integer.parseInt(jobId.toString()));
            lead.setEmail(temp.getEmail().trim());
            lead.setPhone(temp.getPhone().trim());
            Set<ConstraintViolation<Lead>> violations = validator.validate(lead);
            if (violations.size() > 0) {
                for (ConstraintViolation<Lead> violation : violations) {
                    errors.add(violation.getMessage());
                    log.error("Error: " + violation.getMessageTemplate());
                }

                log.error("Error on lead validation!");
            } else {
                return new ImmutablePair<>(lead, errors);
            }
        }
        return new ImmutablePair<>(lead, errors);
    }


    public <T extends TempLead> T tempDtoToTempDomain(T model, TempLeadDto temp, Long jobId) {
        model.setJobId(Integer.parseInt(jobId.toString()));
        model.setEmail(temp.getEmail());
        model.setPhone(temp.getPhone());
        return model;
    }
}
