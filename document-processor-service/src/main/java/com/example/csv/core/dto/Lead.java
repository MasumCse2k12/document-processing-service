package com.example.csv.core.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lead {
    Integer id;
    Integer jobId;
    String email;
    String phone;
}
