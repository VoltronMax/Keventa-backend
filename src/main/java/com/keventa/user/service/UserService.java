package com.keventa.user.service;

import com.keventa.user.dto.ChangeRolRequest;
import com.keventa.user.dto.CreateUserRequest;
import com.keventa.user.dto.UpdateUserRequest;
import com.keventa.user.dto.UserResponse;
import com.keventa.user.enums.UserRole;

import java.util.List;

public interface UserService {

    UserResponse registrarUsuario(CreateUserRequest request);
    UserResponse actualizarUsuario(Long id, UpdateUserRequest request);
    UserResponse obtenerUsuarioPorId(Long id);
    List<UserResponse> obtenerUsuarios();
    void eliminarUsuario(Long id);
    UserResponse cambiarRol(Long id, ChangeRolRequest request);
}
