package pl.blackwater.mysqlsecurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.blackwater.mysqlsecurity.data.HttpResponse;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null) {

            if (authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                try {
                    Jwt jwt = jwtDecoder.decode(token);
                    if (jwt.getExpiresAt() != null && jwt.getExpiresAt().isBefore(Instant.now())) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.writeValue(response.getWriter(), HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(Map.of("token", token))
                                .message("Token is expired")
                                .status(HttpStatus.UNAUTHORIZED)
                                .statusCode(HttpStatus.UNAUTHORIZED.value())
                                .build());
                        return;
                    }
                    Collection<GrantedAuthority> authorities = parseAuthoritiesFromToken(jwt); //
                    System.out.println(authorities);

                    Authentication authentication = new JwtAuthenticationToken(jwt, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } catch (JwtException e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //Nie handluje tego exception API-FRONT
                    response.setContentType("application/json");
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writeValue(response.getWriter(), HttpResponse.builder()
                            .timeStamp(now().toString())
                            .data(Map.of("JwtException", e.getMessage()))
                            .message("Token is invalid")
                            .status(HttpStatus.UNAUTHORIZED)
                            .statusCode(HttpStatus.UNAUTHORIZED.value())
                            .build());
                    return;
                } catch (AccessDeniedException e) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writeValue(response.getWriter(), HttpResponse.builder()
                            .timeStamp(now().toString())
                            .data(Map.of("AccessDeniedException", e.getMessage()))
                            .message(e.getMessage())
                            .status(HttpStatus.FORBIDDEN)
                            .statusCode(HttpStatus.FORBIDDEN.value())
                            .build());
                    return;
                }
            }else{
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(response.getWriter(), HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Reject authentication method")
                        .status(HttpStatus.FORBIDDEN)
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .build());
                return;
            }
        }

        filterChain.doFilter(request, response);


    }
    private Collection<GrantedAuthority> parseAuthoritiesFromToken(Jwt jwt) {
        List<String> roles = jwt.getClaim("roles");

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
