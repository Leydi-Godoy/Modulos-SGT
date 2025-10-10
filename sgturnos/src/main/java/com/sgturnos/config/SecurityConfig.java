package com.sgturnos.config;

import com.sgturnos.security.CustomLoginSuccessHandler;
import com.sgturnos.security.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public PasswordEncoder passwordEncoder() {
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 🔹 Ignorar CSRF para tus APIs
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/sgturnos/usuarios/api/**")
            )

            .authenticationProvider(daoAuthenticationProvider())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/login",
                    "/login?error",
                    "/login?logout",
                    "/logout",
                    "/dashboard_usuario",
                    "/dashboard_admin",
                    "/error_rol",
                    "/novedades",
                    "/planificar_turnos",
                    "/registro",
                    "/lista",
                    "/malla_turnos",
                    "/turno",
                    "/form",
                    "/recuperar",
                    "/estilos.css",
                    "/animac.css",
                    "/css/**",
                    "/js/**",
                    "/test/**",
                    "/static/**",
                    "/images/**"
                ).permitAll()
                .requestMatchers("/admin/dashboard_admin").hasRole("ADMINISTRADOR")
                .requestMatchers("/usuario/dashboard_usuario").hasAnyRole("AUXILIAR", "ENFERMERO", "MEDICO", "TERAPIA")
                .requestMatchers("/sgturnos/usuarios/api/**").authenticated() // 🔹 Asegura que API requiere login
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(successHandler)
                .failureUrl("/login?error")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            // 🔹 AJUSTE: entry point personalizado para APIs JSON
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    if (request.getRequestURI().startsWith("/sgturnos/usuarios/api/")) {
                        // Devuelve JSON en vez de redirigir
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"error\": \"No autorizado\"}");
                    } else {
                        // Para vistas HTML, redirige al login
                        response.sendRedirect("/login");
                    }
                })
            );

        return http.build();
    }
}