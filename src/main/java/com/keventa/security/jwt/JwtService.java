package com.keventa.security.jwt;

import com.keventa.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generarToken(User usuario);
    String extraerEmail(String token);
    boolean validarToken(String token, UserDetails userDetails);
}
