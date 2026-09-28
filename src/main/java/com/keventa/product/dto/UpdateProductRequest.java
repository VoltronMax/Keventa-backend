package com.keventa.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductRequest(

        @Size(min = 1)
        @Schema(description = "Nombre nuevo del producto para actualizar", example = "Pantalon bota alta")
        String name,

        @Positive
        @Schema(description = "Nuevo de compra de la prenda para la tienda", example = "40.000")
        BigDecimal purchasePrice,

        @Positive
        @Schema(description = "Nuevo de venta al publico", example = "40.000")
        BigDecimal salePrice,

        @PositiveOrZero
        @Schema(description = "Nueva cantidad de producto disponible en el inventario", example = "3")
        Integer stock
) {
}
