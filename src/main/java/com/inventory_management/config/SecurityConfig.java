package com.inventory_management.config;
import com.inventory_management.security.JwtFilter;
import com.inventory_management.security.RateLimitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final RateLimitFilter rateLimitFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/staff/**").hasRole("OWNER")
                                .requestMatchers("/api/reports/**").hasAnyRole("OWNER", "MANAGER")
                                .requestMatchers("/api/dashboard/**").hasAnyRole("OWNER", "MANAGER")
                                .requestMatchers("/api/products/**").hasAnyRole("OWNER", "MANAGER")
                                .requestMatchers("/api/suppliers/**").hasAnyRole("OWNER", "MANAGER")
                                .requestMatchers("/api/categories/**").hasAnyRole("OWNER", "MANAGER")
                                .requestMatchers("/api/sales/**").hasAnyRole("OWNER", "MANAGER", "STAFF")
                                .anyRequest().authenticated()
                );
        http.addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
}