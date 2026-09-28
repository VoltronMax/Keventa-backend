package com.keventa.auth.dto;

import com.keventa.user.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
        @Schema(description = "Nombre del usuario autenticado", example = "Manuel Hencker")
        String name,
        @Schema(description = "Rol asignado al usuario. EMPLOYEE y ADMIN", example = "ADMIN")
        UserRole role,
        @Schema(description = "Token de acceso a las diferentes funciones del sistema", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGtleXZlbnRhLmNvbSIsInJvbGUiOiJFTVBMT1lFRSJ9.sixsevensixseven")
        String token

) {
}
