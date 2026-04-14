package com.rabbyte.librarymanagementsystem.controllers;

import com.rabbyte.librarymanagementsystem.dtos.auth.request.LoginRequest;
import com.rabbyte.librarymanagementsystem.dtos.auth.request.RefreshTokenRequest;
import com.rabbyte.librarymanagementsystem.dtos.auth.request.RegisterRequest;
import com.rabbyte.librarymanagementsystem.dtos.auth.response.AuthResponse;
import com.rabbyte.librarymanagementsystem.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, response.getAccessToken()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, response.getAccessToken()).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request) {
        authService.logoutCurrentDevice(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logoutAll() {
        authService.logoutAllDevices();
        return ResponseEntity.noContent().build();
    }
}
