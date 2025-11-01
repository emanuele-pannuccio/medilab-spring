package com.apps.medilab.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.apps.medilab.model.Patient;
import com.apps.medilab.repository.PatientRepository;
import com.apps.medilab.requests.PatientRequestDTO;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    private String generateHashedName(String name, String birthday, String city) {
        try {
            String data = name + birthday + city;
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec("supersecretkey!".getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            
            byte[] hashBytes = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return "pti-" + bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Errore durante l'hashing del nome", e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public List<Patient> list() {
        return patientRepository.findAll();
    }


    public Patient show(Long id) throws EntityNotFoundException {
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id));
    }

    public Patient update(Long id, PatientRequestDTO dto) {
        Patient patient = show(id); 

        String birthday = Objects.nonNull(dto.getBirthday()) ? dto.getBirthday().toString() : patient.getBirthday().toString();
        String name = Objects.nonNull(dto.getName()) ? dto.getName().toString() : patient.getName().toString();
        String city = Objects.nonNull(dto.getCity()) ? dto.getCity().toString() : patient.getCity().toString();

        String hashedName = generateHashedName(birthday, name, city);

        patient.setName(hashedName);
        patient.setBirthday(Date.valueOf(birthday));
        patient.setCity(city);

        return patientRepository.save(patient); // Salva e restituisce l'entità aggiornata
    }

    
    public Patient create(PatientRequestDTO dto) {
        String hashedName = generateHashedName(dto.getName(), dto.getBirthday().toString(), dto.getCity());

        Optional<Patient> existingPatient = patientRepository.findByName(hashedName);

        if (existingPatient.isPresent()) {
            return existingPatient.get();
        } else {
            Patient newPatient = new Patient();
            newPatient.setName(hashedName);
            newPatient.setBirthday(dto.getBirthday());
            newPatient.setCity(dto.getCity());
            
            Patient savedPatient = patientRepository.save(newPatient);
            return savedPatient;
        }
    }

    public void delete(Long id) {
        Patient patient = show(id); // Assicura che esista prima di cancellare
        patientRepository.delete(patient);
    }



}
