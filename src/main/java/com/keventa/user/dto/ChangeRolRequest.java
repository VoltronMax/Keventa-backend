package com.keventa.user.dto;

import com.keventa.user.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ChangeRolRequest(
        @NotNull
        @Schema(example = "ADMIN, EMPLOYEE", description = "Rol que se asignara al usuario. ADMIN y EMPLOYEE ")
        UserRole role
) {
}
