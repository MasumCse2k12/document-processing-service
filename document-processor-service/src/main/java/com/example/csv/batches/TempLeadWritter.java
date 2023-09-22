package com.example.csv.batches;

import com.example.csv.core.entities.TempLead;
import com.example.csv.core.repository.TempLeadRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TempLeadWritter implements ItemWriter<TempLead> {


    private final TempLeadRepository tempLeadRepository;

    @Autowired
    public TempLeadWritter(TempLeadRepository tempLeadRepository) {
        this.tempLeadRepository = tempLeadRepository;
    }

    @Override
    public void write(List<? extends TempLead> items) throws Exception {
        try {
            tempLeadRepository.saveAll(items);
        } catch (Exception e) {
            log.error("Error when write temp data: " + e.getMessage());
        }
    }
}
