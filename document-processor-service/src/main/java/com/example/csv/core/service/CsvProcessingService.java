package com.example.csv.core.service;

import com.example.csv.core.entities.BatchJob;
import com.example.csv.core.entities.TempLead;
import com.example.csv.core.repository.BatchJobRepository;
import com.example.csv.core.repository.LeadRepository;
import com.example.csv.core.repository.TempLeadRepository;
import com.example.csv.core.dto.TempLeadDto;
import com.example.csv.endpoints.request.JobStatusRequest;
import com.example.csv.endpoints.response.FileUploadResponse;
import com.example.csv.endpoints.response.JobStatusResponse;
import com.example.csv.util.AppUtils;
import com.example.csv.util.Constants;
import com.example.csv.util.WriteLeadErrorCsvToResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CsvProcessingService {

    private final StorageService storageService;
    private final BatchJobRepository batchJobRepository;
    private final LeadRepository leadRepository;
    private final TempLeadRepository tempLeadRepository;

    @Qualifier("asyncJobLauncherLead")
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobExplorer jobs;
    @Qualifier("leadJob")
    @Autowired
    private Job leadJob;


    @Autowired
    public CsvProcessingService(StorageService storageService, BatchJobRepository batchJobRepository, LeadRepository leadRepository, TempLeadRepository tempLeadRepository) {
        this.storageService = storageService;
        this.batchJobRepository = batchJobRepository;
        this.leadRepository = leadRepository;
        this.tempLeadRepository = tempLeadRepository;
    }


    public FileUploadResponse upload(MultipartFile file) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        if (file == null || !file.getContentType().equals(Constants.CSV_TYPE)) {
            return new FileUploadResponse("",
                    file.getOriginalFilename(),
                    "Invalid_CSV_File",
                    1);
        }

        String uploadPath = storageService.store(file);
        //Assume valid csv has two columns(email, phone number)
        String isValidFile = AppUtils.isAllColumnsValid(uploadPath, 2);

        if (!isValidFile.equalsIgnoreCase(Constants.VALID)) {
            //jobExecution.stop();
            return new FileUploadResponse("",
                    file.getOriginalFilename(),
                    "Invalid_Data_Format",
                    1);
        } else if (isValidFile.equalsIgnoreCase(Constants.VALID)) {
            JobExecution jobExecution = jobLauncher.run(leadJob, new JobParametersBuilder()
                    .addString("csvPath", uploadPath)
                    .toJobParameters());

            log.info("LEAD JobExecution status : {} ", jobExecution.getStatus());

            BatchJob batchJob = BatchJob.builder()
                    .jobId(jobExecution.getJobId() != null ? jobExecution.getJobId().intValue() : null)
                    .sourceType(0) //  0-> file csv
                    .userDataSource(file.getOriginalFilename())
                    .build();

            batchJob.setCreated(new Date());


            Integer noOfLines = 0;
            // todo: performance check
            try (LineNumberReader reader = new LineNumberReader(new FileReader(uploadPath))) {
                reader.skip(Integer.MAX_VALUE);
                noOfLines = reader.getLineNumber();
                noOfLines--;
                batchJob.setTotalRecords(noOfLines);
            } catch (IOException e) {
                log.error("Error when read line number: " + e.getMessage());
            }

            batchJobRepository.save(batchJob);
            return new FileUploadResponse(String.valueOf(jobExecution.getJobId()),
                    file.getOriginalFilename(),
                    String.valueOf(jobExecution.getStatus()),
                    noOfLines);
        }

        return new FileUploadResponse("",
                file.getOriginalFilename(),
                "Invalid_Data_Format",
                1);
    }

    public JobStatusResponse getJobStatus(JobStatusRequest request) {
        log.info("JobID: " + request.getJobId());

        JobStatusResponse response = new JobStatusResponse();
        try {

            JobInstance jobInstance = jobs.getJobInstance(request.getJobId());
            if (jobInstance == null) return response;
            log.info(jobInstance.getJobName());
            JobExecution jobExecution = jobs.getLastJobExecution(jobInstance);
            if (jobExecution == null) return response;
            response.setStatus(jobExecution.getStatus().name());


            BatchJob batchJob = batchJobRepository.findByJobId(jobInstance.getInstanceId()).orElse(null);
            if (batchJob != null) {
                response.setDataSource(batchJob.getUserDataSource());
                response.setTotal(batchJob.getTotalRecords());
            }

            int jobId = (int) jobInstance.getInstanceId();
            int processed = leadRepository.countByJobId(jobId);
            int failed = tempLeadRepository.countErrorByJobId(jobId);

            log.info("Total processed: " + processed);
            log.info("Total failed: " + failed);
            response.setTotalProcessed(processed);
            response.setTotalFailed(failed);
            response.setLeadErrorList(getLeadDtoListFromEntities(tempLeadRepository.getErrorsByJobId(jobId)));
        } catch (Throwable t) {
            log.error("Lead bulk status caught : ");
            t.printStackTrace();
        }
        return response;
    }

    private List<TempLeadDto> getLeadDtoListFromEntities(List<TempLead> tempLeads) {
        return tempLeads.stream()
                .map(e -> TempLeadDto.builder()
                        .email(e.getEmail())
                        .phone(e.getPhone())
                        .reason(e.getReason())
                        .build())
                .collect(Collectors.toList());
    }

    public void downloadErrorFile(Integer jobId, HttpServletResponse response) throws IOException {
        try {
            //set file name and content type
            String filename = "lead-errors.csv";
            response.setContentType("text/csv");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + filename + "\"");

            WriteLeadErrorCsvToResponse.write(response.getWriter(), tempLeadRepository.getErrorsByJobId(jobId));
        } catch (Exception e) {
            log.error("Caught Exception during download file");
        }
    }

}