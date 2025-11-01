package com.apps.medilab.service;

import java.net.Authenticator;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.apps.medilab.model.Doctor;
import com.apps.medilab.model.MedicalCase;
import com.apps.medilab.model.Patient;
import com.apps.medilab.requests.MedicalCaseRequestDTO;
import com.apps.medilab.repository.DoctorRepository;
import com.apps.medilab.repository.MedicalCaseRepository;
import com.apps.medilab.repository.PatientRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Service
public class MedicalCaseService {

    private final MedicalCaseRepository medicalCaseRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public MedicalCase create(MedicalCaseRequestDTO request) {

        Doctor doctor = doctorRepository.findById(request.getDoctor())
                                        .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + request.getDoctor()));

        Patient patient = patientRepository.findById(request.getPatient())
                                           .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + request.getPatient()));

        if(!Objects.nonNull(request.getDischarge_date()) && Objects.nonNull(request.getDischarge_description())){
            throw new IllegalArgumentException("Not a valid argument");
        }

        MedicalCase medicalCase = MedicalCase.builder()
                                             .hospitalization_date(request.getHospitalization_date())
                                             .present_illness_history(request.getPresent_illness_history())
                                             .past_illness_history(request.getPast_illness_history())
                                             .clinical_evolution(request.getClinical_evolution())
                                             .discharge_date(request.getDischarge_date())
                                             .discharge_description(request.getDischarge_description())
                                             .doctor(doctor)
                                             .patient(patient)
                                             .status(request.getStatus())
                                             .build();

        return medicalCaseRepository.save(medicalCase);
    }

    public List<MedicalCase> list(){
        return medicalCaseRepository.findAll();
    }

    public Page<MedicalCase> findPaginated(
        Pageable page, 
        Optional<Long> paziente,
        Optional<Long> medico,
        Optional<Long> reparto,
        Optional<String> stato
    ){
        int pageSize = page.getPageSize();
        int currentPage = page.getPageNumber();
        int startItem = currentPage * pageSize;

        List<MedicalCase> list;
        List<MedicalCase> allCases = list();

        if(paziente.isPresent()){
            allCases = allCases.stream().filter(c -> c.getPatient().getId() == paziente.get()).collect(Collectors.toList());
        }
        
        if(medico.isPresent()){
            allCases = allCases.stream().filter(c -> c.getDoctor().getId() == medico.get()).collect(Collectors.toList());
        }

        if(reparto.isPresent()){
            allCases = allCases.stream().filter(c -> c.getDoctor().getDepartment().getId() == reparto.get()).collect(Collectors.toList());
        }

        if(stato.isPresent() && !stato.get().equals("")){
            System.err.println("test");
            allCases = allCases.stream().filter(c -> c.getStatus().name().equalsIgnoreCase(stato.get())).collect(Collectors.toList());
        }

        if(allCases.size() < startItem){
            list = Collections.emptyList();
        }else{
            int toIndex = Math.min(startItem + pageSize, allCases.size());
            list = allCases.subList(startItem, toIndex);
        }

        return new PageImpl<MedicalCase>(list, PageRequest.of(currentPage, pageSize), allCases.size());
    }

    public MedicalCase show(Long id) {
        return medicalCaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MedicalCase not found with id: " + id));
    }

    public void delete(Long id) {
        if (!medicalCaseRepository.existsById(id)) {
            throw new EntityNotFoundException("MedicalCase not found: id=" + id);
        }
        medicalCaseRepository.deleteById(id);
    }

    public MedicalCase update(Long id, MedicalCaseRequestDTO request) {
        MedicalCase d = show(id);

        if (Objects.nonNull(request.getHospitalization_date())) {
            d.setHospitalization_date(request.getHospitalization_date());
        }

        if (Objects.nonNull(request.getPresent_illness_history()) && !request.getPresent_illness_history().isBlank()) {
            d.setPresent_illness_history(request.getPresent_illness_history());
        }

        if (Objects.nonNull(request.getPast_illness_history()) && !request.getPast_illness_history().isBlank()) {
            d.setPast_illness_history(request.getPast_illness_history());
        }

        if (Objects.nonNull(request.getClinical_evolution()) && !request.getClinical_evolution().isBlank()) {
            d.setClinical_evolution(request.getClinical_evolution());
        }

        if (Objects.nonNull(request.getDischarge_date())) {
            d.setDischarge_date(request.getDischarge_date());
        }

        if (Objects.nonNull(request.getDischarge_description()) && !request.getDischarge_description().isBlank()) {
            d.setDischarge_description(request.getDischarge_description());
        }

        if (Objects.nonNull(request.getDoctor())) {
            Doctor doctor = doctorRepository.findById(request.getDoctor())
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + request.getDoctor()));
            d.setDoctor(doctor);
        }

        if (Objects.nonNull(request.getStatus())) {
            d.setStatus(request.getStatus());
        }

        if(!Objects.nonNull(request.getDischarge_date()) && Objects.nonNull(request.getDischarge_description())){
            throw new IllegalArgumentException("Discharge date must be specified!");
        }

        return medicalCaseRepository.save(d);
    }



}
