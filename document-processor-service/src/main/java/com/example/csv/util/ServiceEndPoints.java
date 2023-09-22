package com.example.csv.util;

public interface ServiceEndPoints {

    interface CSV_CONTROLLER_MAPPING {
        String FILE_PROCESSING_CONTROLLER = "/api/csv-file-processing";
    }

    interface UPLOADS {
        String UPLOAD = "/upload";
        String STATUS = "/status";
        String DOWNLOAD__ERROR_FILE = "/downloadErrorFile/{jobId}";
    }
}
