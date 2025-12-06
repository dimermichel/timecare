package com.michelmaia.timecare_core.controller;

import com.michelmaia.timecare_core.model.User;
import com.michelmaia.timecare_core.repository.UserRepository;
import com.michelmaia.timecare_core.security.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    record LoginRequest(@NotBlank String email, @NotBlank String password) {}

    record LoginResponse(String token) {}

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest rq) {
        User user = userRepository.findByEmail(rq.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(rq.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        var role = user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        String token = jwtUtil.generateToken(user.getUsername(), role);
        return new LoginResponse(token);
    }
}
