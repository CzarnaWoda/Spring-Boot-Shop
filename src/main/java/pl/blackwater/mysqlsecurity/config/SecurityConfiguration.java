package pl.blackwater.mysqlsecurity.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.blackwater.mysqlsecurity.jwt.JwtAuthenticationFilter;
import pl.blackwater.mysqlsecurity.service.AuthUserDetailService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AccountAuthenticationProvider authenticationProvider;
    private final AuthUserDetailService authUserDetailService;
    private final JwtAuthenticationFilter authenticationFilter;
    private final JwtDecoder jwtDecoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = security.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        authenticationManagerBuilder.userDetailsService(authUserDetailService);
        security.csrf(AbstractHttpConfigurer::disable);
        security.oauth2ResourceServer(Customizer.withDefaults());
        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));
        security.oauth2ResourceServer(o2auth -> o2auth.jwt(jwtConfigurer -> {
            jwtConfigurer.decoder(jwtDecoder);
            jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter());
        }));
        security.authorizeHttpRequests(
                        auth -> auth.requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                                .requestMatchers("/user/login").permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/user/login")).permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/products/all").permitAll()
                                .requestMatchers(HttpMethod.GET,"index").authenticated()
                                .requestMatchers(HttpMethod.GET, "/user/details").authenticated().
                                requestMatchers("/**").authenticated().anyRequest().hasAnyRole("USER", "ADMIN")
                ).addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManagerBuilder.build())
                .httpBasic(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return security.build();
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // defaults to "scope" or "scp"
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // defaults to "SCOPE_"

        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:4200")); // Dodaj swoje domeny
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*")); // Akceptuj wszystkie nagłówki
        configuration.setMaxAge(3600L);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
