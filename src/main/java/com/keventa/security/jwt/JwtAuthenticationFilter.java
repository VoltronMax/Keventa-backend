package com.keventa.security.jwt;

import com.keventa.security.service.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String headerAuthorization = request.getHeader("Authorization");

        if(headerAuthorization == null
                || !headerAuthorization.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = headerAuthorization.substring(7);

        try {

            final String email = jwtService.extraerEmail(jwt);

            if(email != null
                    && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(email);

                //Si token es vigente
                if(jwtService.validarToken(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken tokenAutenticacion = new UsernamePasswordAuthenticationToken
                            (userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    //Crear Authentication
                    tokenAutenticacion.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    //Guardar Authentication en el SecurityContext de SpringSecurity
                    SecurityContextHolder.getContext()
                            .setAuthentication(tokenAutenticacion);
                }
            }

        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
        }


        filterChain.doFilter(request, response);
    }
}
