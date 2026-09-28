package com.keventa.user.service;

import com.keventa.common.exception.EmailAlreadyRegistered;
import com.keventa.common.exception.LastAdminException;
import com.keventa.common.exception.UserNotFoundException;
import com.keventa.user.dto.ChangeRolRequest;
import com.keventa.user.dto.CreateUserRequest;
import com.keventa.user.dto.UpdateUserRequest;
import com.keventa.user.dto.UserResponse;
import com.keventa.user.entity.User;
import com.keventa.user.enums.UserRole;
import com.keventa.user.mapper.UserMapper;
import com.keventa.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    private final UserMapper mapper;
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserServiceImp(UserMapper mapper, UserRepository repository, PasswordEncoder encoder) {
        this.mapper = mapper;
        this.repository = repository;
        this.encoder = encoder;
    }

    @Transactional
    @Override
    public UserResponse registrarUsuario(CreateUserRequest request) {
        if(repository.existsByEmailIgnoreCase(request.email())){
            throw new EmailAlreadyRegistered("Ya hay un usuario con este email registrado");
        }

        User usuario = new User();
        var hContrasena = encoder.encode(request.password());

        usuario.setName(request.name());
        usuario.setEmail(request.email());
        usuario.setPassword(hContrasena);

        if(repository.count()==0){
            usuario.setRole(UserRole.ADMIN);
        } else {
            usuario.setRole(UserRole.EMPLOYEE);
        }
        return mapper.toResponse(usuario);
    }

    @Transactional
    @Override
    public UserResponse actualizarUsuario(Long id, UpdateUserRequest request) {
        User usuario = repository.findById(id).
                orElseThrow(() -> new UserNotFoundException("No existe un usuario con este ID"));

        if(request.name()!=null){
            usuario.setName(request.name());
        }
        if(request.email()!=null){
            if(!request.email().equals(usuario.getEmail()) && repository.existsByEmailIgnoreCase(request.email())){
                throw new EmailAlreadyRegistered("Ya hay un usuario con este email registrado");
            }
            usuario.setEmail(request.email());
        }
        if(request.password()!=null){
            var hContrasena = encoder.encode(request.password());
            usuario.setPassword(hContrasena);
        }

        return mapper.toResponse(usuario);
    }

    @Override
    public UserResponse obtenerUsuarioPorId(Long id) {
        return mapper.toResponse(repository.findById(id).
                orElseThrow(() -> new UserNotFoundException("No existe un usuario con este ID")));
    }

    @Override
    public List<UserResponse> obtenerUsuarios() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public void eliminarUsuario(Long id){
        User usuario = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No existe un usuario con este ID"));
        if(usuario.getRole() == UserRole.ADMIN
                && repository.countByRole(usuario.getRole())==1){
            throw new LastAdminException("No se puede eliminar al unico admin del sistema");
        }
        repository.delete(usuario);
    }

    @Transactional
    @Override
    public UserResponse cambiarRol(Long id, ChangeRolRequest request) {
        User usuario = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No existe un usuario con este ID"));
        if(usuario.getRole() == UserRole.ADMIN
                && request.role() != UserRole.ADMIN &&
                repository.countByRole(usuario.getRole())==1){
            throw new LastAdminException("No se puede modificar el rol al unico admin del sistema");
        }

        usuario.setRole(request.role());
        return mapper.toResponse(usuario);
    }
}
