package com.apps.medilab.requests;

import java.time.LocalDateTime;

import com.apps.medilab.enums.MedicalCaseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MedicalCaseRequestDTO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime hospitalization_date;
    private String present_illness_history;
    private String past_illness_history;
    private String clinical_evolution;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime discharge_date;
    
    private String discharge_description;
    private Long doctor;
    private Long patient;
    private MedicalCaseStatus status;
}
