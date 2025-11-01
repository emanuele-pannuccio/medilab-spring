package com.apps.medilab.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.medilab.model.Doctor;
import com.apps.medilab.requests.DoctorRequestDTO;
import com.apps.medilab.response.ApiResponse;
import com.apps.medilab.service.DoctorService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@AllArgsConstructor
@RestController
@RequestMapping("/api/doctor")
public class DoctorRestController {

    private DoctorService doctorService;

    @GetMapping
    public List<Doctor> list() {
        return doctorService.list();
    }
    
    @GetMapping("{id}")
    public ResponseEntity<Object> show(@PathVariable Long id) throws EntityNotFoundException {
        return ResponseEntity.ok().body(doctorService.show(id));
    }
    
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody DoctorRequestDTO doctor) throws EntityNotFoundException, Exception {
        try {
            return ResponseEntity.ok().body(doctorService.create(doctor));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse(400, Map.of("message", e.getMessage()))
            );
        }
    }
    
    
    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody DoctorRequestDTO doctor) throws EntityNotFoundException,Exception {
        return ResponseEntity.ok().body(doctorService.update(id, doctor));
    }

    
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) throws EntityNotFoundException, Exception {
        doctorService.delete(id);
        return ResponseEntity.ok().body(
            new ApiResponse(200, Map.of("message", "deleted"))
        );
    }

}
