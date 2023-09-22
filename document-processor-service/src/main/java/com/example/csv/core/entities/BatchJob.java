package com.example.csv.core.entities;

import com.example.csv.util.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = Constants.TABLE.NAME.BATCH_JOB)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchJob extends BaseEntity {

    @Column(name = "job_id")
    Integer jobId;
    @Column(name = "job_name")
    String jobName;
    @Column(name = "source_type")
    Integer sourceType;
    @Column(name = "data_source")
    String dataSource;
    @Column(name = "user_data_source")
    String userDataSource;
    @Column(name = "total_records")
    Integer totalRecords;
}
