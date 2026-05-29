package com.nttdata.ecopetrol.talento.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // Si vas a usar @PreAuthorize a nivel de métodos
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/autenticacion/login", "/usuario/crear").permitAll()
                        // Solo LIDER puede aprobar/rechazar
                        .requestMatchers("/vacaciones/aprobarVacaciones/**",
                                "/vacaciones/rechazarVacaciones/**",
                                "/incapacidad/aprobarIncapacidad/**",
                                "/incapacidad/rechazarIncapacidad/**",
                                "/calamidad/aprobarCalamidad/**",
                                "/calamidad/rechazarCalamidad/**",
                                "/cumpleanio/aprobarDiaCumpleanio/**",
                                "/cumpleanio/rechazarDiaCumpleanio/**"
                        ).hasRole("LIDER")
                        // Crear solicitudes: EMPLEADO o LIDER
                        .requestMatchers("/vacaciones/crearVacaciones",
                                "/incapacidad/crearIncapacidad",
                                "/calamidad/crearCalamidad",
                                "/cumpleanio/crearDiaCumpleanio"
                        ).hasAnyRole("EMPLEADO", "LIDER")
                        .requestMatchers(
                                "/administracion/consolidadoSolicitudes",
                                "/administracion/cierreMes"
                        ).hasRole("PEOPLE")
                        // Otros: autenticado
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
