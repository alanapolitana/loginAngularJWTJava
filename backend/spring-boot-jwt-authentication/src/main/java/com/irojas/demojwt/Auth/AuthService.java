package com.irojas.demojwt.Auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.irojas.demojwt.Jwt.JwtService;
import com.irojas.demojwt.User.Role;
import com.irojas.demojwt.User.User;
import com.irojas.demojwt.User.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .role(user.getAuthorities().iterator().next().getAuthority()) // Obtener el rol del usuario autenticado
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .build();

        // Asignar el rol después de crear el usuario
        user.setRole(asignarRole(request.getUsername()));
        userRepository.save(user); // Guardar usuario con el rol asignado

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .role(user.getRole().toString()) // Obtener el rol asignado
                .build();
    }

    private Role asignarRole(String username) {
        if ("admin@admin.com".equals(username)) {
            return Role.ADMIN;
        } else {
            return Role.USER;
        }
    }
}
