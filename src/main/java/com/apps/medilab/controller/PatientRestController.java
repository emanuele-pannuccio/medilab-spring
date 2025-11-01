package com.apps.medilab.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apps.medilab.model.Patient;
import com.apps.medilab.requests.PatientRequestDTO;
import com.apps.medilab.response.ApiResponse;
import com.apps.medilab.service.PatientService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
@RestController
@RequestMapping("/api/patient")
public class PatientRestController {
    private final PatientService patientService;
 
    @GetMapping
    public ResponseEntity<ApiResponse> list() {
        List<Patient> patientsPage = patientService.list();
        
        return ResponseEntity.ok(new ApiResponse(200, patientsPage));
    }

    
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody PatientRequestDTO patientRequest) {
        Patient result = patientService.create(patientRequest);
        
        HttpStatus status = HttpStatus.CREATED;
        return ResponseEntity
                .status(status)
                .body(new ApiResponse(status.value(), result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> show(@PathVariable Long id) {
        Patient patient = patientService.show(id);
        return ResponseEntity.ok(new ApiResponse(200, patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody PatientRequestDTO patientRequest) {
        Patient updatedPatient = patientService.update(id, patientRequest);
        return ResponseEntity.ok(new ApiResponse(200, updatedPatient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        patientService.delete(id);
        // Risposta custom come da esempio Laravel
        return ResponseEntity.ok(new ApiResponse(200, Map.of("ok", 1)));
    }

}
