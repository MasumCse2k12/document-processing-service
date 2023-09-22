package com.example.csv.endpoints.api;

import com.example.csv.core.service.CsvProcessingService;
import com.example.csv.endpoints.request.JobStatusRequest;
import com.example.csv.endpoints.response.FileUploadResponse;
import com.example.csv.endpoints.response.JobStatusResponse;
import com.example.csv.util.ServiceEndPoints;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = ServiceEndPoints.CSV_CONTROLLER_MAPPING.FILE_PROCESSING_CONTROLLER)
public class DocumentsController {

    private final CsvProcessingService csvProcessingService;

    @Autowired
    public DocumentsController(CsvProcessingService csvProcessingService) {
        this.csvProcessingService = csvProcessingService;
    }

    @Operation(summary = "Upload CSV file API")
    @PostMapping(value = ServiceEndPoints.UPLOADS.UPLOAD)
    public FileUploadResponse uploadCsvFile(@RequestParam("file ") MultipartFile file) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        return csvProcessingService.upload(file);
    }

    @Operation(summary = "Find Uploaded CSV file status API")
    @GetMapping(value = ServiceEndPoints.UPLOADS.STATUS)
    public JobStatusResponse getUploadedCSVFileStatus(@PathVariable JobStatusRequest request) {
        return csvProcessingService.getJobStatus(request);
    }

    @Operation(summary = "Download uploaded error CSV API")
    @GetMapping(value = ServiceEndPoints.UPLOADS.DOWNLOAD__ERROR_FILE)
    public void downloadErrorFile(@PathVariable("jobId") Integer jobId, HttpServletResponse response) throws Exception {
        csvProcessingService.downloadErrorFile(jobId, response);
    }
}
