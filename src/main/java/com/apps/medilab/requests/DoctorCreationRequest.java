package com.apps.medilab.requests;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DoctorCreationRequest {
    private String name;
    private String email;
    private String password;
    private String department; // Ricevi l'ID del dipartimento
}