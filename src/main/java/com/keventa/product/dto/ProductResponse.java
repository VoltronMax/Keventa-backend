package com.keventa.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProductResponse(

        @Schema(description = "ID asignado al producto", example = "1")
        Long id,
        @Schema(description = "Nombre del producto registrado", example = "Pantalon")
        String name,
        @Schema(description = "Precio de compra de la prenda para la tienda", example = "20.000")
        BigDecimal purchasePrice,
        @Schema(description = "Precio de venta al publico", example = "40.000")
        BigDecimal salePrice,
        @Schema(description = "Cantidad de producto disponible en el inventario", example = "3")
        Integer stock
) {
}
