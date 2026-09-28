package com.keventa.auth.controller;

import com.keventa.auth.dto.AuthResponse;
import com.keventa.auth.dto.LoginRequest;
import com.keventa.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacion", description = "Controlador encargado de la autenticacion del usuario con generacion de token JWT")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario mediante sus credenciales y genera un token JWT para acceder a los recursos protegidos del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticacion exitosa y tokenn JWT generado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Campos de la autenticacion invalidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciales incorreos o usuario no autenticado", content = @Content)
    })
    public AuthResponse login (
            @RequestBody @Valid LoginRequest request){
        return authService.login(request);
    }
}
