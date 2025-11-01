package com.apps.medilab.requests;

import lombok.Data;

@Data
public class DoctorRequestDTO {
    private String name;
    private String email;
    private String password;
    private String department; // Ricevi l'ID del dipartimento
}