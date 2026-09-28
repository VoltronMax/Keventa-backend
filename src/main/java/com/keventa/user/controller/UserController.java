package com.keventa.user.controller;

import com.keventa.user.dto.ChangeRolRequest;
import com.keventa.user.dto.CreateUserRequest;
import com.keventa.user.dto.UpdateUserRequest;
import com.keventa.user.dto.UserResponse;
import com.keventa.user.service.UserService;
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
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios",
        description = "Controlador para la gestion de usuarios registrados en el sistema")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    //Obtener usuarios
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Obtener todos los usuarios",
            description = "Devolver una lista con todos los usuarios registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Lista de usuarios registrados con exito"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes")
    })
    public List<UserResponse> obtenerUsuarios(){
        return service.obtenerUsuarios();
    }

    //Registrar usuarios
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar un usuario en el sistema",
            description = "Crea y almacena un usuario en el sistema. El primer usuario creado recibe rol de admin, los demas reciben rol de employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado y registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del usuario a registrar invalidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes"),
            @ApiResponse(responseCode = "409", description = "El correo del usuario a registrar ya se encuentra en uso")
    })
    public UserResponse registrarUsuario(
            @RequestBody @Valid
            CreateUserRequest request){
        return service.registrarUsuario(request);
    }

    //Actualizar usuario
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar los campos especificados de un usuario",
            description = "Modifica campos especificos de un usuario registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Campos de usuario actualizados exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado un usuario con el ID para actualizar", content = @Content),
            @ApiResponse(responseCode = "409", description = "El nuevo correo ya se encuentra en uso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos para actualizar")

    })
    public UserResponse actualizarUsuario(
            @PathVariable @Positive Long id,
            @RequestBody @Valid UpdateUserRequest request){
        return service.actualizarUsuario(id, request);
    }

    //Obtener usuario por ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por su id",
            description = "Busca un usuario especifico registrado que coincida con el ID ingresado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID ingresado no valido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes"),
            @ApiResponse(responseCode = "404", description = "El ID ingresado no coincide con el de algun usuario registrado en el sistema", content = @Content)
    })
    public UserResponse obtenerUsuarioPorId(
            @PathVariable @Positive Long id){
        return service.obtenerUsuarioPorId(id);
    }

    //Eliminar usuario
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un usuario",
            description = "Elimina permanentemente del sistema al usuario a traves de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes"),
            @ApiResponse(responseCode = "404", description = "ID ingresado no coincide con el ID de un usuario registrado")
    })
    public void eliminarUsuario(
            @PathVariable @Positive Long id
    ){
        service.eliminarUsuario(id);
    }

    //Cambiar rol
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/rol")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cambiar el rol de un usuario",
            description = "Permite actualizar explícitamente el rol de un usuario (EMPLOYEE y ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Rol inexistente o ID invalido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuario sin permisos suficientes"),
            @ApiResponse(responseCode = "404", description = "El usuario no existe", content = @Content)
    })
    public UserResponse cambiarRol(
            @PathVariable @Positive Long id,
            @RequestBody @Valid ChangeRolRequest request) {
        return service.cambiarRol(id, request);
    }

}
