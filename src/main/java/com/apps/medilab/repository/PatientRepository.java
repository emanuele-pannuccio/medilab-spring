package com.apps.medilab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apps.medilab.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    
}
