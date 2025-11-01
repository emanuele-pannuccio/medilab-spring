package com.apps.medilab.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apps.medilab.model.Department;
import com.apps.medilab.model.Doctor;
import com.apps.medilab.repository.DepartmentRepository;
import com.apps.medilab.repository.DoctorRepository;
import com.apps.medilab.requests.DepartmentCreationRequest;
import com.apps.medilab.requests.DoctorCreationRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DoctorService implements UserDetailsService {
    
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Doctor> doctor = doctorRepository.findByEmail(username);
        if(doctor.isPresent()){
            Doctor doc = doctor.get();
            return User.builder().username(doc.getEmail())
                                 .password(doc.getPassword())
                                 .build();
        }
        throw new UsernameNotFoundException(username);
    }

    public Doctor create(DoctorCreationRequest request) throws Exception{
        Department department = departmentRepository.findByName(request.getDepartment());
            
        if (department == null) {
            throw new Exception("Department not found: " + request.getDepartment());
        }

        if (doctorRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + request.getEmail());
        }

        Doctor doc = new Doctor();

        doc.setDepartment(department);
        doc.setEmail(request.getEmail());
        doc.setName(request.getName());
        doc.setPassword(passwordEncoder.encode(request.getPassword()));
        return doctorRepository.save(doc);
    }

    public List<Doctor> list(){
        return doctorRepository.findAll();
    }

    public Doctor show(Long id) throws Exception{
        return doctorRepository.findById(id).orElseThrow(()-> new Exception("Doctor not found"));
    }

    public Doctor update(Long id, DoctorCreationRequest request) throws Exception {
        Doctor doc = show(id);

        if (request.getName() != null && !request.getName().isBlank()) {
            doc.setName(request.getName().trim());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            // opzionale: verifica unicità email
            Optional<Doctor> existing = doctorRepository.findByEmail(request.getEmail().trim());
            if (existing.isPresent() && !existing.get().getId().equals(id)) {
                throw new IllegalArgumentException("Email already in use");
            }
            doc.setEmail(request.getEmail().trim());
        }

        if (request.getDepartment() != null && !request.getDepartment().isBlank()) {
            Department department = departmentRepository.findByName(request.getDepartment().trim());
            doc.setDepartment(department);
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            doc.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return doctorRepository.save(doc);
    }

    
    public ResponseEntity<Void> delete(Long id) throws Exception {
        if (!doctorRepository.existsById(id)) {
            throw new Exception("Doctor not found: id=" + id);
        }
        doctorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
