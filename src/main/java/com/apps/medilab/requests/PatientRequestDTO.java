package com.apps.medilab.requests;

import java.sql.Date;
import lombok.Data;

@Data
public class PatientRequestDTO {
    private String name;
    private Date birthday;
    private String city;
}
