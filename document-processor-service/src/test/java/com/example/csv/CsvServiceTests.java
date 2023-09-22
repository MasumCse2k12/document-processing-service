package com.example.csv;

import com.example.csv.core.service.CsvProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.batch.operations.JobRestartException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SpringBootTest
class CsvServiceTests {

    @Autowired
    CsvProcessingService csvProcessingService;

    @Test
    void csvUploadTest() throws IOException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, org.springframework.batch.core.repository.JobRestartException {
        csvProcessingService.upload(convert("resources/csv/sample-leads.csv"));
    }

    public static MultipartFile convert(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + filePath);
        }

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",             // Form field name
                file.getName(),     // Original file name
                Files.probeContentType(file.toPath()), // Content type
                FileCopyUtils.copyToByteArray(file)    // File content as bytes
        );


        return mockMultipartFile;
    }


}
