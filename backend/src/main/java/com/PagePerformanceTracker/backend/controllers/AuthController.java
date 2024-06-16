package com.PagePerformanceTracker.backend.controllers;

import com.PagePerformanceTracker.backend.dto.JwtAuthenticationResponse;
import com.PagePerformanceTracker.backend.dto.SignInRequest;
import com.PagePerformanceTracker.backend.dto.SignUpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.PagePerformanceTracker.backend.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }

    @PostMapping("/signin")
    public JwtAuthenticationResponse signin(@RequestBody SignInRequest request) {
        return authenticationService.signin(request);
    }
}