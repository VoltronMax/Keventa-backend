package com.keventa.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @Size(min = 1)
        @Schema(example = "Kevin Hencker", description = "Nombre completo del usuario")
        String name,

        @Email
        @Schema(example = "HenckerKev@gmail.com", description = "Correo electronico unico del usuario")
        String email,

        @Size(min = 8)
        @Schema(example = "micontraseñasegura67", description = "Contraseńa del usuario sin encript")
        String password
) {
}
