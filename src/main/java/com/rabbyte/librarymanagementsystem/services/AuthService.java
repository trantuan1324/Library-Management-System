package com.rabbyte.librarymanagementsystem.services;

import com.rabbyte.librarymanagementsystem.cache.RoleCache;
import com.rabbyte.librarymanagementsystem.dtos.auth.request.LoginRequest;
import com.rabbyte.librarymanagementsystem.dtos.auth.request.RegisterRequest;
import com.rabbyte.librarymanagementsystem.dtos.auth.response.AuthResponse;
import com.rabbyte.librarymanagementsystem.entities.Role;
import com.rabbyte.librarymanagementsystem.entities.Session;
import com.rabbyte.librarymanagementsystem.entities.User;
import com.rabbyte.librarymanagementsystem.repositories.UserRepository;
import com.rabbyte.librarymanagementsystem.security.JwtUtils;
import com.rabbyte.librarymanagementsystem.security.UserDetailsImpl;
import com.rabbyte.librarymanagementsystem.utils.enums.RoleName;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleCache roleCache;
    private final RoleName defaultRoleName = RoleName.ROLE_USER;
    private final JwtUtils jwtUtils;
    private final SessionService sessionService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final UserService userService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("This Email already taken");
        }

        Role roleToAssign = determineRoleForRegistration(request.getRole());

        // Create a new user
        User newUser = new User();
        newUser.setFullName(request.getFullName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(Set.of(roleToAssign));

        User userToDB = userRepository.save(newUser);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userToDB.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(userToDB.getEmail());

        String accessToken = jwtUtils.generateAccessToken(userDetails);
        Session newSession = sessionService.createSession(userToDB);

        long expiresInSeconds = Duration.between(LocalDateTime.now(), newSession.getExpiresAt()).getSeconds();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newSession.getRefreshToken())
                .expiresIn(expiresInSeconds)
                .fullName(userToDB.getFullName())
                .email(userToDB.getEmail())
                .build();
    }

    private Role determineRoleForRegistration(String roleName) {
        Role roleToAssign;

        if (roleName != null && !roleName.trim().isEmpty()) {
            roleToAssign = roleCache.getRoleByName(roleName);

            if (roleToAssign == null)
                throw new RuntimeException("Role not found: " + roleName);

            if (!isSelfRegistrationAllowedRole(roleToAssign.getRoleName().name())) {
                throw new RuntimeException("You are not allowed to register with this role");
            }
        } else {
            roleToAssign = roleCache.getRoleByName(defaultRoleName.name());

            if (roleToAssign == null)
                throw new RuntimeException("Default role is not found");
        }
        log.debug("Role to assign: {}", roleToAssign.getRoleName());

        return roleToAssign;
    }

    private boolean isSelfRegistrationAllowedRole(String roleName) {
        return defaultRoleName.name().equals(roleName);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User loggedUser = userService.findByEmail(request.getEmail());

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtUtils.generateAccessToken(userDetails);

        Session newSession = sessionService.createSession(loggedUser);

        long expiresInSeconds = Duration.between(LocalDateTime.now(), newSession.getExpiresAt()).getSeconds();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newSession.getRefreshToken())
                .expiresIn(expiresInSeconds)
                .fullName(loggedUser.getFullName())
                .email(loggedUser.getEmail())
                .build();
    }

    private UserDetailsImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getPrincipal() instanceof UserDetailsImpl
                ? (UserDetailsImpl) authentication.getPrincipal()
                : null;
    }

    @Transactional
    public void logoutAllDevices() {
        UserDetailsImpl currentUser = getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        sessionService.revokeAllTokensByUser(currentUser.getId());
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public void logoutCurrentDevice(String refreshToken) {
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Refresh token is required");
        }
        sessionService.deleteByToken(refreshToken);
    }
}
