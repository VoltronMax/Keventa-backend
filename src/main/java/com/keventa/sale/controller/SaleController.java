package com.keventa.sale.controller;


import com.keventa.sale.dto.CreateSaleRequest;
import com.keventa.sale.dto.SaleResponse;
import com.keventa.sale.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
@Tag(name = "Ventas",
    description = "Controlador para la gestion de ventas registradas en el sistema")
@SecurityRequirement(name = "bearerAuth")
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    //Registrar venta
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registro de ventas",
            description = "Crea y almacena ventas relacionadas a productos registrados en el sistema y sus cantidades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta registrada exitosamente"),
            @ApiResponse(responseCode = "409", description = "Stock de producto insuficiente", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de la venta a registrar invalidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto inexistente", content = @Content)
    })
    public SaleResponse registrarVenta(
            @RequestBody @Valid CreateSaleRequest request) {
        return service.registrarVenta(request);
    }

    //Cancelar venta
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una venta",
        description = "Cancela una venta registrada y restaura el stock del producto vendido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta cancelada exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID de venta invalido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Venta o producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Esta venta ya se encuentra cancelada", content = @Content)
    })
    public SaleResponse cancelarVenta(
            @PathVariable @Positive Long id) {
        return service.cancelarVenta(id);
    }

    //Obtener venta por ID
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una venta relacionada con un ID",
            description = "Expone una venta asociada con el ID ingresado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta encontrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID de venta invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
    })
    public SaleResponse obtenerVentaPorId(
            @PathVariable @Positive Long id){
        return service.obtenerVentaPorId(id);
    }

    //Obtener ventas
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    @Operation(summary = "Mostrar todas las ventas",
            description = "Expone todas las ventas registradas en el sistema sin importar si estan canceladas o no")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ventas obtenidas exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
    })
    public List<SaleResponse> obtenerVentas(){
        return service.obtenerVentas();
    }
}
