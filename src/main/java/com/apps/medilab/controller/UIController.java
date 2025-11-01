package com.apps.medilab.controller;

import com.apps.medilab.enums.MedicalCaseStatus;
import com.apps.medilab.model.Doctor;
import com.apps.medilab.model.MedicalCase;
import com.apps.medilab.repository.DoctorRepository;
import com.apps.medilab.requests.AuthRequestDTO;
import com.apps.medilab.service.DepartmentService;
import com.apps.medilab.service.DoctorService;
import com.apps.medilab.service.MedicalCaseService;
import com.apps.medilab.service.PatientService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@AllArgsConstructor
public class UIController {

    private final DoctorRepository doctorRepository;

    private final PatientService patientService;

    private final DoctorService doctorService;

    private final DepartmentService departmentService;

    private final MedicalCaseService medicalCaseService;

    private final AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String login(Model model) {
        if(isAuthenticated()) return "redirect:/";
        return "login";
    }
    
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
        isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("/")
    public String dashboard(
        Model model,
        @RequestParam Optional<Integer> page,
        @RequestParam Optional<Long> paziente,
        @RequestParam Optional<Long> medico,
        @RequestParam Optional<Long> reparto,
        @RequestParam Optional<String> stato
    ) {
        List<MedicalCase> medicalCases = medicalCaseService.list();
        int currentPage = page.orElse(1);
        int pagesize = 15;
        org.springframework.data.domain.Page<MedicalCase> pageMedicalCase = medicalCaseService.findPaginated(
            PageRequest.of(currentPage-1, pagesize),
            paziente,
            medico,
            reparto,
            stato
        );
        model.addAttribute("user", doctorRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get());

        model.addAttribute("medicalCases", pageMedicalCase);
        
        model.addAttribute("totalCases", IntStream.rangeClosed(
            1, pageMedicalCase.getTotalPages()
        ).boxed().collect(Collectors.toList()));

        model.addAttribute("currentPage", pageMedicalCase.getNumber() + 1); 
        model.addAttribute("totalPages", pageMedicalCase.getTotalPages());

        model.addAttribute("allMedicalCases", medicalCases);
        model.addAttribute("repartoOptions", departmentService.list());
        model.addAttribute("medicoOptions", doctorService.list());
        model.addAttribute("pazienteOptions", patientService.list());
        model.addAttribute("statoOptions", MedicalCaseStatus.values());

        return "dashboard";
    }
}
