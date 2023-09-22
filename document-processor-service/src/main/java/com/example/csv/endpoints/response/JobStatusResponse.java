package com.example.csv.endpoints.response;

import com.example.csv.endpoints.TempLeadDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobStatusResponse {
    private String dataSource;
    private String status;
    private long totalProcessed;
    private long totalFailed;
    private long total;
    List<TempLeadDto> leadErrorList;
}
