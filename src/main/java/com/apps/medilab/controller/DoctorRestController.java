package com.apps.medilab.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.medilab.model.Doctor;
import com.apps.medilab.repository.DoctorRepository;
import com.apps.medilab.requests.DoctorCreationRequest;
import com.apps.medilab.service.DoctorService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public ResponseEntity<Doctor> show(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(doctorService.show(id));
    }
    
    @PostMapping
    public Doctor create(@RequestBody DoctorCreationRequest doctor) throws Exception {
        return doctorService.create(doctor);
    }
    
    
    @PutMapping("{id}")
    public ResponseEntity<Doctor> update(@PathVariable Long id, @RequestBody DoctorCreationRequest doctor) throws Exception {
        return ResponseEntity.ok().body(doctorService.update(id, doctor));
    }

    
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws Exception {
        doctorService.delete(id);
        return ResponseEntity.ok().build();
    }

}
