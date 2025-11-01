package com.apps.medilab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apps.medilab.model.MedicalCase;

public interface MedicalCaseRepository extends JpaRepository<MedicalCase, Long> {
    
}
