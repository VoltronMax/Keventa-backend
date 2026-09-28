package com.keventa.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateSaleRequest(
        @NotNull
        @Positive
        @Schema(description = "ID del producto a agregar en la venta", example = "1")
        Long productId,

        @NotNull
        @Positive
        @Schema(description = "Cantidad de productos a agregar a la venta. La cantidad debe ser positiva mayor a 0", example = "10")
        Integer quantity
) {
}
