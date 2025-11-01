package com.apps.medilab.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

enum MedicalCaseStatus {
    APERTO,
    REVISIONE,
    CHIUSO,
    ANALISI
}

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter @Setter
public class MedicalCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    

    @Column(nullable = false)
    private Date hospitalization_date;

    @Column(nullable = false)
    private String present_illness_history;

    @Column(nullable = false)
    private String past_illness_history;

    private String clinical_evolution;

    private Date discharge_date;

    private String discharge_description;

    @Column(columnDefinition = "enum('Aperto','Analisi','Revisione','Chiuso')")
    @Enumerated(EnumType.STRING)
    private MedicalCaseStatus status;

    @ManyToOne
    @JoinColumn(name = "doctorId", nullable = false)
    private Doctor doctor;

    
    @ManyToOne
    @JoinColumn(name = "patientId", nullable = false)
    private Patient patient;
}
