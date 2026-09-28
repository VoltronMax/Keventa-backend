package com.keventa.product.controller;

import com.keventa.product.dto.CreateProductRequest;
import com.keventa.product.dto.ProductResponse;
import com.keventa.product.dto.UpdateProductRequest;
import com.keventa.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos",
        description = "Controlador para le gestion de productos registrados en el sistema")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    @Operation(summary = "Obtener los productos registrados en el sistema",
            description = "Muestra todos los productos actualizados y registrados que se encuentran en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos obtenidos exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content)
    })
    public List<ProductResponse>obtenerProductos(){
        return service.obtenerTodosLosProductos();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar producto registrado",
            description = "Actualiza un producto registrado en el sistema de forma parcial o total dependiendo los campos ingresados. Se exceptua el cambio de rol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del producto o ID invalido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    public ProductResponse actualizarProducto(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProductRequest request) {
        return service.actualizarProducto(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Registra un producto en el sistema",
            description = "Registra un solo producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del producto a registrar invalidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse registrarProducto(
            @RequestBody @Valid CreateProductRequest request) {
        return service.registrarProducto(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto relacionado a un ID",
            description = "Muestra un producto asociado al ID ingresado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID del producto invalido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    public ProductResponse obtenerProductoPorId(
            @PathVariable Long id
    ){
        return service.obtenerProductoPorId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar producto del sistema",
            description = "Elimina totalmente del sistema un producto previamente registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID del producto invalido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    public void eliminarProducto(
            @PathVariable @Valid Long id
    ){
        service.eliminarProducto(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/nombre/{name}")
    @Operation(summary = "Obtener producto por su nombre",
            description = "Permite encontrar productos por su nombre especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado con el nombre indicado", content = @Content)
    })
    public ProductResponse obtenerProductoPorNombre(@PathVariable String name){
        return service.obtenerProductoPorNombre(name);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/buscar")
    @Operation(summary = "Obtener productos que contengan una palabra",
            description = "Permite mostrar todos los productos que contengan cierta palabra")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Contenido de texto invalido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado", content = @Content)
    })
    public List<ProductResponse> buscarProductosPorNombre(@RequestParam String name){
        return service.obtenerProductosPorNombre(name);
    }
}
