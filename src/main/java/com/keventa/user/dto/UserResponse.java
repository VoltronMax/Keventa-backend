package com.keventa.user.dto;

import com.keventa.user.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(

         @Schema(description = "Identificador unico del usuario en la base de datos", example = "2")
         Long id,

         @Schema(description = "Nombre completo del usuario registrado", example = "Kevin Hencker")
         String name,

         @Schema(description = "Correo electronico unico del usuario registrado", example = "HenckEv@gmail.com")
         String email,

         @Schema(description = "Rol asignado al usuario registrado", example = "ADMIN, EMPLOYEE")
         UserRole role
) {
}
