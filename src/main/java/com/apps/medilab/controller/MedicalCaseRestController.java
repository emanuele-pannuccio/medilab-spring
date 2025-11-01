package com.apps.medilab.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.medilab.model.MedicalCase;
import com.apps.medilab.repository.DoctorRepository;
import com.apps.medilab.requests.MedicalCaseRequestDTO;
import com.apps.medilab.response.ApiResponse;
import com.apps.medilab.service.MedicalCaseService;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@AllArgsConstructor
@RestController
@RequestMapping("/api/report")
public class MedicalCaseRestController {

    private final DoctorRepository doctorRepository;
    private final MedicalCaseService medicalCaseService;

    @PostMapping
    public ResponseEntity<Object> create(
        @AuthenticationPrincipal UserDetails doctor,
        @RequestBody MedicalCaseRequestDTO entity
    ) {
        entity.setDoctor(
            doctorRepository.findByEmail(doctor.getUsername()).get().getId()
        );
        MedicalCase medicalCase = medicalCaseService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(201, medicalCase));
    }

    @GetMapping
    public List<MedicalCase> list() {
        return medicalCaseService.list();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> show(@PathVariable Long id) {
        return ResponseEntity.ok().body(
            new ApiResponse(200, medicalCaseService.show(id))
        );
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        medicalCaseService.delete(id);
        return ResponseEntity.ok().body(
            new ApiResponse(200, Map.of("message", "Medical Case deleted successfully"))
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody MedicalCaseRequestDTO request) throws Exception { // <-- Usa un DTO
        return ResponseEntity.ok().body(medicalCaseService.update(id, request));
    }
}
