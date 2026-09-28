package com.keventa.user.mapper;

import com.keventa.user.dto.UserResponse;
import com.keventa.user.entity.User;
import com.keventa.user.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user){
        if(user==null){
            throw new IllegalArgumentException("Mapeo de usuario vacio no posible");
        }

        Long id = user.getId();
        String name = user.getName();
        String email = user.getEmail();
        UserRole role = user.getRole();

        return new UserResponse(id, name, email, role);
    }
}
