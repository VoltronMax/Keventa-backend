package com.keventa.security.jwt;

import com.keventa.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtServiceImp implements JwtService{

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private SecretKey key;

    @PostConstruct
    private void generarLlave(){
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generarToken(User usuario) {

        Date fechaActual = new Date();
        long fechaEmision = fechaActual.getTime(); //Fecha actual en milisegundos
        long fechaExpiracion = fechaEmision + expirationTime; //Fecha actual + duracion del token (24 horas)

        return Jwts.builder() //Constructor del token
                .subject(usuario.getEmail())
                .issuedAt(fechaActual)
                .expiration(new Date(fechaExpiracion))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public String extraerEmail(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    @Override
    public boolean validarToken(String token, UserDetails userDetails) {

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject()
                .equals(userDetails.getUsername());
    }
}
