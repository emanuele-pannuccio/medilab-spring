package com.apps.medilab.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apps.medilab.repository.DoctorRepository;
import com.apps.medilab.requests.AuthRequestDTO;
import com.apps.medilab.response.ApiResponse;
import com.apps.medilab.service.JwtService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")

@AllArgsConstructor
public class AuthRestController {
    private final AuthenticationManager authenticationManager;
    private final DoctorRepository doctorRepository;

    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(@RequestBody AuthRequestDTO user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok().body(
            new ApiResponse(201, Map.of("token", jwtService.generateToken(userDetails)))
        );
    }

}
