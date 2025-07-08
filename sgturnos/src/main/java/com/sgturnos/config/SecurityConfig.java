package com.sgturnos.config;

import com.sgturnos.security.CustomLoginSuccessHandler;
import com.sgturnos.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomLoginSuccessHandler successHandler;

    @Bean
    public CustomUserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authenticationProvider(daoAuthenticationProvider())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/sgturnos/login",
                "/sgturnos/login?error",
                "/sgturnos/login?logout",
                "/sgturnos/error_rol",
                "/sgturnos/registro",
                "/sgturnos/recuperar",
                "/estilos.css",
                "/css/**",
                "/js/**",
                "/images/**"
            ).permitAll()
            .requestMatchers("/sgturnos/dashboard_admin").hasRole("ADMIN")
            .requestMatchers("/sgturnos/dashboard_usuario").hasAnyRole("AUX", "ENF", "MED", "TER")
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/sgturnos/login") // ✅ Cambiar aquí
            .successHandler(successHandler)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/sgturnos/logout")
            .logoutSuccessUrl("/sgturnos/login?logout")
            .permitAll()
        );

    return http.build();
}
}