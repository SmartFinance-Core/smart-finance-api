package com.dev.smartfinanceapi.config;

import com.dev.smartfinanceapi.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Buscamos el token en la cabecera (header) "Authorization"
        final String authHeader = request.getHeader("Authorization");

        // 2. Si no hay token o no empieza con "Bearer ", lo dejamos pasar (Spring Security lo bloqueará después si es ruta privada)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraemos el token quitando la palabra "Bearer " (los primeros 7 caracteres)
        final String jwt = authHeader.substring(7);

        // 4. Si el token es válido, le decimos a Spring que este usuario tiene permiso
        if (jwtService.isTokenValid(jwt)) {
            String userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Creamos la credencial de acceso temporal
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail, null, new ArrayList<>()
                );

                // Añadimos los detalles de la petición (IP, etc.) al contexto de seguridad
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Le damos el pase al usuario
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 5. Continuamos con el flujo normal
        filterChain.doFilter(request, response);
    }
}