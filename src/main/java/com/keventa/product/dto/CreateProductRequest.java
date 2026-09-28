package com.keventa.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequest(

        @NotEmpty
        @Schema(description = "Nombre del producto a registrar", example = "Camiseta")
        String name,

        @NotNull
        @Positive
        @Schema(description = "Precio de compra de la prenda para la tienda", example = "20.000")
        BigDecimal purchasePrice,

        @NotNull
        @Positive
        @Schema(description = "Precio de venta al publico", example = "40.000")
        BigDecimal salePrice,

        @NotNull
        @PositiveOrZero
        @Schema(description = "Cantidad de producto disponible en el inventario", example = "3")
        Integer stock
) {
}
