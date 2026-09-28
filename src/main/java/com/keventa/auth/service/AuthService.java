package com.keventa.auth.service;

import com.keventa.auth.dto.AuthResponse;
import com.keventa.auth.dto.LoginRequest;
import com.keventa.common.exception.UserNotFoundException;
import com.keventa.security.jwt.JwtService;
import com.keventa.user.entity.User;
import com.keventa.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager autenticador;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager autenticador, JwtService jwtService, UserRepository userRepository) {
        this.autenticador = autenticador;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public AuthResponse login(LoginRequest request){
        autenticador.authenticate(new UsernamePasswordAuthenticationToken
                        (request.email(),
                        request.password()));

        User usuario = userRepository.findByEmailIgnoreCase(request.email()).
                orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        String token = jwtService.generarToken(usuario);

        return new AuthResponse(usuario.getName(),
                usuario.getRole(),
                token);
    }
}
