package com.example.csv.util;

import com.example.csv.core.entities.TempLead;
import com.example.csv.endpoints.TempLeadDto;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class WriteLeadErrorCsvToResponse {

    public static void write(PrintWriter writer, List<TempLead> tempLeads) {
        try {
            ColumnPositionMappingStrategy<TempLeadDto> mapStrategy
                    = new ColumnPositionMappingStrategy<>();

            mapStrategy.setType(TempLeadDto.class);

            String[] columns = new String[]{"email", "phone", "error"};
            mapStrategy.setColumnMapping(columns);

            StatefulBeanToCsv<TempLeadDto> btcsv = new StatefulBeanToCsvBuilder<TempLeadDto>(writer)
                    .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .withMappingStrategy(mapStrategy)
                    .withSeparator(',')
                    .build();

            btcsv.write(convertToCsvModel(tempLeads));
            writer.close();
        } catch (Exception e) {
            log.error("Error mapping Bean to CSV for Lead.", e);
        }
    }

    private static List<TempLeadDto> convertToCsvModel(List<TempLead> tempLeads) {
        List<TempLeadDto> tempLeadDtos = new ArrayList<>();

        tempLeadDtos.add(new TempLeadDto(
                "email",
                "phone",
                "errors"
        ));

        tempLeads.stream().map(e ->
                TempLeadDto.builder()
                        .email(e.getEmail())
                        .phone(e.getPhone())
                        .reason(e.getReason())
                        .build())
                .collect(Collectors.toList());

        return tempLeadDtos;
    }
}
