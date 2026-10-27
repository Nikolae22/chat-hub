package com.backend.service;

import com.backend.api.dto.AuthResponse;
import com.backend.api.dto.LoginRequest;
import com.backend.api.dto.RegisterRequest;
import com.backend.domain.UserAccount;
import com.backend.exception.ConflictException;
import com.backend.exception.ForbiddenException;
import com.backend.respository.UserAccountRepository;
import com.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final AuditService auditService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest request){
        if(userAccountRepository.existsByEmailIgnoreCase(request.email())){
            throw new ConflictException("An account already exists for this email");
        }

        var user=userAccountRepository.save(
                new UserAccount(request.displayName(),
                        request.email(),passwordEncoder.encode(request.password()))
        );

        auditService.record(user.getId(),"USER_REGISTERED","USER",
                user.getId().toString(), Map.of());

        return response(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request){
        var user=userAccountRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(()-> new ForbiddenException("Invalid email or password"));

        if (!user.isEnabled() || !passwordEncoder.matches(request.password(),user.getPasswordHash())){
            throw new ForbiddenException("Invalid email or password");
        }

        return response(user);
    }



    private AuthResponse response(UserAccount user){
        var token=jwtService.issue(user);
        return new AuthResponse(token.value(),"Bearer",token.expiresAt(),
                user.getId(),user.getDisplayName(),user.getEmail());
    }
}
