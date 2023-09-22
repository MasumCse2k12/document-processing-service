package com.example.csv.core.entities;

import com.example.csv.util.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = Constants.TABLE.NAME.TEMP_LEAD)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TempLead extends BaseEntity {
    @Column(name = "job_id")
    Integer jobId;
    @Column(name = "email")
    String email;
    @Column(name = "phone")
    String phone;
    @Column(name = "reason")
    private String reason;
    @Lob
    @Column(name = "error")
    private String error;
}
