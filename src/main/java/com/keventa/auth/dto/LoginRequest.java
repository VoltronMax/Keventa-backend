package com.keventa.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank
        @Email
        @Schema(description = "Correo electronico unico del usuario a iniciar sesion", example = "Henckerdev@gmail.com")
        String email,

        @NotBlank
        @Schema(description = "Contraseña del usuario a iniciar sesion", example = "sixsevenwasaaateleton")
        String password

) {
}
