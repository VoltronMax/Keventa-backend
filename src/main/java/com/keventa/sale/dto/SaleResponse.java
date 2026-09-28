package com.keventa.sale.dto;

import com.keventa.sale.enums.SaleStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleResponse(

        @Schema(description = "ID asignado a la venta", example = "2")
        Long id,
        @Schema(description = "ID del producto a vender", example = "3")
        Long productId,
        @Schema(description = "Nombre del producto a vender", example = "Jean para dama")
        String productName,
        @Schema(description = "Precio de compra de la prenda para la tienda", example = "20.000")
        BigDecimal purchasePrice,
        @Schema(description = "Precio de venta al publico", example = "50.000")
        BigDecimal salePrice,
        @Schema(description = "Cantidad de producto a comprar", example = "3")
        Integer quantity,
        @Schema(description = "Valor total de la compra. Precio de venta x cantidad", example = "150.000")
        BigDecimal total,
        @Schema(description = "Ganancia de cada venta. (Precio de compra - Precio de venta) x cantidad")
        BigDecimal profit,
        @Schema(description = "Fecha de realizacion de la venta", example = "2026-08-20T19:04:15.382715")
        LocalDateTime saleDate,
        @Schema(description = "Estado actual de la venta. COMPLETED y CANCELLED", example = "COMPLETED")
        SaleStatus status
) {
}
