package com.example.resourcebooking.security;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;


import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth

                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/error").permitAll()

                    // Resource management - ADMIN only
                    .requestMatchers(HttpMethod.POST, "/api/resources/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/api/resources/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.DELETE, "/api/resources/**")
                    .hasRole("ADMIN")

                    // View resources - logged-in users
                    .requestMatchers(HttpMethod.GET, "/api/resources/**")
                    .authenticated()

                    // Create reservation
                    .requestMatchers(HttpMethod.POST, "/api/reservations")
                    .authenticated()

                    // My reservations
                    .requestMatchers(HttpMethod.GET, "/api/reservations/my")
                    .authenticated()

                    // All reservations - ADMIN only
                    .requestMatchers(HttpMethod.GET, "/api/reservations")
                    .hasRole("ADMIN")

                    // Reservation by ID
                    .requestMatchers(HttpMethod.GET, "/api/reservations/**")
                    .authenticated()

                    // Cancel reservation
                    .requestMatchers(HttpMethod.DELETE, "/api/reservations/**")
                    .authenticated()

                    .anyRequest().authenticated()
            )

            .authenticationProvider(authenticationProvider())

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}