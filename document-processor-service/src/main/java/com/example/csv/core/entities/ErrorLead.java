package com.example.csv.core.entities;

import com.example.csv.util.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = Constants.TABLE.NAME.ERROR_LEAD)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorLead extends BaseEntity {

    @Column(name = "data")
    private String data;

    @Column(name = "error_desc")
    private String errorDescription;

    @Column(name = "job_id")
    private Integer jobId;
}
