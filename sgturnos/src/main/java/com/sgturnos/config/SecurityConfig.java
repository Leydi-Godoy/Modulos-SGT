package com.sgturnos.config;

import com.sgturnos.security.CustomLoginSuccessHandler;
import com.sgturnos.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomLoginSuccessHandler loginSuccessHandler;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        UserDetailsService customUserDetailsService = null;
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/sgturnos/admin/**").hasRole("ADMIN")
                .requestMatchers("/sgturnos/usuario/**").hasRole("USER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/sgturnos/login")
                .loginProcessingUrl("/sgturnos/login")
                .successHandler(loginSuccessHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/sgturnos/logout")
                .logoutSuccessUrl("/sgturnos/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Usuario administrador en memoria
        UserDetails admin = User.builder()
            .username("admin")  // usuario fijo
            .password(passwordEncoder().encode("admin123")) // contraseña codificada
            .roles("ADMIN")
            .build();

        auth.inMemoryAuthentication()
            .withUser(admin);

        // Usuarios desde base de datos
        auth.authenticationProvider(daoAuthProvider());
    }
}