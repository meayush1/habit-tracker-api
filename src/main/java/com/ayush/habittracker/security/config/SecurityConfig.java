package com.ayush.habittracker.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ayush.habittracker.security.filter.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()) // disable CSRF for APIs
				.authorizeHttpRequests(auth -> auth

						// open endpoints (public)
						.requestMatchers("/auth/**").permitAll().requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
						.permitAll()
						// Admin routes
						.requestMatchers("/admin/**").hasAuthority("ADMIN")

						// User-specific routes
						.requestMatchers("/users/me").hasAuthority("USER")
						// all others require authentication
						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no
																												// sessions
																												// → JWT
				)
				// Add JWT filter BEFORE Spring's username-password filter
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// expose AuthenticationManager for login (optional future use)
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
