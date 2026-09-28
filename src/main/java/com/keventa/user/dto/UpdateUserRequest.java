package com.keventa.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

        @Size(min = 1)
        @Schema(example = "Kevin Hencker", description = "Nombre completo del usuario para actualizar")
        String name,

        @Email
        @Schema(example = "HenckerKev@gmail.com", description = "Correo unico del usuario para actualizar")
        String email,

        @Size(min = 1)
        @Schema(example = "micontraseñasegura67", description = "Contraseńa del usuario sin encript para actualizar")
        String password
) {
}
