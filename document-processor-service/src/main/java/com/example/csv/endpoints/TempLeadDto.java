package com.example.csv.endpoints;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TempLeadDto {
    String email;
    String phone;
    String reason;
}
