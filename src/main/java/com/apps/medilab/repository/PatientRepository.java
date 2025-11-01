package com.apps.medilab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apps.medilab.model.Patient;
import java.util.List;
import java.util.Optional;


public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByName(String name);
}
