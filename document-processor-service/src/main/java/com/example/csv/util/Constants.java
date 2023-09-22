package com.example.csv.util;

public interface Constants {

    String CSV_TYPE = "text/csv";
    Integer BULK_SIZE = 1000; // change as per requirements
    Integer CHUNK_SIZE = 10000; // change as per requirements
    String VALID = "VALID";

    interface TABLE {
        interface NAME {
            String LEAD = "lead";
            String TEMP_LEAD = "temp_lead";
            String ERROR_LEAD = "error_lead";
            String BATCH_JOB = "batch_job";
        }
    }
}
